<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<title>Công nợ</title>

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

.card-box{
    background:white;
    border-radius:12px;
    padding:20px;
    box-shadow:0 0 5px #ddd;
}

.box{
    background:white;
    border-radius:12px;
    padding:20px;
    box-shadow:0 0 5px #ddd;
    margin-top:25px;
}

.green{
    color:green;
}

.orange{
    color:orange;
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

        <h4 class="mb-4">
            CÔNG NỢ KHÁCH HÀNG
        </h4>

        <div class="row">

            <div class="col-md-3">

                <div class="card-box">

                    <h6>Tổng phải thu</h6>

                    <h4>230,000,000</h4>

                </div>

            </div>

            <div class="col-md-3">

                <div class="card-box">

                    <h6 class="green">
                        Chưa đến hạn
                    </h6>

                    <h4>120,000,000</h4>

                </div>

            </div>

            <div class="col-md-3">

                <div class="card-box">

                    <h6 class="orange">
                        Đến hạn
                    </h6>

                    <h4>60,000,000</h4>

                </div>

            </div>

            <div class="col-md-3">

                <div class="card-box">

                    <h6 class="red">
                        Quá hạn
                    </h6>

                    <h4>50,000,000</h4>

                </div>

            </div>

        </div>

        <div class="box">

            <table class="table table-bordered table-hover">

                <tr>

                    <th>Khách hàng</th>
                    <th>Tổng nợ</th>
                    <th>Đã thanh toán</th>
                    <th>Còn lại</th>
                    <th>Hạn thanh toán</th>
                    <th>Trạng thái</th>

                </tr>

                <tr>

                    <td>Công ty A</td>
                    <td>25,000,000</td>
                    <td>0</td>
                    <td>25,000,000</td>
                    <td>10/06/2024</td>
                    <td class="green">
                        Chưa đến hạn
                    </td>

                </tr>

                <tr>

                    <td>Công ty B</td>
                    <td>30,000,000</td>
                    <td>5,000,000</td>
                    <td>25,000,000</td>
                    <td>10/05/2024</td>
                    <td class="red">
                        Quá hạn
                    </td>

                </tr>

                <tr>

                    <td>Công ty C</td>
                    <td>15,000,000</td>
                    <td>0</td>
                    <td>15,000,000</td>
                    <td>05/05/2024</td>
                    <td class="red">
                        Quá hạn
                    </td>

                </tr>

                <tr>

                    <td>Công ty D</td>
                    <td>10,000,000</td>
                    <td>2,000,000</td>
                    <td>8,000,000</td>
                    <td>20/05/2024</td>
                    <td class="orange">
                        Đến hạn
                    </td>

                </tr>

            </table>

        </div>

    </div>

</div>

</body>
</html>