<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.Role"%>

<%
    ArrayList<Role> listRole = (ArrayList<Role>) request.getAttribute("listRole");
    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">
<title>Thêm người dùng</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
rel="stylesheet">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dark-theme.css">
</head>

<body class="container mt-5">

<h2>Thêm người dùng</h2>

<% if (error != null) { %>
<div class="alert alert-danger"><%=error%></div>
<% } %>

<form action="<%=request.getContextPath()%>/nguoidung" method="post">

    <input type="hidden" name="action" value="insert">

    <div class="mb-3">
        <label>Tên đăng nhập</label>
        <input class="form-control" name="username" required>
    </div>

    <div class="mb-3">
        <label>Mật khẩu</label>
        <input class="form-control" type="password" name="password" required>
    </div>

    <div class="mb-3">
        <label>Họ và tên</label>
        <input class="form-control" name="fullName" required>
    </div>

    <div class="mb-3">
        <label>Số điện thoại</label>
        <input class="form-control" name="phone">
    </div>

    <div class="mb-3">
        <label>Email</label>
        <input class="form-control" type="email" name="email">
    </div>

    <div class="mb-3">

        <label>Vai trò</label>

        <select class="form-select" name="roleId" required>

            <option value="">------ Chọn vai trò ------</option>

            <%
                if (listRole != null) {
                    for (Role r : listRole) {
            %>

            <option value="<%=r.getRoleId()%>"><%=r.getRoleName()%></option>

            <%
                    }
                }
            %>

        </select>

    </div>

    <button class="btn btn-primary">Lưu</button>

    <a href="<%=request.getContextPath()%>/nguoidung" class="btn btn-secondary">
        Quay lại
    </a>

</form>

</body>

</html>
