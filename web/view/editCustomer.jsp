<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Customer"%>

<%
    Customer c = (Customer) request.getAttribute("customer");
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

    <form action="<%=request.getContextPath()%>/khachhang" method="post">

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
                    value="<%=c.getPhone()%>">

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

</body>

</html>