<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.InvoiceDetail"%>

<%
    InvoiceDetail d = (InvoiceDetail) request.getAttribute("detail");
%>

<!DOCTYPE html>

<html>

    <head>

        <meta charset="UTF-8">

        <title>Sửa chi tiết hóa đơn</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
              rel="stylesheet">

        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
              rel="stylesheet">

        <style>

            body{

                background:#eef2f7;

                font-family:Segoe UI;

            }

            .box{

                width:700px;

                margin:40px auto;

                background:white;

                border-radius:15px;

                padding:30px;

                box-shadow:0 5px 15px rgba(0,0,0,.08);

            }

            h2{

                text-align:center;

                margin-bottom:30px;

                font-weight:bold;

            }

        </style>

    </head>

    <body>

        <div class="box">

            <h2>

                <i class="bi bi-pencil-square"></i>

                SỬA CHI TIẾT HÓA ĐƠN

            </h2>

            <form

                action="<%=request.getContextPath()%>/invoice-detail"

                method="post">

                <input
                    type="hidden"
                    name="action"
                    value="update">

                <input
                    type="hidden"
                    name="detailId"
                    value="<%=d.getDetailId()%>">

                <input
                    type="hidden"
                    name="invoiceId"
                    value="<%=d.getInvoiceId()%>">

                <div class="mb-3">

                    <label>

                        Tên hàng

                    </label>

                    <input

                        class="form-control"

                        name="itemName"

                        value="<%=d.getItemName()%>"

                        required>

                </div>

                <div class="mb-3">

                    <label>

                        Đơn vị tính

                    </label>

                    <input

                        class="form-control"

                        name="unit"

                        value="<%=d.getUnit()%>"

                        required>

                </div>

                <div class="mb-3">

                    <label>

                        Số lượng

                    </label>

                    <input

                        type="number"

                        min="1"

                        class="form-control"

                        name="quantity"

                        value="<%=d.getQuantity()%>"

                        required>

                </div>

                <div class="mb-3">

                    <label>

                        Đơn giá

                    </label>

                    <input

                        type="number"

                        step="0.01"

                        min="0"

                        class="form-control"

                        name="unitPrice"

                        value="<%=d.getUnitPrice()%>"

                        required>

                </div>

                <div class="d-flex justify-content-between">

                    <button

                        class="btn btn-primary">

                        <i class="bi bi-floppy-fill"></i>

                        Cập nhật

                    </button>

                    <a

                        href="<%=request.getContextPath()%>/invoice-detail?action=add&id=<%=d.getInvoiceId()%>"

                        class="btn btn-secondary">

                        Quay lại

                    </a>

                </div>

            </form>

        </div>

    </body>

</html>