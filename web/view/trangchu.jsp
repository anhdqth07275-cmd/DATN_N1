<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="model.DangKy"%>

<%
DangKy user = (DangKy) session.getAttribute("user");

if(user == null){
    response.sendRedirect(request.getContextPath() + "/dangnhap");
    return;
}
%>
<!DOCTYPE html>
<html>

    <head>

        <meta charset="UTF-8">

        <title>Trang chủ</title>

        <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
            rel="stylesheet">

        <script
            src="https://cdn.jsdelivr.net/npm/chart.js">
        </script>

        <style>

            body{
                margin:0;
                background:#f4f6f9;
                font-family:Segoe UI;
            }

            .wrapper{
                display:flex;
            }

            .sidebar{
                width:240px;
                background:#0f172a;
                min-height:100vh;
            }

            .logo{
                color:white;
                text-align:center;
                padding:20px;
                border-bottom:1px solid #334155;
            }

            .sidebar a{
                display:block;
                color:white;
                text-decoration:none;
                padding:15px;
            }

            .sidebar a:hover{
                background:#1e293b;
            }

            .content{
                flex:1;
            }

            .topbar{
                background:white;
                padding:15px;
                display:flex;
                justify-content:flex-end;
                box-shadow:0 0 5px #ccc;
            }

            .main{
                padding:30px;
            }

            .card-stat{
                background:white;
                border-radius:12px;
                padding:20px;
                box-shadow:0 0 5px #ddd;
            }

            .card-stat h6{
                color:gray;
            }

            .card-stat h3{
                font-weight:bold;
            }

            .box{
                background:white;
                border-radius:12px;
                padding:20px;
                box-shadow:0 0 5px #ddd;
                margin-top:25px;
            }

            .table td,
            .table th{
                font-size:14px;
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
            .user-box{
                font-size:15px;
                font-weight:600;
                color:#333;
            }

            .user-box strong{
                color:#0d6efd;
            }
        </style>

    </head>

    <body>

        <div class="wrapper">

            <jsp:include page="menu.jsp"/>

            <div class="content">

                <div class="topbar">

                    <div>
                        🔔
                    </div>

                    <div class="user-box">

                        👤 <strong><%= user.getFullName() %></strong>

                        <br>

                        <small>
                            <%= user.getRoleName() %>
                        </small>

                    </div>

                </div>

                <div class="main">

                    <div class="row">

                        <div class="col-md-3">

                            <div class="card-stat">

                                <h6>Tổng doanh thu</h6>

                                <h3>550,000,000</h3>

                            </div>

                        </div>

                        <div class="col-md-3">

                            <div class="card-stat">

                                <h6>Tổng thu</h6>

                                <h3>420,000,000</h3>

                            </div>

                        </div>

                        <div class="col-md-3">

                            <div class="card-stat">

                                <h6>Tổng chi</h6>

                                <h3>130,000,000</h3>

                            </div>

                        </div>

                        <div class="col-md-3">

                            <div class="card-stat">

                                <h6>Công nợ phải thu</h6>

                                <h3 class="text-danger">

                                    230,000,000

                                </h3>

                            </div>

                        </div>

                    </div>

                    <div class="row">

                        <div class="col-md-6">

                            <div class="box">

                                <h5>DOANH THU THEO THÁNG</h5>

                                <canvas id="chart1"></canvas>

                            </div>

                        </div>

                        <div class="col-md-6">

                            <div class="box">

                                <h5>CÔNG NỢ THEO TRẠNG THÁI</h5>

                                <canvas id="chart2"></canvas>

                            </div>

                        </div>

                    </div>

                    <div class="row">

                        <div class="col-md-6">

                            <div class="box">

                                <h5>HÓA ĐƠN MỚI NHẤT</h5>

                                <table class="table">

                                    <tr>

                                        <th>Mã HĐ</th>
                                        <th>Khách hàng</th>
                                        <th>Ngày lập</th>
                                        <th>Tổng tiền</th>
                                        <th>Trạng thái</th>

                                    </tr>

                                    <tr>

                                        <td>HD0001</td>
                                        <td>Công ty A</td>
                                        <td>20/05/2024</td>
                                        <td>15,000,000</td>
                                        <td class="green">
                                            Đã thanh toán
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

                                    </tr>

                                    <tr>

                                        <td>HD0003</td>
                                        <td>Khách lẻ</td>
                                        <td>18/05/2024</td>
                                        <td>2,300,000</td>
                                        <td class="green">
                                            Đã thanh toán
                                        </td>

                                    </tr>

                                </table>

                            </div>

                        </div>

                        <div class="col-md-6">

                            <div class="box">

                                <h5>CÔNG NỢ QUÁ HẠN</h5>

                                <table class="table">

                                    <tr>

                                        <th>Khách hàng</th>
                                        <th>Số tiền</th>
                                        <th>Ngày quá hạn</th>

                                    </tr>

                                    <tr>

                                        <td>Công ty B</td>
                                        <td>25,000,000</td>
                                        <td>10/05/2024</td>

                                    </tr>

                                    <tr>

                                        <td>Công ty C</td>
                                        <td>15,000,000</td>
                                        <td>05/05/2024</td>

                                    </tr>

                                    <tr>

                                        <td>Công ty D</td>
                                        <td>10,000,000</td>
                                        <td>01/05/2024</td>

                                    </tr>

                                </table>

                            </div>

                        </div>

                    </div>

                </div>

            </div>

        </div>

        <script>

            new Chart(
                    document.getElementById('chart1'),
                    {
                        type: 'line',

                        data: {
                            labels: [
                                'T1', 'T2', 'T3', 'T4',
                                'T5', 'T6', 'T7', 'T8',
                                'T9', 'T10', 'T11', 'T12'
                            ],

                            datasets: [{
                                    data: [
                                        50, 100, 140, 130,
                                        90, 120, 100, 150,
                                        150, 150, 100, 150
                                    ]
                                }]
                        }
                    });

            new Chart(
                    document.getElementById('chart2'),
                    {
                        type: 'doughnut',

                        data: {
                            labels: [
                                'Chưa đến hạn',
                                'Đến hạn',
                                'Quá hạn'
                            ],

                            datasets: [{
                                    data: [
                                        120,
                                        60,
                                        50
                                    ]
                                }]
                        }
                    });

        </script>

    </body>

</html>