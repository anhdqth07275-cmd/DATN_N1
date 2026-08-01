<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.Customer"%>

<%
ArrayList<Customer> list =
(ArrayList<Customer>)request.getAttribute("listCustomer");

String error = (String) request.getAttribute("error");
String selectedCustomerId = (String) request.getAttribute("selectedCustomerId");
String dueDateInput = (String) request.getAttribute("dueDateInput");

if (selectedCustomerId == null) {
    selectedCustomerId = "";
}
if (dueDateInput == null) {
    dueDateInput = "";
}

java.time.LocalDate todayLd = java.time.LocalDate.now();
String todayStr = todayLd.toString();
String todayVN = todayLd.format(
        java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
%>

<!DOCTYPE html>

<html>

    <head>

        <meta charset="UTF-8">

        <title>Thêm hóa đơn</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
              rel="stylesheet">

        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
              rel="stylesheet">

        <style>

            body{

                background:#edf2f7;
                font-family:Segoe UI;

            }

            .card{

                max-width:700px;
                margin:40px auto;
                border:none;
                border-radius:15px;
                box-shadow:0 5px 15px rgba(0,0,0,.08);

            }

            .card-header{

                background:#0d6efd;
                color:white;
                font-size:22px;
                font-weight:bold;
                text-align:center;
                padding:18px;

            }

            .card-body{

                padding:35px;

            }

            .btn{

                min-width:120px;

            }

        </style>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dark-theme.css">
</head>

    <body>

        <div class="card">

            <div class="card-header">

                <i class="bi bi-receipt-cutoff"></i>

                THÊM HÓA ĐƠN

            </div>

            <div class="card-body">

                <form action="<%=request.getContextPath()%>/hoadon"
                      method="post">

                    <input
                        type="hidden"
                        name="action"
                        value="insert">

                    <div class="mb-3">

                        <label class="form-label">

                            Khách hàng

                        </label>

                        <select
                            id="customerId"
                            name="customerId"
                            class="form-select"
                            required>

                            <option value="">

                                -- Chọn khách hàng --

                            </option>

                            <%

                            if(list!=null){

                            for(Customer c:list){

                            %>

                            <option
                                value="<%=c.getCustomerId()%>"
                                <%=String.valueOf(c.getCustomerId()).equals(selectedCustomerId) ? "selected" : ""%>
                                data-search="<%=c.getPhone()!=null?c.getPhone():""%>"
                                data-sub="<%=c.getPhone()!=null?c.getPhone():"Không có SĐT"%><%=c.getAddress()!=null && !c.getAddress().isEmpty() ? " • " + c.getAddress() : ""%>">

                                <%=c.getCustomerName()%>

                            </option>

                            <%

                            }

                            }

                            %>

                        </select>

                    </div>

                    <% if (error != null && !error.isEmpty()) { %>
                    <div class="alert alert-danger">
                        <i class="bi bi-exclamation-triangle-fill"></i>
                        <%=error%>
                    </div>
                    <% } %>

                    <div class="mb-3">

                        <label class="form-label">

                            Ngày lập hóa đơn

                        </label>

                        <input
                            type="text"
                            class="form-control"
                            value="<%=todayVN%>"
                            disabled>

                        <div class="form-text">

                            Hóa đơn được lập vào thời điểm hiện tại,
                            không thể chỉnh sửa.

                        </div>

                    </div>

                    <div class="mb-3">

                        <label class="form-label">

                            Hạn thanh toán <span class="text-danger">*</span>

                        </label>

                        <input
                            type="date"
                            id="dueDate"
                            name="dueDate"
                            class="form-control"
                            min="<%=todayStr%>"
                            value="<%=dueDateInput%>"
                            required>

                        <div class="form-text">

                            Hạn thanh toán phải lớn hơn hoặc bằng ngày
                            lập hóa đơn (<%=todayVN%>).

                        </div>

                    </div>

                    <div class="mb-3">

                        <div class="alert alert-info mb-0">

                            <i class="bi bi-info-circle"></i>
                            Hóa đơn mới sẽ ở trạng thái
                            <strong>Chưa thanh toán</strong>.
                            Trạng thái sẽ tự động chuyển sang
                            <strong>Đã thanh toán</strong> khi khách
                            hàng thanh toán đủ qua phiếu thu.

                        </div>

                    </div>

                    <div class="text-center mt-4">

                        <button
                            class="btn btn-primary">

                            <i class="bi bi-floppy-fill"></i>

                            Lưu

                        </button>

                        <a
                            href="<%=request.getContextPath()%>/hoadon"
                            class="btn btn-secondary">

                            <i class="bi bi-arrow-left"></i>

                            Quay lại

                        </a>

                    </div>

                </form>

            </div>

        </div>

        <script src="${pageContext.request.contextPath}/js/searchable-select.js"></script>
        <script>
            initSearchableSelect('customerId', {
                placeholder: '🔍 Tìm kiếm khách hàng theo tên hoặc số điện thoại...',
                emptyText: 'Không tìm thấy khách hàng phù hợp'
            });
        </script>

    </body>

</html>