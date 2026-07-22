<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.Customer"%>
<%@page import="model.HoaDon"%>

<%
HoaDon hd=(HoaDon)request.getAttribute("hoaDon");

ArrayList<Customer> list=
(ArrayList<Customer>)request.getAttribute("listCustomer");
%>

<!DOCTYPE html>

<html>

    <head>

        <meta charset="UTF-8">

        <title>Sửa hóa đơn</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
              rel="stylesheet">

        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
              rel="stylesheet">

        <style>

            body{

                background:#edf2f7;
                font-family:Segoe UI;

            }

            .card{

                max-width:720px;
                margin:40px auto;
                border:none;
                border-radius:15px;
                box-shadow:0 5px 15px rgba(0,0,0,.08);

            }

            .card-header{

                background:#ffc107;
                color:#000;
                text-align:center;
                font-size:22px;
                font-weight:bold;
                padding:18px;

            }

            .card-body{

                padding:35px;

            }

            .btn{

                min-width:120px;

            }

        </style>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dark-theme.css">
</head>

    <body>

        <div class="card">

            <div class="card-header">

                <i class="bi bi-pencil-square"></i>

                SỬA HÓA ĐƠN

            </div>

            <div class="card-body">

                <form
                    action="<%=request.getContextPath()%>/hoadon"
                    method="post">

                    <input
                        type="hidden"
                        name="action"
                        value="update">

                    <input
                        type="hidden"
                        name="invoiceId"
                        value="<%=hd.getInvoiceId()%>">

                    <div class="mb-3">

                        <label class="form-label">

                            Khách hàng

                        </label>

                        <select
                            name="customerId"
                            class="form-select"
                            required>

                            <%

                            for(Customer c:list){

                            %>

                            <option
                                value="<%=c.getCustomerId()%>"

                                <%=c.getCustomerId()==hd.getCustomerId()?
"selected":""%>>

                                <%=c.getCustomerName()%>

                            </option>

                            <%

                            }

                            %>

                        </select>

                    </div>

                    <div class="mb-3">

                        <label class="form-label">

                            Tổng tiền

                        </label>

                        <input
                            type="text"
                            class="form-control"
                            value="<%=hd.getMoney()%> đ"
                            disabled>

                        <div class="form-text">

                            Tổng tiền được tính tự động từ chi tiết
                            hóa đơn, không thể sửa trực tiếp ở đây.

                        </div>

                    </div>

                    <div class="mb-3">

                        <label class="form-label">

                            Trạng thái

                        </label>

                        <input
                            type="text"
                            class="form-control"
                            value="<%=hd.getStatus()%>"
                            disabled>

                        <div class="form-text">

                            Trạng thái được tự động cập nhật dựa
                            trên số tiền đã thu (phiếu thu), không
                            thể sửa tay để tránh mâu thuẫn với công
                            nợ.

                        </div>

                    </div>

                    <div class="row mt-4">

                        <div class="col text-center">

                            <button
                                class="btn btn-warning">

                                <i class="bi bi-floppy-fill"></i>

                                Cập nhật

                            </button>

                            <a
                                href="<%=request.getContextPath()%>/hoadon"
                                class="btn btn-secondary">

                                <i class="bi bi-arrow-left"></i>

                                Quay lại

                            </a>

                        </div>

                    </div>

                </form>

            </div>

        </div>

    </body>

</html>