<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>

        <meta charset="UTF-8">
        <title>Quản lý khách hàng</title>

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
                font-family:Segoe UI;
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
                margin-bottom:20px;
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

                    <h4 class="text-center mb-4">
                        QUẢN LÝ KHÁCH HÀNG
                    </h4>

                    <div class="top">

                        <button class="btn btn-primary">
                            + Thêm khách hàng
                        </button>

                        <input
                            type="text"
                            class="form-control"
                            style="width:250px"
                            placeholder="Tìm kiếm...">

                    </div>

                    <table class="table table-bordered table-hover">

                        <tr>

                            <th>Mã KH</th>
                            <th>Tên khách hàng</th>
                            <th>SĐT</th>
                            <th>Địa chỉ</th>
                            <th>Công nợ</th>

                        </tr>

                        <tr>
                            <td>KH001</td>
                            <td>Công ty A</td>
                            <td>0901234567</td>
                            <td>Hà Nội</td>
                            <td>25,000,000</td>
                        </tr>

                        <tr>
                            <td>KH002</td>
                            <td>Công ty B</td>
                            <td>0912345678</td>
                            <td>Đà Nẵng</td>
                            <td>30,000,000</td>
                        </tr>

                        <tr>
                            <td>KH003</td>
                            <td>Công ty C</td>
                            <td>0934567890</td>
                            <td>Hồ Chí Minh</td>
                            <td>15,000,000</td>
                        </tr>

                        <tr>
                            <td>KH004</td>
                            <td>Khách lẻ</td>
                            <td>0987654321</td>
                            <td>Cần Thơ</td>
                            <td>0</td>
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