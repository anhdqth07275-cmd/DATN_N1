<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Thêm khách hàng</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
rel="stylesheet">

</head>

<body class="container mt-5">

<h2>Thêm khách hàng</h2>

<form action="<%=request.getContextPath()%>/khachhang" method="post">

    <input type="hidden" name="action" value="insert">

    <div class="mb-3">

        <label>Tên khách hàng</label>

        <input
            class="form-control"
            name="customerName"
            required>

    </div>

    <div class="mb-3">

        <label>Số điện thoại</label>

        <input
            class="form-control"
            name="phone">

    </div>

    <div class="mb-3">

        <label>Địa chỉ</label>

        <input
            class="form-control"
            name="address">

    </div>

    <div class="mb-3">

        <label>Email</label>

        <input
            class="form-control"
            type="email"
            name="email">

    </div>

    <button class="btn btn-primary">

        Lưu

    </button>

    <a href="<%=request.getContextPath()%>/khachhang"
       class="btn btn-secondary">

        Quay lại

    </a>

</form>

</body>

</html>