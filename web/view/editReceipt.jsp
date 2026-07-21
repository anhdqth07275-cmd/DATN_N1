<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.HoaDon"%>
<%@page import="model.Receipt"%>

<%
    Receipt receipt =
            (Receipt) request.getAttribute("receipt");

    HoaDon invoice =
            (HoaDon) request.getAttribute("invoice");

    Double maxAmountObj =
            (Double) request.getAttribute("maxAmount");

    double maxAmount = maxAmountObj == null ? 0 : maxAmountObj;
%>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <title>Cập nhật phiếu thu</title>

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

            color:#0d6efd;

            font-weight:700;

            margin-bottom:25px;

        }

    </style>

</head>

<body>

<div class="box">

<h3>

<i class="bi bi-pencil-square"></i>

Cập nhật phiếu thu

</h3>

<%
    String errorMsg = request.getParameter("error");
    if (errorMsg != null && !errorMsg.isEmpty()) {
%>
<div class="alert alert-danger">

    <i class="bi bi-exclamation-triangle-fill"></i>
    <%=errorMsg%>

</div>
<%
    }
%>

<form action="<%=request.getContextPath()%>/phieuthu"

      method="post">

<input
        type="hidden"
        name="action"
        value="update">

<input
        type="hidden"
        name="receiptId"
        value="<%=receipt.getReceiptId()%>">

<!-- Hóa đơn -->

<div class="mb-3"><!-- Hóa đơn -->

<div class="mb-3">

    <label class="form-label">

        Hóa đơn

    </label>

    <select
            class="form-select"
            disabled>

        <option selected>

            <%=invoice != null ? invoice.getInvoiceCode() : receipt.getInvoiceCode()%>

            |

            <%=invoice != null ? invoice.getCustomerName() : ""%>

            |

            Tổng hóa đơn:

            <%=invoice != null ? invoice.getMoney() : "0"%> đ

        </option>

    </select>

    <!-- vẫn gửi invoiceId về Servlet -->
    <input
            type="hidden"
            name="invoiceId"
            value="<%=receipt.getInvoiceId()%>">

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

            value="<%=receipt.getAmount()%>"

            min="1"

            max="<%=(long) maxAmount%>"

            step="1"

            required>

</div>


<!-- Phương thức thanh toán -->

<div class="mb-3">

    <label class="form-label">

        Phương thức thanh toán

    </label>

    <select

            name="paymentMethod"

            class="form-select"

            required>

        <option
                value="Tiền mặt"
                <%=receipt.getPaymentMethod().equals("Tiền mặt")?"selected":""%>>

            Tiền mặt

        </option>

        <option
                value="Chuyển khoản"
                <%=receipt.getPaymentMethod().equals("Chuyển khoản")?"selected":""%>>

            Chuyển khoản

        </option>

        <option
                value="Ví điện tử"
                <%=receipt.getPaymentMethod().equals("Ví điện tử")?"selected":""%>>

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

            class="form-control"><%=receipt.getNote()==null?"":receipt.getNote()%></textarea>

</div>
   <div class="d-flex justify-content-between">

    <a href="<%=request.getContextPath()%>/phieuthu"
       class="btn btn-secondary">

        <i class="bi bi-arrow-left"></i>

        Quay lại

    </a>

    <button
            type="submit"
            class="btn btn-warning">

        <i class="bi bi-pencil-square"></i>

        Cập nhật phiếu thu

    </button>

</div>

</form>

</div>

<script>

const amount = document.getElementById("amount");

const form = document.querySelector("form");

// Hạn mức tối đa được tính đúng ở server (tổng hóa đơn - đã thu
// bởi các phiếu thu KHÁC), không dùng giá trị hiện tại của phiếu
// thu này làm hạn mức (đó là lỗi cũ khiến không thể tăng số tiền
// thu khi sửa phiếu thu).
const debt = Number(amount.max);

// Không cho nhập âm hoặc vượt quá hạn mức
amount.addEventListener("input", function () {

    let value = Number(this.value);

    if (value < 0) {

        this.value = 0;

        return;

    }

    if (value > debt) {

        alert("Số tiền thu không được lớn hơn " + debt.toLocaleString("vi-VN") + " VNĐ!");

        this.value = debt;

    }

});

// Kiểm tra trước khi lưu
form.addEventListener("submit", function (e) {

    let value = Number(amount.value);

    if (value <= 0) {

        alert("Số tiền thu phải lớn hơn 0!");

        amount.focus();

        e.preventDefault();

        return;

    }

    if (value > debt) {

        alert("Số tiền thu vượt quá hạn mức cho phép!");

        amount.focus();

        e.preventDefault();

        return;

    }

});

</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>

</html>         