<%@page contentType="text/html" pageEncoding="UTF-8"%>

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

        <div class="wrapper">

            <jsp:include page="menu.jsp"/>

            <div class="content">

                <div class="box">

                    <h4 class="text-center">
                        DANH SÁCH HÓA ĐƠN
                    </h4>

                    <div class="top">

                        <button class="btn btn-primary">
                            + Thêm hóa đơn
                        </button>

                        <input
                            type="text"
                            class="form-control"
                            style="width:250px"
                            placeholder="Tìm kiếm...">

                    </div>

                    <table class="table table-bordered table-hover">

                        <tr>

                            <th>Mã HĐ</th>
                            <th>Khách hàng</th>
                            <th>Ngày lập</th>
                            <th>Tổng tiền</th>
                            <th>Trạng thái</th>
                            <th>Chi tiết</th>

                        </tr>

                        <tr>

                            <td>HD0001</td>
                            <td>Công ty A</td>
                            <td>20/05/2024</td>
                            <td>15,000,000</td>

                            <td class="green">
                                Đã thanh toán
                            </td>

                            <td>

                                <a
                                    href="chitiethoadon.jsp"
                                    class="btn btn-success btn-sm">

                                    Xem

                                </a>

                            </td>

                        </tr>

                        <tr>

                            <td>HD0002</td>
                            <td>Công ty B</td>
                            <td>19/05/2024</td>
                            <td>8,500,000</td>

                            <td class="orange">
                                Chưa thanh toán
                            </td>

                            <td>

                                <a
                                    href="chitiethoadon.jsp"
                                    class="btn btn-success btn-sm">

                                    Xem

                                </a>

                            </td>

                        </tr>

                        <tr>

                            <td>HD0003</td>
                            <td>Công ty C</td>
                            <td>18/05/2024</td>
                            <td>12,000,000</td>

                            <td>
                                Một phần
                            </td>

                            <td>

                                <a
                                    href="chitiethoadon.jsp"
                                    class="btn btn-success btn-sm">

                                    Xem

                                </a>

                            </td>

                        </tr>

                        <tr>

                            <td>HD0004</td>
                            <td>Khách lẻ</td>
                            <td>17/05/2024</td>
                            <td>2,300,000</td>

                            <td class="green">
                                Đã thanh toán
                            </td>

                            <td>

                                <a
                                    href="chitiethoadon.jsp"
                                    class="btn btn-success btn-sm">

                                    Xem

                                </a>

                            </td>

                        </tr>

                    </table>

                    <nav>

                        <ul class="pagination">

                            <li class="page-item active">
                                <a class="page-link">1</a>
                            </li>

                            <li class="page-item">
                                <a class="page-link">2</a>
                            </li>

                            <li class="page-item">
                                <a class="page-link">3</a>
                            </li>

                        </ul>

                    </nav>

                </div>

            </div>

        </div>

    </body>
</html>