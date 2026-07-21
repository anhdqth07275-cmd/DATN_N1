<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.HoaDon"%>

<%
ArrayList<HoaDon> list =
(ArrayList<HoaDon>)request.getAttribute("listHoaDon");
%>

<!DOCTYPE html>

<html>

    <head>

        <meta charset="UTF-8">

        <title>Quản lý hóa đơn</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
              rel="stylesheet">

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

                background:white;
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

                font-weight:700;
                color:#0f172a;

            }

            .page-title p{

                color:#64748b;
                margin-top:5px;

            }

            .toolbar{

                display:flex;
                justify-content:space-between;
                align-items:center;
                margin-bottom:20px;
                gap:20px;

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

                width:260px;

            }

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

            tbody tr:hover{

                background:#f7fbff;

            }

            .badge{

                font-size:13px;
                padding:8px 12px;

            }

            .btn-sm{

                margin-right:5px;

            }

            .pagination{

                justify-content:center;
                margin-top:25px;

            }

            .empty-row{

                height:90px;
                text-align:center;
                color:#6c757d;

            }

            .money{

                color:#dc3545;
                font-weight:bold;

            }

            .status-paid{

                background:#198754;
                color:white;

            }

            .status-unpaid{

                background:#ffc107;
                color:#000;

            }

        </style>

    </head>

    <body>

        <div class="wrapper">

            <jsp:include page="menu.jsp"/>

            <div class="content">

                <div class="box">

                    <div class="page-title">

                        <div>

                            <h2>

                                <i class="bi bi-receipt-cutoff"></i>

                                Quản lý hóa đơn

                            </h2>

                            <p>

                                Quản lý toàn bộ hóa đơn bán hàng

                            </p>

                        </div>

                    </div>

                    <%
                        String errorMsg = request.getParameter("error");
                        if (errorMsg != null && !errorMsg.isEmpty()) {
                    %>
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">

                        <i class="bi bi-exclamation-triangle-fill"></i>
                        <%=errorMsg%>

                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>

                    </div>
                    <%
                        }
                    %>

                    <div class="toolbar">

                        <div class="left">

                            <a
                                href="<%=request.getContextPath()%>/hoadon?action=add"
                                class="btn btn-primary">

                                <i class="bi bi-plus-circle-fill"></i>

                                Thêm hóa đơn

                            </a>

                        </div>

                        <div class="right">

                            <form
                                action="<%=request.getContextPath()%>/hoadon"
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
                                    placeholder="Tìm khách hàng...">

                                <button
                                    class="btn btn-success">

                                    <i class="bi bi-search"></i>

                                </button>

                            </form>

                        </div>

                    </div>

                    <table class="table table-bordered table-hover">

                        <thead>

                            <tr>

                                <th width="90">

                                    Mã HĐ

                                </th>

                                <th>

                                    Khách hàng

                                </th>

                                <th width="170">

                                    Người lập

                                </th>

                                <th width="140">

                                    Ngày lập

                                </th>

                                <th width="170">

                                    Tổng tiền

                                </th>

                                <th width="150">

                                    Trạng thái

                                </th>

                                <th width="180">

                                    Thao tác

                                </th>

                            </tr>

                        </thead>

                        <tbody>
                            <%

                        if(list!=null && !list.isEmpty()){

                            for(HoaDon hd:list){

                            %>

                            <tr>

                                <td align="center">

                                    HD<%=String.format("%04d",hd.getInvoiceId())%>

                                </td>

                                <td>

                                    <%=hd.getCustomerName()%>

                                </td>

                                <td>

                                    <%=hd.getUserName()%>

                                </td>

                                <td align="center">

                                    <%=hd.getInvoiceDate()%>

                                </td>

                                <td class="money" align="right">

                                    <%=String.format("%,.0f",hd.getTotalAmount())%> đ

                                </td>

                                <td align="center">

                                    <%

                                    if("Đã thanh toán".equals(hd.getStatus())){

                                    %>

                                    <span class="badge status-paid">

                                        <i class="bi bi-check-circle-fill"></i>

                                        Đã thanh toán

                                    </span>

                                    <%

                                    }else{

                                    %>

                                    <span class="badge status-unpaid">

                                        <i class="bi bi-clock-fill"></i>

                                        Chưa thanh toán

                                    </span>

                                    <%

                                    }

                                    %>

                                </td>

                                <td align="center">

                                    <a
                                        href="<%=request.getContextPath()%>/hoadon?action=view&id=<%=hd.getInvoiceId()%>"
                                        class="btn btn-info btn-sm"
                                        title="Chi tiết">

                                        <i class="bi bi-eye-fill"></i>

                                    </a>

                                    <a
                                        href="<%=request.getContextPath()%>/hoadon?action=edit&id=<%=hd.getInvoiceId()%>"
                                        class="btn btn-warning btn-sm"
                                        title="Sửa">

                                        <i class="bi bi-pencil-square"></i>

                                    </a>

                                    <a
                                        href="<%=request.getContextPath()%>/hoadon?action=delete&id=<%=hd.getInvoiceId()%>"
                                        class="btn btn-danger btn-sm"
                                        title="Xóa"
                                        onclick="return confirm('Bạn có chắc muốn xóa hóa đơn này?')">

                                        <i class="bi bi-trash-fill"></i>

                                    </a>

                                </td>

                            </tr>

                            <%

                                }

                            }else{

                            %>

                            <tr>

                                <td colspan="7" class="empty-row">

                                    <i class="bi bi-database-exclamation fs-3"></i>

                                    <br><br>

                                    Chưa có hóa đơn nào trong hệ thống.

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