<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.HoaDon"%>
<%@page import="model.InvoiceDetail"%>
<%@page import="java.util.ArrayList"%>

<%
    HoaDon hd = (HoaDon) request.getAttribute("hoaDon");

    ArrayList<InvoiceDetail> list =
            (ArrayList<InvoiceDetail>) request.getAttribute("listDetail");
%>

<!DOCTYPE html>

<html>

    <head>

        <meta charset="UTF-8">

        <title>Chi tiết hóa đơn</title>

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

                width:1100px;

                margin:35px auto;

            }

            .box{

                background:white;

                border-radius:15px;

                padding:30px;

                box-shadow:0 5px 18px rgba(0,0,0,.08);

            }

            .title{

                display:flex;

                justify-content:space-between;

                align-items:center;

                margin-bottom:30px;

            }

            .title h2{

                font-weight:700;

                color:#0f172a;

            }

            .info{

                margin-bottom:25px;

            }

            .info table{

                width:100%;

            }

            .info td{

                padding:10px 5px;

            }

            .table thead{

                background:#0d6efd;

                color:white;

            }

            .table th{

                text-align:center;

            }

            .table td{

                vertical-align:middle;

            }

            .table tbody tr:hover{

                background:#f5f9ff;

            }

            .total{

                text-align:right;

                font-size:28px;

                color:#dc3545;

                font-weight:bold;

                margin-top:25px;

            }

        </style>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dark-theme.css">
</head>

    <body>

        <div class="wrapper">

            <div class="box">

                <div class="title">

                    <div>

                        <h2>

                            <i class="bi bi-receipt-cutoff"></i>

                            CHI TIẾT HÓA ĐƠN

                        </h2>

                    </div>

                    <div>

                        <span class="badge bg-primary fs-6">

                            <%=hd.getInvoiceCode()%>

                        </span>

                    </div>

                </div>

                <div class="info">

                    <table class="table table-borderless">

                        <tr>

                            <td width="180">

                                <b>Khách hàng</b>

                            </td>

                            <td>

                                <%=hd.getCustomerName()%>

                            </td>

                            <td width="180">

                                <b>Người lập</b>

                            </td>

                            <td>

                                <%=hd.getUserName()%>

                            </td>

                        </tr>

                        <tr>

                            <td>

                                <b>Ngày lập</b>

                            </td>

                            <td>

                                <%=hd.getDateVN()%>

                            </td>

                            <td>

                                <b>Trạng thái</b>

                            </td>

                            <td>

                                <%=hd.getStatus()%>

                            </td>

                        </tr>

                    </table>

                </div>
                <!-- =========================
                        DANH SÁCH HÀNG HÓA
                ========================= -->

                <table class="table table-bordered table-hover">

                    <thead>

                        <tr>

                            <th width="70">

                                STT

                            </th>

                            <th>

                                Tên hàng

                            </th>

                            <th width="120">

                                ĐVT

                            </th>

                            <th width="100">

                                SL

                            </th>

                            <th width="170">

                                Đơn giá

                            </th>

                            <th width="180">

                                Thành tiền

                            </th>

                        </tr>

                    </thead>

                    <tbody>

                        <%

                            int stt = 1;

                            if(list != null && !list.isEmpty()){

                                for(InvoiceDetail d : list){

                        %>

                        <tr>

                            <td align="center">

                                <%=stt++%>

                            </td>

                            <td>

                                <%=d.getItemName()%>

                            </td>

                            <td align="center">

                                <%=d.getUnit()%>

                            </td>

                            <td align="center">

                                <%=d.getQuantity()%>

                            </td>

                            <td align="right">

                                <%=String.format("%,.0f", d.getUnitPrice())%>

                            </td>

                            <td align="right">

                                <strong>

                                    <%=String.format("%,.0f", d.getSubtotal())%>

                                </strong>

                            </td>

                        </tr>

                        <%

                                }

                            }else{

                        %>

                        <tr>

                            <td colspan="6"

                                class="text-center text-secondary"

                                style="height:80px;">

                                <i class="bi bi-database-exclamation"></i>

                                Hóa đơn chưa có chi tiết.

                            </td>

                        </tr>

                        <%

                            }

                        %>

                    </tbody>

                </table>

                <!-- =========================
                        TỔNG TIỀN
                ========================= -->

                <div class="total">

                    Tổng cộng:

                    <span class="text-danger">

                        <%=hd.getMoney()%> VNĐ

                    </span>

                </div>
                <!-- =========================
                BUTTON
        ========================= -->

                <div class="d-flex justify-content-between mt-4">

                    <a href="<%=request.getContextPath()%>/hoadon"
                       class="btn btn-secondary">

                        <i class="bi bi-arrow-left-circle"></i>

                        Quay lại danh sách

                    </a>

                    <div>

                        <button
                            class="btn btn-success me-2"
                            onclick="window.print()">

                            <i class="bi bi-printer-fill"></i>

                            In hóa đơn

                        </button>

                        <a href="<%=request.getContextPath()%>/invoice-detail?action=add&id=<%=hd.getInvoiceId()%>"
                           class="btn btn-warning">

                            <i class="bi bi-pencil-square"></i>

                            Chỉnh sửa chi tiết

                        </a>

                    </div>

                </div>

            </div>

        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

    </body>

</html>