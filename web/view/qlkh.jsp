<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashSet"%>
<%@page import="model.Customer"%>
<%@taglib prefix="my" tagdir="/WEB-INF/tags" %>

<%
    ArrayList<Customer> list =
            (ArrayList<Customer>) request.getAttribute("listCustomer");

    Boolean canSoftDelete = (Boolean) request.getAttribute("canSoftDelete");
    Boolean canAdd = (Boolean) request.getAttribute("canAdd");
    Boolean canEdit = (Boolean) request.getAttribute("canEdit");
    Boolean showInactive = (Boolean) request.getAttribute("showInactive");
    HashSet<Integer> pendingIds =
            (HashSet<Integer>) request.getAttribute("pendingIds");

    if (canSoftDelete == null) canSoftDelete = false;
    if (canAdd == null) canAdd = false;
    if (canEdit == null) canEdit = false;
    if (showInactive == null) showInactive = false;
    if (pendingIds == null) pendingIds = new HashSet<>();
%>

<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">
    <title>Quản lý khách hàng</title>

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">

    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
          rel="stylesheet">

    <style>

        *{
            margin:0;
            padding:0;
            box-sizing:border-box;
        }

        body{
            background:#edf2f7;
            font-family:"Segoe UI",sans-serif;
        }

        .wrapper{
            display:flex;
            min-height:100vh;
        }

        /* =========================
              CONTENT
        ========================== */

        .content{
            flex:1;
            padding:30px;
        }

        /* =========================
              CARD
        ========================== */

        .box{
            background:#fff;
            border-radius:15px;
            padding:30px;
            box-shadow:0 5px 15px rgba(0,0,0,.08);
        }

        /* =========================
              HEADER
        ========================== */

        .page-title{
            display:flex;
            justify-content:space-between;
            align-items:center;
            margin-bottom:25px;
        }

        .page-title h2{
            color:#0f172a;
            font-weight:700;
            margin-bottom:5px;
        }

        .page-title p{
            color:#64748b;
            margin:0;
        }

        /* =========================
              TOOLBAR
        ========================== */

        .toolbar{
            display:flex;
            justify-content:space-between;
            align-items:center;
            margin-bottom:20px;
            gap:15px;
        }

        .toolbar .left{
            display:flex;
            gap:10px;
        }

        .toolbar .right{
            display:flex;
            gap:10px;
        }

        .toolbar input{
            width:250px;
        }

        /* =========================
              TABLE
        ========================== */

        table{
            vertical-align:middle!important;
        }

        thead{
            background:#0d6efd;
            color:white;
        }

        thead th{
            text-align:center;
        }

        tbody td{
            vertical-align:middle;
        }

        tbody tr:hover{
            background:#f5f9ff;
            transition:.2s;
        }

        /* =========================
              BADGE
        ========================== */

        .badge{
            padding:8px 12px;
            font-size:13px;
        }

        /* =========================
              BUTTON
        ========================== */

        .btn-sm{
            margin-right:5px;
        }

        /* =========================
              PAGINATION
        ========================== */

        .pagination{
            justify-content:center;
            margin-top:25px;
        }

        /* =========================
              EMPTY TABLE
        ========================== */

        .empty-row{
            height:80px;
            text-align:center;
            color:#6c757d;
        }

    </style>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dark-theme.css">
</head>
<body>

