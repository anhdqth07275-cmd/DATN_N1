<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.ExpenseVoucher"%>

<%
    ArrayList<ExpenseVoucher> list =
            (ArrayList<ExpenseVoucher>) request.getAttribute("listExpense");
%>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <title>Quản lý phiếu chi</title>

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

        .content{

            flex:1;

            padding:30px;

        }

        .box{

            background:#fff;

            border-radius:15px;

            padding:30px;

            box-shadow:0 5px 15px rgba(0,0,0,.08);

        }

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

        .toolbar{

            display:flex;

            justify-content:space-between;

            align-items:center;

            margin-bottom:20px;

        }

        table{

            vertical-align:middle!important;

        }

        thead{

            background:#dc3545;

            color:#fff;

        }

        thead th{

            text-align:center;

        }

        tbody tr:hover{

            background:#fff5f5;

            transition:.2s;

        }

        .pagination{

            justify-content:center;

            margin-top:25px;

        }

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

    <jsp:include page="menu.jsp"/>

    <div class="content">

        <div class="box">

            <div class="page-title">

                <div>

                    <h2>

                        <i class="bi bi-wallet2"></i>

                        Quản lý phiếu chi

                    </h2>

                    <p>

                        Theo dõi các khoản chi của doanh nghiệp

                    </p>

                </div>

                <a href="<%=request.getContextPath()%>/phieuchi?action=add"
                   class="btn btn-danger">

                    <i class="bi bi-plus-circle"></i>

                    Thêm phiếu chi

                </a>

            </div>
                   <!-- Toolbar -->

<div class="toolbar">

    <form action="<%=request.getContextPath()%>/phieuchi"
          method="post"
          class="d-flex">

        <input
                type="hidden"
                name="action"
                value="search">

        <input
                type="text"
                name="keyword"
                class="form-control me-2"
                placeholder="Nhập tên khoản chi hoặc người lập...">

        <button class="btn btn-success">

            <i class="bi bi-search"></i>

        </button>

    </form>

</div>


<!-- Table -->

<table class="table table-bordered table-hover align-middle">

    <thead>

    <tr>

        <th width="90">Mã PC</th>

        <th>Tên khoản chi</th>

        <th width="170">Số tiền</th>

        <th width="140">Ngày chi</th>

        <th width="180">Người lập</th>

        <th>Mô tả</th>

        <th width="150">Thao tác</th>

    </tr>

    </thead>

    <tbody>

    <%

        if(list!=null && !list.isEmpty()){

            for(ExpenseVoucher e : list){

    %>

    <tr>

        <td align="center">

            PC<%=String.format("%04d",e.getExpenseId())%>

        </td>

        <td>

            <%=e.getExpenseName()%>

        </td>

        <td align="right">

            <strong class="text-danger">

                <%=e.getMoney()%> VNĐ

            </strong>

        </td>

        <td align="center">

            <%=e.getDateVN()%>

        </td>

        <td>

            <%=e.getUserName()%>

        </td>

        <td>

            <%=e.getDescription()==null?"":e.getDescription()%>

        </td>

        <td align="center">

            <a
                href="<%=request.getContextPath()%>/phieuchi?action=edit&id=<%=e.getExpenseId()%>"
                class="btn btn-warning btn-sm">

                <i class="bi bi-pencil-square"></i>

            </a>

            <a
                href="<%=request.getContextPath()%>/phieuchi?action=delete&id=<%=e.getExpenseId()%>"
                class="btn btn-danger btn-sm"
                onclick="return confirm('Bạn có chắc muốn xóa phiếu chi này?');">

                <i class="bi bi-trash"></i>

            </a>

        </td>

    </tr>

    <%

            }

        }else{

    %>

    <tr>

        <td colspan="7" class="empty-row">

            <i class="bi bi-database-exclamation"></i>

            Chưa có dữ liệu phiếu chi.

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