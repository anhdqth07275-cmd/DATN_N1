<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.DangKy"%>

<%
    DangKy currentUser = (DangKy) session.getAttribute("user");

    if (currentUser == null) {
        response.sendRedirect(request.getContextPath() + "/dangnhap");
        return;
    }

    boolean isAdmin = "Quản trị viên".equals(currentUser.getRoleName());

    if (!isAdmin) {
        response.sendRedirect(request.getContextPath()
                + "/trangchu?error=Ban+khong+co+quyen+truy+cap+chuc+nang+nay.");
        return;
    }
%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">
<title>Hệ thống</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
      rel="stylesheet">

<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
      rel="stylesheet">

<style>

body{
    margin:0;
    background:#f4f6f9;
    font-family:Segoe UI;
}

.wrapper{
    display:flex;
}

.content{
    flex:1;
    padding:30px;
}

.box{
    background:white;
    border-radius:12px;
    padding:25px;
    box-shadow:0 0 5px #ddd;
}

.list-group-item{
    padding:20px;
    font-size:18px;
    display:flex;
    align-items:center;
    gap:12px;
    text-decoration:none;
    color:#0f172a;
    transition:.2s;
}

.list-group-item:hover{
    background:#f5f9ff;
    color:#0d6efd;
}

.list-group-item i{
    font-size:22px;
    color:#0d6efd;
}

.list-group-item.disabled{
    color:#9ca3af;
    cursor:not-allowed;
}

.list-group-item.disabled i{
    color:#9ca3af;
}

.list-group-item small{
    display:block;
    font-size:13px;
    color:#64748b;
}

</style>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dark-theme.css">
</head>

<body>

<div class="wrapper">

    <jsp:include page="menu.jsp"/>

    <div class="content">

        <div class="box">

            <h3 class="mb-4">
                HỆ THỐNG
            </h3>

            <div class="list-group">

                <a href="<%=request.getContextPath()%>/nguoidung"
                   class="list-group-item list-group-item-action">

                    <i class="bi bi-people-fill"></i>

                    <div>
                        Người dùng
                        <small>Quản lý tài khoản, thông tin, vai trò, khóa/mở khóa</small>
                    </div>

                </a>

                <a href="<%=request.getContextPath()%>/phanquyen"
                   class="list-group-item list-group-item-action">

                    <i class="bi bi-shield-lock-fill"></i>

                    <div>
                        Phân quyền
                        <small>Chỉ QTV mới có: cấp quyền truy cập từng phần cho mỗi vai trò</small>
                    </div>

                </a>

                <a href="<%=request.getContextPath()%>/nhatky"
                   class="list-group-item list-group-item-action">

                    <i class="bi bi-journal-text"></i>

                    <div>
                        Nhật ký hoạt động
                        <small>Tra cứu theo khoảng thời gian, tổng hợp thu/chi/thêm/sửa/xóa</small>
                    </div>

                </a>

                <span class="list-group-item disabled">

                    <i class="bi bi-cloud-arrow-up-fill"></i>

                    <div>
                        Sao lưu dữ liệu
                        <small>Sắp ra mắt</small>
                    </div>

                </span>

            </div>

        </div>

    </div>

</div>

</body>
</html>