<div class="wrapper">

    <!-- Sidebar -->
    <jsp:include page="menu.jsp"/>

    <!-- Content -->
    <div class="content">

        <div class="box">

            <!-- Header -->
            <div class="page-title">

                <div>

                    <h2>
                        <i class="bi bi-people-fill"></i>
                        Quản lý khách hàng
                    </h2>

                    <p>
                        Quản lý toàn bộ khách hàng trong hệ thống
                    </p>

                </div>

            </div>

            <!-- Toolbar -->
            <div class="toolbar">

                <div class="left">

                    <% if (canAdd) { %>
                    <a href="<%=request.getContextPath()%>/khachhang?action=add"
                       class="btn btn-primary">

                        <i class="bi bi-plus-circle-fill"></i>

                        Thêm khách hàng

                    </a>
                    <% } %>

                </div>

                <div class="right d-flex align-items-center gap-3">

                    <% if (canSoftDelete) { %>
                    <div class="form-check form-switch mb-0">
                        <input class="form-check-input" type="checkbox" id="showInactiveToggle"
                               <%=showInactive ? "checked" : ""%>
                               onchange="window.location.href='<%=request.getContextPath()%>/khachhang?showInactive=' + (this.checked ? '1' : '0')">
                        <label class="form-check-label" for="showInactiveToggle" style="white-space:nowrap;">
                            Hiện cả đã vô hiệu hóa
                        </label>
                    </div>
                    <% } %>

                    <form action="<%=request.getContextPath()%>/khachhang"
                          method="post"
                          class="d-flex">

                        <input type="hidden"
                               name="action"
                               value="search">

                        <input type="text"
                               name="keyword"
                               class="form-control me-2"
                               placeholder="Nhập tên hoặc SĐT">

                        <button class="btn btn-success">

                            <i class="bi bi-search"></i>

                        </button>

                    </form>

                </div>

            </div>

            <!-- Table -->

            <table class="table table-bordered table-hover align-middle">

                <thead>

                <tr>

                    <th width="90">Mã KH</th>

                    <th>Tên khách hàng</th>

                    <th width="150">Số điện thoại</th>

                    <th>Địa chỉ</th>

                    <th>Email</th>

                    <th width="120">Trạng thái</th>

                    <th width="170">Thao tác</th>

                </tr>

                </thead>

                <tbody>

                <%

                    if(list!=null && !list.isEmpty()){

                        for(Customer c:list){

                %>

                <tr>

                    <td>

                        KH<%=String.format("%03d",c.getCustomerId())%>

                    </td>

                    <td>

                        <%=c.getCustomerName()%>

                    </td>

                    <td>

                        <%=c.getPhone()%>

                    </td>

                    <td>

                        <%=c.getAddress()%>

                    </td>

                    <td>

                        <%=c.getEmail()%>

                    </td>

                    <td align="center">

                        <% if(c.isStatus()){ %>

                        <span class="badge bg-success">

                            Hoạt động

                        </span>

                        <% }else{ %>

                        <span class="badge bg-secondary">

                            Đã vô hiệu hóa

                        </span>

                        <% } %>

                    </td>

                    <td align="center">

                        <% if (canEdit) { %>
                        <a href="<%=request.getContextPath()%>/khachhang?action=edit&id=<%=c.getCustomerId()%>"
                           class="btn btn-warning btn-sm">

                            <i class="bi bi-pencil-square"></i>

                        </a>
                        <% } %>

                        <% String khBaseUrl = request.getContextPath() + "/khachhang"; %>
                        <my:deleteActions
                            baseUrl="<%=khBaseUrl%>"
                            entityId="<%=String.valueOf(c.getCustomerId())%>"
                            entityLabel="<%=c.getCustomerName()%>"
                            isActive="<%=c.isStatus()%>"
                            canSoftDelete="<%=canSoftDelete%>"
                            pendingRequest="<%=pendingIds.contains(c.getCustomerId())%>" />

                    </td>

                </tr>

                <%

                        }

                    }else{

                %>

                <tr>

                    <td colspan="7" class="empty-row">

                        <i class="bi bi-database-exclamation"></i>

                        Chưa có khách hàng nào trong hệ thống.

                    </td>

                </tr>

                <%

                    }

                %>

                </tbody>

            </table>

            <!-- Pagination -->

            <nav>

                <ul class="pagination">

                    <li class="page-item active">

                        <a class="page-link">

                            1

                        </a>

                    </li>

                </ul>

            </nav>

        </div>

    </div>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>

</html>