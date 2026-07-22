<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.Customer"%>

<%
ArrayList<Customer> list =
(ArrayList<Customer>)request.getAttribute("listCustomer");
%>

<!DOCTYPE html>

<html>

    <head>

        <meta charset="UTF-8">

        <title>Thêm hóa đơn</title>

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

                max-width:700px;
                margin:40px auto;
                border:none;
                border-radius:15px;
                box-shadow:0 5px 15px rgba(0,0,0,.08);

            }

            .card-header{

                background:#0d6efd;
                color:white;
                font-size:22px;
                font-weight:bold;
                text-align:center;
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

                <i class="bi bi-receipt-cutoff"></i>

                THÊM HÓA ĐƠN

            </div>

            <div class="card-body">

                <form action="<%=request.getContextPath()%>/hoadon"
                      method="post">

                    <input
                        type="hidden"
                        name="action"
                        value="insert">

                    <div class="mb-3">

                        <label class="form-label">

                            Khách hàng

                        </label>

                        <select
                            name="customerId"
                            class="form-select"
                            required>

                            <option value="">

                                -- Chọn khách hàng --

                            </option>

                            <%

                            if(list!=null){

                            for(Customer c:list){

                            %>

                            <option
                                value="<%=c.getCustomerId()%>">

                                <%=c.getCustomerName()%>

                            </option>

                            <%

                            }

                            }

                            %>

                        </select>

                    </div>

                            

                    <div class="mb-3">

                        <div class="alert alert-info mb-0">

                            <i class="bi bi-info-circle"></i>
                            Hóa đơn mới sẽ ở trạng thái
                            <strong>Chưa thanh toán</strong>.
                            Trạng thái sẽ tự động chuyển sang
                            <strong>Đã thanh toán</strong> khi khách
                            hàng thanh toán đủ qua phiếu thu.

                        </div>

                    </div>

                    <div class="text-center mt-4">

                        <button
                            class="btn btn-primary">

                            <i class="bi bi-floppy-fill"></i>

                            Lưu

                        </button>

                        <a
                            href="<%=request.getContextPath()%>/hoadon"
                            class="btn btn-secondary">

                            <i class="bi bi-arrow-left"></i>

                            Quay lại

                        </a>

                    </div>

                </form>

            </div>

        </div>

    </body>

</html>