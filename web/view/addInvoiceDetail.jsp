<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.InvoiceDetail"%>

<%
    int invoiceId = (Integer) request.getAttribute("invoiceId");

    ArrayList<InvoiceDetail> list =
            (ArrayList<InvoiceDetail>) request.getAttribute("listDetail");
%>

<!DOCTYPE html>
<html>

    <head>

        <meta charset="UTF-8">

        <title>Chi tiết hóa đơn</title>

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

            .container-box{

                width:1150px;

                margin:35px auto;

                background:#fff;

                border-radius:15px;

                padding:30px;

                box-shadow:0 5px 18px rgba(0,0,0,.08);

            }

            h2{

                font-weight:700;

                color:#0f172a;

                margin-bottom:30px;

            }

            .form-title{

                font-size:20px;

                font-weight:600;

                margin-bottom:20px;

                color:#0d6efd;

            }

            .table{

                margin-top:30px;

            }

            thead{

                background:#0d6efd;

                color:white;

            }

            thead th{

                text-align:center;

                vertical-align:middle;

            }

            tbody td{

                vertical-align:middle;

            }

            tbody tr:hover{

                background:#f5f9ff;

            }

            .btn{

                border-radius:8px;

            }

            .card-box{

                border:1px solid #dee2e6;

                border-radius:12px;

                padding:20px;

                background:#fafafa;

            }

        </style>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dark-theme.css">
</head>

    <body>

        <div class="container-box">

            <h2>

                <i class="bi bi-receipt-cutoff"></i>

                Chi tiết hóa đơn

            </h2>

            <div class="card-box">

                <div class="form-title">

                    <i class="bi bi-plus-circle-fill"></i>

                    Thêm mặt hàng

                </div>

                <form action="<%=request.getContextPath()%>/invoice-detail"
                      method="post">

                    <input
                        type="hidden"
                        name="action"
                        value="insert">

                    <input
                        type="hidden"
                        name="invoiceId"
                        value="<%=invoiceId%>">

                    <div class="row">

                        <div class="col-md-4">

                            <label class="form-label">

                                Tên hàng

                            </label>

                            <input
                                class="form-control"
                                name="itemName"
                                required>

                        </div>

                        <div class="col-md-2">

                            <label class="form-label">

                                Đơn vị tính

                            </label>

                            <input
                                class="form-control"
                                name="unit"
                                required>

                        </div>

                        <div class="col-md-2">

                            <label class="form-label">

                                Số lượng

                            </label>

                            <input
                                type="number"
                                min="1"
                                class="form-control"
                                name="quantity"
                                required>

                        </div>

                        <div class="col-md-2">

                            <label class="form-label">

                                Đơn giá

                            </label>

                            <input
                                type="number"
                                min="0"
                                step="0.01"
                                class="form-control"
                                name="unitPrice"
                                required>

                        </div>

                        <div class="col-md-2 d-grid">

                            <label class="form-label">

                                &nbsp;

                            </label>

                            <button
                                class="btn btn-primary">

                                <i class="bi bi-plus-circle"></i>

                                Thêm

                            </button>

                        </div>

                    </div>

                </form>

            </div>
            <!-- =========================
                DANH SÁCH CHI TIẾT
        ========================== -->

            <table class="table table-bordered table-hover">

                <thead>

                    <tr>

                        <th width="70">STT</th>

                        <th>Tên hàng</th>

                        <th width="130">ĐVT</th>

                        <th width="100">SL</th>

                        <th width="170">Đơn giá</th>

                        <th width="180">Thành tiền</th>

                        <th width="150">Thao tác</th>

                    </tr>

                </thead>

                <tbody>

                    <%

                        double tong = 0;

                        int stt = 1;

                        if(list != null && !list.isEmpty()){

                            for(InvoiceDetail d : list){

                                tong += d.getSubtotal();

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

                            <%=String.format("%,.0f",d.getUnitPrice())%>

                        </td>

                        <td align="right">

                            <strong>

                                <%=String.format("%,.0f",d.getSubtotal())%>

                            </strong>

                        </td>

                        <td align="center">

                            <a

                                href="<%=request.getContextPath()%>/invoice-detail?action=edit&detailId=<%=d.getDetailId()%>"

                                class="btn btn-warning btn-sm">

                                <i class="bi bi-pencil-square"></i>

                            </a>

                            <a

                                href="<%=request.getContextPath()%>/invoice-detail?action=delete&detailId=<%=d.getDetailId()%>&invoiceId=<%=invoiceId%>"

                                class="btn btn-danger btn-sm"

                                onclick="return confirm('Bạn chắc chắn muốn xóa dòng hàng này?')">

                                <i class="bi bi-trash3-fill"></i>

                            </a>

                        </td>

                    </tr>

                    <%

                            }

                        }else{

                    %>

                    <tr>

                        <td colspan="7"

                            class="text-center text-secondary"

                            style="height:80px;">

                            <i class="bi bi-database-exclamation"></i>

                            Chưa có mặt hàng nào.

                        </td>

                    </tr>

                    <%

                        }

                    %>

                </tbody>

            </table>
            <!-- =========================
            TỔNG TIỀN
    ========================== -->

            <div class="row mt-4">

                <div class="col-md-8">

                </div>

                <div class="col-md-4">

                    <div class="card border-primary">

                        <div class="card-body text-end">

                            <div class="text-secondary">

                                Tổng tiền hóa đơn

                            </div>

                            <h3 class="text-danger fw-bold mt-2">

                                <%=String.format("%,.0f", tong)%> VNĐ

                            </h3>

                        </div>

                    </div>

                </div>

            </div>

            <!-- =========================
                    BUTTON
            ========================== -->

            <div class="d-flex justify-content-between mt-4">

                <a
                    href="<%=request.getContextPath()%>/hoadon"
                    class="btn btn-secondary">

                    <i class="bi bi-arrow-left-circle"></i>

                    Quay lại danh sách

                </a>

                <a
                    href="<%=request.getContextPath()%>/hoadon"
                    class="btn btn-success">

                    <i class="bi bi-check-circle-fill"></i>

                    Hoàn tất hóa đơn

                </a>

            </div>

        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

    </body>

</html>