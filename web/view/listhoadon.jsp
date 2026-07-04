<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Danh sách hóa đơn</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">

    <style>
.sidebar{
    width:240px;
    background:#0f172a;
    min-height:100vh;
}

.logo{
    text-align:center;
    padding-top:40px;
    padding-bottom:25px;
    border-bottom:1px solid #334155;
}

.logo h4{
    color:#ffffff;       /* Chữ QUẢN LÍ TÀI CHÍNH màu trắng */
    margin:0;
    font-size:22px;
    font-weight:bold;
}

.sidebar a{
    display:block;
    color:#ffffff;       /* Các mục menu màu trắng */
    text-decoration:none;
    padding:15px 20px;
    font-size:16px;
}

.sidebar a:hover{
    background:#1e293b;
    color:#ffffff;       /* Hover vẫn giữ màu trắng */
}
            body{
                margin:0;
                background:#f4f6f9;
            }

            .wrapper{
                display:flex;
            }

            .content{
                flex:1;
                padding:30px;
            }

            .box{
                background:white;
                padding:20px;
                border-radius:12px;
                box-shadow:0 0 5px #ddd;
            }

            .top{
                display:flex;
                justify-content:space-between;
                margin-bottom:20px;
            }

            .green{
                color:green;
            }

            .orange{
                color:orange;
            }

            .pagination{
                justify-content:center;
            }

        </style>

</head>

<body>

<div class="container">

    <div class="card">

        <div class="card-header bg-primary text-white">

            <h3 class="text-center">
                DANH SÁCH HÓA ĐƠN
            </h3>

        </div>

        <div class="card-body">

            <!-- Thông báo -->
            <c:if test="${not empty sessionScope.message}">
                <div class="alert alert-success">
                    ${sessionScope.message}
                </div>

                <c:remove var="message" scope="session"/>
            </c:if>

            <!-- Thanh chức năng -->
            <div class="row mb-3">

                <div class="col-md-6">

                    <a href="${pageContext.request.contextPath}/hoadon?action=add"
                       class="btn btn-success">

                        + Thêm hóa đơn

                    </a>

                </div>

                <div class="col-md-6">

                    <form action="${pageContext.request.contextPath}/hoadon"
                          method="get">

                        <input type="hidden"
                               name="action"
                               value="search">

                        <div class="input-group">

                            <input
                                type="text"
                                name="keyword"
                                class="form-control"
                                placeholder="Nhập mã hóa đơn...">

                            <button class="btn btn-primary">

                                Tìm kiếm

                            </button>

                        </div>

                    </form>

                </div>

            </div>

            <!-- Bảng dữ liệu -->

            <table class="table table-bordered table-hover">

                <thead class="table-dark">

                <tr>

                    <th>Mã HĐ</th>

                    <th>Mã KH</th>

                    <th>Mã User</th>

                    <th>Ngày lập</th>

                    <th>Tổng tiền</th>

                    <th>Trạng thái</th>

                    <th>Chi tiết</th>

                </tr>

                </thead>

                <tbody>

                <c:forEach items="${list}" var="hd">

                    <tr>

                        <td>${hd.invoiceId}</td>

                        <td>${hd.customerId}</td>

                        <td>${hd.userId}</td>

                        <td>${hd.invoiceDate}</td>

                        <td>${hd.totalAmount}</td>

                        <td>${hd.status}</td>

                        <td>

                            <a href="${pageContext.request.contextPath}/chitiethoadon?id=${hd.invoiceId}"
                               class="btn btn-info btn-sm">

                                Xem

                            </a>

                        </td>

                    </tr>

                </c:forEach>

                </tbody>

            </table>

        </div>

    </div>

</div>

</body>
</html>