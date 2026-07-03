<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>

    <head>

        <meta charset="UTF-8">

        <title>Chi tiết hóa đơn</title>

        <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
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

        <div class="wrapper">

            <jsp:include page="menu.jsp"/>

            <div class="content">

                <div class="box">

                    <div class="top">

                        <h5>
                            Hóa đơn:
                            HD0002 - Công ty B
                        </h5>

                        <a
                            href="listhoadon.jsp"
                            class="btn btn-secondary">

                            Quay lại

                        </a>

                    </div>

                    <hr>

                    <div class="row mb-4">

                        <div class="col-md-3">
                            Ngày lập:
                            20/05/2024
                        </div>

                        <div class="col-md-3">
                            Nhân viên:
                            Admin
                        </div>

                        <div class="col-md-3">
                            Trạng thái:
                            Chưa thanh toán
                        </div>

                        <div class="col-md-3">

                            <b class="red">

                                Tổng tiền:
                                8,500,000

                            </b>

                        </div>

                    </div>

                    <table class="table table-bordered">

                        <tr>

                            <th>STT</th>
                            <th>Sản phẩm/Dịch vụ</th>
                            <th>ĐVT</th>
                            <th>Số lượng</th>
                            <th>Đơn giá</th>
                            <th>Thành tiền</th>

                        </tr>

                        <tr>

                            <td>1</td>
                            <td>Sản phẩm A</td>
                            <td>Cái</td>
                            <td>2</td>
                            <td>2,000,000</td>
                            <td>4,000,000</td>

                        </tr>

                        <tr>

                            <td>2</td>
                            <td>Dịch vụ B</td>
                            <td>Gói</td>
                            <td>1</td>
                            <td>4,500,000</td>
                            <td>4,500,000</td>

                        </tr>

                        <tr>

                            <td colspan="5" align="right">

                                <b>Tổng cộng:</b>

                            </td>

                            <td>

                                <b>
                                    8,500,000
                                </b>

                            </td>

                        </tr>

                    </table>

                </div>

            </div>

        </div>

    </body>

</html>