<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.HoaDon"%>

<%
    ArrayList<HoaDon> listHoaDon =
            (ArrayList<HoaDon>) request.getAttribute("listHoaDon");
%>

<!DOCTYPE html>
<html>

    <head>

        <meta charset="UTF-8">

        <title>Thêm phiếu thu</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
              rel="stylesheet">

        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
              rel="stylesheet">

        <style>

            body{

                background:#edf2f7;

                font-family:"Segoe UI",sans-serif;

            }

            .box{

                width:700px;

                margin:40px auto;

                background:#fff;

                padding:30px;

                border-radius:15px;

                box-shadow:0 5px 15px rgba(0,0,0,.08);

            }

            h3{

                margin-bottom:25px;

                color:#0d6efd;

                font-weight:700;

            }

        </style>

    </head>

    <body>

        <div class="box">

            <h3>

                <i class="bi bi-cash-stack"></i>

                Thêm phiếu thu

            </h3>

            <form action="<%=request.getContextPath()%>/phieuthu"

                  method="post">

                <input

                    type="hidden"

                    name="action"

                    value="insert">
                <!-- Chọn hóa đơn -->

                <div class="mb-3">

                    <label class="form-label">

                        Hóa đơn

                    </label>

                    <select
                        id="invoice"
                        name="invoiceId"
                        class="form-select"
                        required>

                        <option value="">

                            -- Chọn hóa đơn --

                        </option>

                        <%

                            if(listHoaDon != null){

                                for(HoaDon hd : listHoaDon){

                        %>

                        <option
                            value="<%=hd.getInvoiceId()%>"
                            data-money="<%=hd.getRemainingAmount()%>"
                            data-total="<%=hd.getTotalAmount()%>"
                            data-customer="<%=hd.getCustomerName()%>"
                            data-invoice="<%=hd.getInvoiceCode()%>">

                            <%=hd.getInvoiceCode()%> |

                            <%=hd.getCustomerName()%> |

                            Còn nợ:

                            <%=hd.getRemainingMoney()%> VNĐ

                        </option>

                        <%

                                }

                            }

                        %>

                    </select>

                </div>


                <!-- Số tiền -->

                <div class="mb-3">

                    <label class="form-label">

                        Số tiền thu

                    </label>

                    <input
                        type="number"
                        id="amount"
                        name="amount"
                        class="form-control"
                        min="1"
                        step="1000"
                        required>

                </div>


                <!-- Phương thức -->

                <div class="mb-3">

                    <label class="form-label">

                        Phương thức thanh toán

                    </label>

                    <select
                        name="paymentMethod"
                        class="form-select"
                        required>

                        <option value="Tiền mặt">

                            Tiền mặt

                        </option>

                        <option value="Chuyển khoản">

                            Chuyển khoản

                        </option>

                        <option value="Ví điện tử">

                            Ví điện tử

                        </option>

                    </select>

                </div>


                <!-- Ghi chú -->

                <div class="mb-3">

                    <label class="form-label">

                        Ghi chú

                    </label>

                    <textarea
                        name="note"
                        rows="4"
                        class="form-control"></textarea>

                </div>
                <div class="d-flex justify-content-between">

                    <a href="<%=request.getContextPath()%>/phieuthu"
                       class="btn btn-secondary">

                        <i class="bi bi-arrow-left"></i>

                        Quay lại

                    </a>

                    <button
                        type="submit"
                        class="btn btn-primary">

                        <i class="bi bi-save"></i>

                        Lưu phiếu thu

                    </button>

                </div>

            </form>

        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>


        <script>

            const invoice = document.getElementById("invoice");
            const amount = document.getElementById("amount");
            const form = document.querySelector("form");

// Tạo khu vực hiển thị thông tin hóa đơn
            const info = document.createElement("div");

            info.className = "alert alert-info mt-3";
            info.style.display = "none";

            invoice.parentNode.appendChild(info);

// Hàm định dạng tiền
            function formatMoney(number) {

                return Number(number).toLocaleString("vi-VN");

            }

// Cập nhật thông tin khi chọn hóa đơn
            function updateInvoiceInfo() {

                let option = invoice.options[invoice.selectedIndex];

                if (option.value == "") {

                    info.style.display = "none";

                    amount.value = "";

                    amount.removeAttribute("max");

                    return;

                }

                let debt = Number(option.dataset.money || 0);

                let customer = option.dataset.customer || "";

                let invoiceCode = option.dataset.invoice || "";

                let total = Number(option.dataset.total || debt);

                let paid = total - debt;

                amount.value = debt;

                amount.max = debt;

                info.innerHTML =
                        "<strong>👤 Khách hàng:</strong> " + customer + "<br>"
                        + "<strong>🧾 Hóa đơn:</strong> " + invoiceCode + "<br>"
                        + "<strong>💰 Tổng hóa đơn:</strong> "
                        + formatMoney(total) + " VNĐ<br>"
                        + "<strong>💸 Đã thu:</strong> "
                        + formatMoney(paid) + " VNĐ<br>"
                        + "<strong>❗ Còn nợ:</strong> "
                        + "<span class='text-danger fw-bold'>"
                        + formatMoney(debt)
                        + " VNĐ</span>";

                info.style.display = "block";

            }

// Khi đổi hóa đơn
            invoice.addEventListener("change", updateInvoiceInfo);

// Không cho nhập âm
            amount.addEventListener("input", function () {

                let option = invoice.options[invoice.selectedIndex];

                let debt = Number(option.dataset.money || 0);

                let value = Number(this.value);

                if (value < 0) {

                    this.value = 0;

                    return;

                }

                if (value > debt) {

                    alert("Số tiền thu không được lớn hơn số tiền còn nợ!");

                    this.value = debt;

                }

            });

// Kiểm tra trước khi lưu
            form.addEventListener("submit", function (e) {

                let option = invoice.options[invoice.selectedIndex];

                let debt = Number(option.dataset.money || 0);

                let value = Number(amount.value);

                if (invoice.value == "") {

                    alert("Vui lòng chọn hóa đơn!");

                    invoice.focus();

                    e.preventDefault();

                    return;

                }

                if (value <= 0) {

                    alert("Số tiền thu phải lớn hơn 0!");

                    amount.focus();

                    e.preventDefault();

                    return;

                }

                if (value > debt) {

                    alert("Số tiền thu vượt quá công nợ!");

                    amount.focus();

                    e.preventDefault();

                    return;

                }

            });

// Nếu trang mở sẵn với hóa đơn đã chọn
            updateInvoiceInfo();

        </script>
    </body>

</html>