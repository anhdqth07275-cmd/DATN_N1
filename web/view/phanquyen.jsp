<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.Role"%>
<%@page import="model.Permission"%>

<%
    ArrayList<Role> listRole = (ArrayList<Role>) request.getAttribute("listRole");
    ArrayList<Permission> listPermission =
            (ArrayList<Permission>) request.getAttribute("listPermission");
    Integer selectedRoleId = (Integer) request.getAttribute("selectedRoleId");

    String message = request.getParameter("message");
%>

<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">
    <title>Phân quyền</title>

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

        .page-title h2{color:#0f172a;font-weight:700;margin-bottom:5px;}

        .page-title p{color:#64748b;margin:0 0 20px 0;}

        .breadcrumb-link{color:#64748b;text-decoration:none;}

        .role-tabs a{
            display:inline-block;
            padding:10px 20px;
            margin-right:8px;
            margin-bottom:20px;
            border-radius:8px;
            background:#f1f5f9;
            color:#334155;
            text-decoration:none;
            font-weight:500;
        }

        .role-tabs a.active{
            background:#0d6efd;
            color:#fff;
        }

        .perm-group{
            margin-bottom:20px;
            border:1px solid #e2e8f0;
            border-radius:10px;
            padding:20px;
        }

        .perm-group h5{
            color:#0f172a;
            margin-bottom:15px;
        }

        .form-check{
            padding:10px 0;
            border-bottom:1px dashed #eee;
        }

        .form-check:last-child{
            border-bottom:none;
        }

        .form-check-label small{
            display:block;
            color:#64748b;
        }

    </style>

</head>
<body>

<div class="wrapper">

    <jsp:include page="menu.jsp"/>

    <div class="content">

        <div class="box">

            <div class="page-title">

                <a class="breadcrumb-link"
                   href="<%=request.getContextPath()%>/view/hethong.jsp">
                    <i class="bi bi-arrow-left"></i> Hệ thống
                </a>

                <h2 class="mt-2">
                    <i class="bi bi-shield-lock-fill"></i>
                    Phân quyền
                </h2>

                <p>
                    Chọn 1 vai trò rồi đánh dấu những phần được phép truy cập.
                    Chỉ Quản trị viên mới thấy và chỉnh sửa được màn hình này.
                </p>

            </div>

            <% if (message != null) { %>
            <div class="alert alert-success"><%=message%></div>
            <% } %>

            <div class="role-tabs">

                <%
                    if (listRole != null) {
                        for (Role r : listRole) {

                            boolean active = selectedRoleId != null
                                    && selectedRoleId == r.getRoleId();
                %>

                <a href="<%=request.getContextPath()%>/phanquyen?roleId=<%=r.getRoleId()%>"
                   class="<%=active ? "active" : ""%>">
                    <%=r.getRoleName()%>
                </a>

                <%
                        }
                    }
                %>

            </div>

            <% if (listPermission != null && !listPermission.isEmpty()) { %>

            <form action="<%=request.getContextPath()%>/phanquyen" method="post">

                <input type="hidden" name="roleId" value="<%=selectedRoleId%>">

                <%
                    String currentGroup = "";

                    for (Permission p : listPermission) {

                        if (!p.getModuleGroup().equals(currentGroup)) {

                            if (!currentGroup.equals("")) {
                %>
                            </div>
                <%
                            }

                            currentGroup = p.getModuleGroup();
                %>

                <div class="perm-group">
                    <h5><%=currentGroup%></h5>

                <%
                        }
                %>

                <div class="form-check">

                    <input class="form-check-input" type="checkbox"
                           name="permissionIds" value="<%=p.getPermissionId()%>"
                           id="perm<%=p.getPermissionId()%>"
                           <%=p.isGranted() ? "checked" : ""%>>

                    <label class="form-check-label" for="perm<%=p.getPermissionId()%>">
                        <%=p.getPermissionName()%>
                        <small><%=p.getDescription()%></small>
                    </label>

                </div>

                <%
                    }
                %>

                </div>

                <button class="btn btn-primary mt-3">
                    <i class="bi bi-check-circle-fill"></i>
                    Lưu phân quyền
                </button>

            </form>

            <% } else { %>

            <p class="text-muted">Chưa có dữ liệu quyền nào trong hệ thống.</p>

            <% } %>

        </div>

    </div>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
