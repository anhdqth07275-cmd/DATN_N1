<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Customer"%>

<%
    String error = (String) request.getAttribute("error");
    Customer old = (Customer) request.getAttribute("customer");
%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Thêm khách hàng</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
rel="stylesheet">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dark-theme.css">
</head>

<body class="container mt-5">

<h2>Thêm khách hàng</h2>

<% if (error != null && !error.isEmpty()) { %>
<div class="alert alert-danger">
    <%=error%>
</div>
<% } %>

<form action="<%=request.getContextPath()%>/khachhang" method="post" id="customerForm">

    <input type="hidden" name="action" value="insert">

    <div class="mb-3">

        <label>Tên khách hàng</label>

        <input
            class="form-control"
            name="customerName"
            value="<%=old != null ? old.getCustomerName() : ""%>"
            required>

    </div>

    <div class="mb-3">

        <label>Số điện thoại</label>

        <input
            class="form-control"
            name="phone"
            value="<%=old != null && old.getPhone() != null ? old.getPhone() : ""%>"
            type="text"
            inputmode="numeric"
            pattern="\d{9,10}"
            minlength="9"
            maxlength="10"
            title="Số điện thoại phải là số và có độ dài từ 9 đến 10 kí tự"
            required>
        <div class="form-text">Chỉ nhập số, độ dài từ 9 đến 10 kí tự.</div>

    </div>

    <div class="mb-3">

        <label>Địa chỉ</label>

        <input
            class="form-control"
            name="address"
            value="<%=old != null && old.getAddress() != null ? old.getAddress() : ""%>">

    </div>

    <div class="mb-3">

        <label>Email</label>

        <input
            class="form-control"
            type="email"
            name="email"
            value="<%=old != null && old.getEmail() != null ? old.getEmail() : ""%>">

    </div>

    <button class="btn btn-primary">

        Lưu

    </button>

    <a href="<%=request.getContextPath()%>/khachhang"
       class="btn btn-secondary">

        Quay lại

    </a>

</form>

<script>
    (function () {
        var phoneInput = document.querySelector('input[name="phone"]');
        var form = document.getElementById('customerForm');

        phoneInput.addEventListener('input', function () {
            this.value = this.value.replace(/\D/g, '').slice(0, 10);
        });

        form.addEventListener('submit', function (e) {
            var val = phoneInput.value.trim();
            if (!/^\d{9,10}$/.test(val)) {
                e.preventDefault();
                alert('Số điện thoại không hợp lệ! Vui lòng nhập số, độ dài từ 9 đến 10 kí tự.');
                phoneInput.focus();
            }
        });
    })();
</script>

</body>

</html>