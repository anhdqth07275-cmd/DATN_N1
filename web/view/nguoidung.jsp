<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.DangKy"%>

<%
    ArrayList<DangKy> list =
            (ArrayList<DangKy>) request.getAttribute("listUser");

    String message = request.getParameter("message");
    String error = request.getParameter("error");
%>

<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">
    <title>Quản lý người dùng</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
          rel="stylesheet">

    <style>

        *{margin:0;padding:0;box-sizing:border-box;}

        body{background:#edf2f7;font-family:"Segoe UI",sans-serif;}

        .wrapper{display:flex;min-height:100vh;}

        .content{flex:1;padding:30px;}

        .box{background:#fff;border-radius:15px;padding:30px;
             box-shadow:0 5px 15px rgba(0,0,0,.08);}

        .page-title{display:flex;justify-content:space-between;
                    align-items:center;margin-bottom:25px;}

        .page-title h2{color:#0f172a;font-weight:700;margin-bottom:5px;}

        .page-title p{color:#64748b;margin:0;}

        .toolbar{display:flex;justify-content:space-between;
                 align-items:center;margin-bottom:20px;gap:15px;}

        .toolbar input{width:250px;}

        thead{background:#0d6efd;color:white;}

        thead th{text-align:center;}

        tbody td{vertical-align:middle;}

        tbody tr:hover{background:#f5f9ff;transition:.2s;}

        .btn-sm{margin-right:5px;}

        .empty-row{height:80px;text-align:center;color:#6c757d;}

        .breadcrumb-link{color:#64748b;text-decoration:none;}

    </style>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dark-theme.css">
</head>
<body>

<div class="wrapper">

    <jsp:include page="menu.jsp"/>

    <div class="content">

        <div class="box">

            <div class="page-title">

                <div>

                    <a class="breadcrumb-link"
                       href="<%=request.getContextPath()%>/view/hethong.jsp">
                        <i class="bi bi-arrow-left"></i> Hệ thống
                    </a>

                    <h2 class="mt-2">
                        <i class="bi bi-people-fill"></i>
                        Quản lý người dùng
                    </h2>

                    <p>
                        Danh sách tài khoản đang sử dụng hệ thống
                    </p>

                </div>

            </div>

            <% if (message != null) { %>
            <div class="alert alert-success"><%=message%></div>
            <% } %>

            <% if (error != null) { %>
            <div class="alert alert-danger"><%=error%></div>
            <% } %>

            <div class="toolbar">

                <div class="left">

                    <a href="<%=request.getContextPath()%>/nguoidung?action=add"
                       class="btn btn-primary">

                        <i class="bi bi-plus-circle-fill"></i>
                        Thêm người dùng

                    </a>

                </div>

                <div class="right">

                    <form action="<%=request.getContextPath()%>/nguoidung"
                          method="post" class="d-flex">

                        <input type="hidden" name="action" value="search">

                        <input type="text" name="keyword" class="form-control me-2"
                               placeholder="Tên đăng nhập / họ tên / email">

                        <button class="btn btn-success">
                            <i class="bi bi-search"></i>
                        </button>

                    </form>

                </div>

            </div>

            <table class="table table-bordered table-hover align-middle">

                <thead>
                <tr>
                    <th width="70">Mã</th>
                    <th>Tên đăng nhập</th>
                    <th>Mật khẩu</th>
                    <th>Họ và tên</th>
                    <th>Email</th>
                    <th width="120">SĐT</th>
                    <th width="160">Vai trò</th>
                    <th width="110">Trạng thái</th>
                    <th width="220">Thao tác</th>
                </tr>
                </thead>

                <tbody>

                <%
                    if (list != null && !list.isEmpty()) {

                        for (DangKy u : list) {
                %>

                <tr>

                    <td>NV<%=String.format("%03d", u.getUserId())%></td>

                    <td><%=u.getUsername()%></td>

                    <td>***</td>

                    <td><%=u.getFullName()%></td>

                    <td><%=u.getEmail()%></td>

                    <td><%=u.getPhone()%></td>

                    <td>
                        <span class="badge bg-secondary">
                            <%=u.getRoleName()%>
                        </span>
                    </td>

                    <td align="center">

                        <% if (u.isStatus()) { %>
                            <span class="badge bg-success">Hoạt động</span>
                        <% } else { %>
                            <span class="badge bg-danger">Đã khóa</span>
                        <% } %>

                    </td>

                    <td align="center">

                        <a href="<%=request.getContextPath()%>/nguoidung?action=edit&id=<%=u.getUserId()%>"
                           class="btn btn-warning btn-sm" title="Sửa">
                            <i class="bi bi-pencil-square"></i>
                        </a>

                        <% if (u.isStatus()) { %>

                        <a href="<%=request.getContextPath()%>/nguoidung?action=lock&id=<%=u.getUserId()%>"
                           class="btn btn-danger btn-sm" title="Khóa tài khoản"
                           onclick="return confirm('Khóa tài khoản này? Người dùng sẽ không thể đăng nhập.')">
                            <i class="bi bi-lock-fill"></i>
                        </a>

                        <% } else { %>

                        <a href="<%=request.getContextPath()%>/nguoidung?action=unlock&id=<%=u.getUserId()%>"
                           class="btn btn-success btn-sm" title="Mở khóa tài khoản"
                           onclick="return confirm('Mở khóa tài khoản này?')">
                            <i class="bi bi-unlock-fill"></i>
                        </a>

                        <% } %>

                        <a href="<%=request.getContextPath()%>/nguoidung?action=resetpassword&id=<%=u.getUserId()%>"
                           class="btn btn-secondary btn-sm" title="Đặt lại mật khẩu về mặc định"
                           onclick="return confirm('Đặt lại mật khẩu của tài khoản này về mặc định (123456)?')">
                            <i class="bi bi-key-fill"></i>
                        </a>

                    </td>

                </tr>

                <%
                        }

                    } else {
                %>

                <tr>
                    <td colspan="9" class="empty-row">
                        <i class="bi bi-database-exclamation"></i>
                        Chưa có người dùng nào trong hệ thống.
                    </td>
                </tr>

                <% } %>

                </tbody>

            </table>

        </div>

    </div>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
