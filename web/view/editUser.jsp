<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.Role"%>
<%@page import="model.DangKy"%>

<%
    DangKy user = (DangKy) request.getAttribute("user");
    ArrayList<Role> listRole = (ArrayList<Role>) request.getAttribute("listRole");
%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">
<title>Sửa người dùng</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
rel="stylesheet">

</head>

<body class="container mt-5">

<h2>Sửa người dùng</h2>

<form action="<%=request.getContextPath()%>/nguoidung" method="post">

    <input type="hidden" name="action" value="update">

    <input type="hidden" name="userId" value="<%=user.getUserId()%>">

    <div class="mb-3">
        <label>Tên đăng nhập</label>
        <input class="form-control" value="<%=user.getUsername()%>" disabled>
    </div>

    <div class="mb-3">
        <label>Mật khẩu</label>
        <input class="form-control" value="***" disabled>
        <div class="form-text">
            Dùng nút "Đặt lại mật khẩu" ở danh sách người dùng để đổi mật khẩu.
        </div>
    </div>

    <div class="mb-3">
        <label>Họ và tên</label>
        <input class="form-control" name="fullName"
               value="<%=user.getFullName()%>" required>
    </div>

    <div class="mb-3">
        <label>Số điện thoại</label>
        <input class="form-control" name="phone" value="<%=user.getPhone()%>">
    </div>

    <div class="mb-3">
        <label>Email</label>
        <input class="form-control" type="email" name="email"
               value="<%=user.getEmail()%>">
    </div>

    <div class="mb-3">

        <label>Vai trò</label>

        <select class="form-select" name="roleId" required>

            <%
                if (listRole != null) {
                    for (Role r : listRole) {
            %>

            <option value="<%=r.getRoleId()%>"
                <%=(r.getRoleId() == user.getRoleId()) ? "selected" : ""%>>
                <%=r.getRoleName()%>
            </option>

            <%
                    }
                }
            %>

        </select>

    </div>

    <div class="mb-3">

        <label>Trạng thái</label>

        <select class="form-select" name="status">

            <option value="1" <%=user.isStatus() ? "selected" : ""%>>
                Hoạt động
            </option>

            <option value="0" <%=!user.isStatus() ? "selected" : ""%>>
                Đã khóa
            </option>

        </select>

    </div>

    <button class="btn btn-primary">Lưu</button>

    <a href="<%=request.getContextPath()%>/nguoidung" class="btn btn-secondary">
        Quay lại
    </a>

</form>

</body>

</html>
