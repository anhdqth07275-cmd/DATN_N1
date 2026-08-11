<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Customer"%>

<%
    Customer c = (Customer) request.getAttribute("customer");
    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">

    <title>Sửa khách hàng</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dark-theme.css">
</head>

<body class="container mt-5">

    <h2 class="mb-4">Sửa khách hàng</h2>

    <% if (error != null && !error.isEmpty()) { %>
    <div class="alert alert-danger">
        <%=error%>
    </div>
    <% } %>

    <form action="<%=request.getContextPath()%>/khachhang" method="post" id="customerForm">

        <input type="hidden"
               name="action"
               value="update">

        <input type="hidden"
               name="customerId"
               value="<%=c.getCustomerId()%>">

        <div class="mb-3">

            <label>Tên khách hàng</label>

            <input
                    class="form-control"
                    name="customerName"
                    value="<%=c.getCustomerName()%>"
                    required>

        </div>

        <div class="mb-3">

            <label>Số điện thoại</label>

            <input
                    class="form-control"
                    name="phone"
                    value="<%=c.getPhone() != null ? c.getPhone() : ""%>"
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

            <label>Email</label>

            <input
                    class="form-control"
                    name="email"
                    value="<%=c.getEmail()%>">

        </div>

        <div class="mb-3">

            <label>Địa chỉ</label>

            <input
                    class="form-control"
                    name="address"
                    value="<%=c.getAddress()%>">

        </div>

        <div class="mb-3">

            <label>Trạng thái</label>

            <select
                    class="form-select"
                    name="status">

                <option value="true"
                        <%=c.isStatus() ? "selected" : ""%>>
                    Hoạt động
                </option>

                <option value="false"
                        <%=!c.isStatus() ? "selected" : ""%>>
                    Ngừng hoạt động
                </option>

            </select>

        </div>

        <button class="btn btn-primary">

            Cập nhật

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