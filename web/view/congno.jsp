<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.Debt"%>

<%
    ArrayList<Debt> list =
            (ArrayList<Debt>) request.getAttribute("listDebt");
%>

<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">

    <title>Quản lý công nợ</title>

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
            justify-content:flex-end;
            align-items:center;
            margin-bottom:20px;
        }

        .toolbar input{
            width:280px;
        }

        table{
            vertical-align:middle!important;
        }

        thead{
            background:#0d6efd;
            color:#fff;
        }

        thead th{
            text-align:center;
        }

        tbody tr:hover{
            background:#f8fbff;
            transition:.2s;
        }

        .badge{
            padding:8px 12px;
            font-size:13px;
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

</head>

<body>

<div class="wrapper">

    <!-- Sidebar -->
    <jsp:include page="menu.jsp"/>

    <div class="content">

        <div class="box">

            <!-- Header -->
            <div class="page-title">

                <div>

                    <h2>

                        <i class="bi bi-cash-coin"></i>

                        Quản lý công nợ

                    </h2>

                    <p>

                        Theo dõi công nợ của khách hàng

                    </p>

                </div>

            </div>

            <!-- Search -->

            <div class="toolbar">

                <form action="<%=request.getContextPath()%>/congno"
      method="get"
      class="d-flex">

                    <input
                            type="hidden"
                            name="action"
                            value="search">

                    <input
                            type="text"
                            name="keyword"
                            class="form-control me-2"
                            placeholder="Nhập tên khách hàng hoặc mã hóa đơn">

                    <button class="btn btn-success">

                        <i class="bi bi-search"></i>

                    </button>

                </form>

            </div>

            <!-- Table -->

            <table class="table table-bordered table-hover align-middle">

                <thead>

<tr>

    <th width="90">Mã CN</th>

    <th>Khách hàng</th>

    <th width="120">Mã HĐ</th>

    <th width="180">Còn nợ</th>

    <th width="150">Hạn thanh toán</th>

    <th width="150">Trạng thái</th>

    <th width="150">Thao tác</th>

</tr>

</thead>

                <tbody>

<%

if(list != null && !list.isEmpty()){

    for(Debt d : list){

%>

<tr>

    <td align="center">

        <%=d.getDebtCode()%>

    </td>

    <td>

        <%=d.getCustomerName()%>

    </td>

    <td align="center">

        <%=d.getInvoiceCode()%>

    </td>

    <td align="right">

        <strong class="text-danger">

            <%=d.getMoney()%> VNĐ

        </strong>

    </td>

    <td align="center">

        <%=d.getDateVN()%>

    </td>

    <td align="center">

<%

String status=d.getStatus();

if(status==null){

    status="";

}

if(status.equalsIgnoreCase("Đã thanh toán")){

%>

<span class="badge bg-success">

    Đã thanh toán

</span>

<%

}else if(status.equalsIgnoreCase("Quá hạn")){

%>

<span class="badge bg-danger">

    Quá hạn

</span>

<%

}else{

%>

<span class="badge bg-warning text-dark">

    Còn nợ

</span>

<%

}

%>

    </td>

    <td align="center">

        <a href="<%=request.getContextPath()%>/congno?action=extend&id=<%=d.getDebtId()%>"
           class="btn btn-warning btn-sm">

            <i class="bi bi-calendar-event"></i>

            Gia hạn

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

        Chưa có dữ liệu công nợ.

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