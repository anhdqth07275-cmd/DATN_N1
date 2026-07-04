<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>-Thêm hóa đơn-</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">

    <style>
        body{
            background:#f5f5f5;
        }

        .container{
            margin-top:40px;
            width:700px;
        }

        .card{
            box-shadow:0 0 10px gray;
        }
    </style>

</head>

<body>

<div class="container">

    <div class="card">

        <div class="card-header bg-success text-white">

            <h3 class="text-center">
                THÊM HÓA ĐƠN
            </h3>

        </div>

        <div class="card-body">

            <% if(request.getAttribute("error") != null){ %>

                <div class="alert alert-danger">

                    <%=request.getAttribute("error")%>

                </div>

            <% } %>

            <form action="${pageContext.request.contextPath}/hoadon"
                  method="post">

                <div class="mb-3">

                    <label class="form-label">
                        Mã khách hàng
                    </label>

                    <input
                        type="number"
                        name="customerId"
                        class="form-control"
                        required>

                </div>

                <div class="mb-3">

                    <label class="form-label">
                        Mã nhân viên
                    </label>

                    <input
                        type="number"
                        name="userId"
                        class="form-control"
                        required>

                </div>

                <div class="mb-3">

                    <label class="form-label">
                        Tổng tiền
                    </label>

                    <input
                        type="number"
                        step="0.01"
                        min="0"
                        name="totalAmount"
                        class="form-control"
                        required>

                </div>

                <div class="mb-3">

                    <label class="form-label">
                        Trạng thái
                    </label>

                    <select
                        name="status"
                        class="form-select">

                        <option value="Đã thanh toán">

                            Đã thanh toán

                        </option>

                        <option value="Chưa thanh toán">

                            Chưa thanh toán

                        </option>

                        <option value="Một phần">

                            Một phần

                        </option>

                    </select>

                </div>

                <div class="text-center">

                    <button
                        type="submit"
                        class="btn btn-success">

                        Lưu hóa đơn

                    </button>

                    <a href="${pageContext.request.contextPath}/hoadon"
                       class="btn btn-secondary">

                        Quay lại

                    </a>

                </div>

            </form>

        </div>

    </div>

</div>

</body>
</html>