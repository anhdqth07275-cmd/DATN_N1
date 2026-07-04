<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chi tiết hóa đơn</title>

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

.logo h3{
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
                border-radius:12px;
                padding:20px;
                box-shadow:0 0 5px #ddd;
            }

            .top{
                display:flex;
                justify-content:space-between;
            }

            .red{
                color:red;
            }

        </style>

</head>

<body>

<div class="container">

    <div class="card">

        <div class="card-header bg-primary text-white">

            <h3 class="text-center">

                CHI TIẾT HÓA ĐƠN

            </h3>

        </div>

        <div class="card-body">

            <table class="table table-bordered">

                <tr>
                    <th width="200">Mã hóa đơn</th>
                    <td>${invoice.invoiceId}</td>
                </tr>

                <tr>
                    <th>Mã khách hàng</th>
                    <td>${invoice.customerId}</td>
                </tr>

                <tr>
                    <th>Mã nhân viên</th>
                    <td>${invoice.userId}</td>
                </tr>

                <tr>
                    <th>Ngày lập</th>
                    <td>${invoice.invoiceDate}</td>
                </tr>

                <tr>
                    <th>Tổng tiền</th>
                    <td>${invoice.totalAmount}</td>
                </tr>

                <tr>
                    <th>Trạng thái</th>
                    <td>${invoice.status}</td>
                </tr>

            </table>

            <h4 class="mt-4 mb-3">
                Danh sách sản phẩm
            </h4>

            <table class="table table-bordered table-hover">

                <thead class="table-dark">

                    <tr>

                        <th>Mã SP</th>
                        <th>Số lượng</th>
                        <th>Đơn giá</th>
                        <th>Thành tiền</th>

                    </tr>

                </thead>

                <tbody>

                    <c:forEach items="${listDetail}" var="d">

                        <tr>

                            <td>${d.productId}</td>

                            <td>${d.quantity}</td>

                            <td>${d.unitPrice}</td>

                            <td>${d.subtotal}</td>

                        </tr>

                    </c:forEach>

                </tbody>

            </table>

            <div class="text-center">

                <a href="${pageContext.request.contextPath}/hoadon"
                   class="btn btn-secondary">

                    Quay lại

                </a>

            </div>

        </div>

    </div>

</div>

</body>
</html>