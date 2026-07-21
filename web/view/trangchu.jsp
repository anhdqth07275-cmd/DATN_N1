<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="model.DangKy"%>
<%@page import="model.Dashboard"%>
<%@page import="model.HoaDon"%>
<%@page import="model.Debt"%>
<%@page import="java.util.ArrayList"%>

<%
DangKy user = (DangKy) session.getAttribute("user");
Dashboard dashboard =
        (Dashboard) request.getAttribute("dashboard");

ArrayList<HoaDon> listInvoice =
        (ArrayList<HoaDon>) request.getAttribute("listInvoice");

ArrayList<Debt> listDebt =
        (ArrayList<Debt>) request.getAttribute("listDebt");
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

                <%
                    String pageError = request.getParameter("error");
                    if (pageError != null) {
                %>
                <div class="alert alert-danger"><%=pageError%></div>
                <%
                    }
                %>

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

                                <h6>💰 Tổng doanh thu</h6>

                                <h3 class="text-primary">

                                    <%=String.format("%,.0f",
                            dashboard.getTotalRevenue())%>

                                    VNĐ

                                </h3>

                            </div>

                        </div>

                        <div class="col-md-3">

                            <div class="card-stat">

                                <h6>💵 Tổng thu</h6>

                                <h3 class="text-success">

                                    <%=String.format("%,.0f",
                            dashboard.getTotalReceiptAmount())%>

                                    VNĐ

                                </h3>

                            </div>

                        </div>

                        <div class="col-md-3">

                            <div class="card-stat">

                                <h6>💸 Tổng chi</h6>

                                <h3 class="text-danger">

                                    <%=String.format("%,.0f",
                            dashboard.getTotalExpenseAmount())%>

                                    VNĐ

                                </h3>

                            </div>

                        </div>

                        <div class="col-md-3">

                            <div class="card-stat">

                                <h6>📋 Công nợ</h6>

                                <h3 class="text-warning">

                                    <%=String.format("%,.0f",
                            dashboard.getTotalDebt())%>

                                    VNĐ

                                </h3>

                            </div>

                        </div>

                    </div>

                    <div class="row">

                        <div class="col-md-6">

                            <div class="box">

                                <h5>📈 DOANH THU THEO THÁNG</h5>

                                <canvas id="chart1"></canvas>

                            </div>

                        </div>

                        <div class="col-md-6">

                            <div class="box">

                                <h5>📊 CÔNG NỢ THEO TRẠNG THÁI</h5>

                                <canvas id="chart2"></canvas>

                            </div>

                        </div>

                    </div>

                    <div class="row">

                        <div class="col-md-6">

                            <div class="box">

                                <h5>🧾 HÓA ĐƠN MỚI NHẤT</h5>

                                <table class="table table-hover">

                                    <thead>

                                        <tr>

                                            <th>Mã HĐ</th>

                                            <th>Khách hàng</th>

                                            <th>Ngày lập</th>

                                            <th>Tổng tiền</th>

                                            <th>Trạng thái</th>

                                        </tr>

                                    </thead>

                                    <tbody>

                                        <%

                                        if(listInvoice!=null){

                                            for(HoaDon hd:listInvoice){

                                        %>

                                        <tr>

                                            <td><%=hd.getInvoiceCode()%></td>

                                            <td><%=hd.getCustomerName()%></td>

                                            <td><%=hd.getDateVN()%></td>

                                            <td><%=hd.getMoney()%> VNĐ</td>

                                            <td>

                                                <%

                                                if("Đã thanh toán".equalsIgnoreCase(hd.getStatus())){

                                                %>

                                                <span class="badge bg-success">

                                                    Đã thanh toán

                                                </span>

                                                <%

                                                }else{

                                                %>

                                                <span class="badge bg-warning text-dark">

                                                    Còn nợ

                                                </span>

                                                <%

                                                }

                                                %>

                                            </td>

                                        </tr>

                                        <%

                                            }

                                        }

                                        %>

                                    </tbody>

                                </table>

                            </div>

                        </div>

                        <div class="col-md-6">

                            <div class="box">

                                <h5>📋 CÔNG NỢ GẦN NHẤT</h5>

                                <table class="table table-hover">

                                    <thead>

                                        <tr>

                                            <th>Mã CN</th>

                                            <th>Khách hàng</th>

                                            <th>Số tiền</th>

                                            <th>Hạn TT</th>

                                        </tr>

                                    </thead>

                                    <tbody>

                                        <%

                                        if(listDebt!=null){

                                            for(Debt d:listDebt){

                                        %>

                                        <tr>

                                            <td><%=d.getDebtCode()%></td>

                                            <td><%=d.getCustomerName()%></td>

                                            <td class="text-danger">

                                                <%=d.getMoney()%> VNĐ

                                            </td>

                                            <td>

                                                <%=d.getDateVN()%>

                                            </td>

                                        </tr>

                                        <%

                                            }

                                        }

                                        %>

                                    </tbody>

                                </table>

                            </div>

                        </div>

                    </div>

                </div>

            </div>

        </div>

        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

        <script>

                        new Chart(document.getElementById("chart1"), {

                            type: "line",

                            data: {

                                labels: ["T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12"],

                                datasets: [{

                                        label: "Doanh thu",

                                        data: [15, 20, 25, 18, 40, 35, 42, 50, 46, 58, 65, 70],

                                        borderWidth: 3,

                                        fill: false,

                                        tension: .35

                                    }]

                            },

                            options: {

                                responsive: true

                            }

                        });

                        new Chart(document.getElementById("chart2"), {

                            type: "doughnut",

                            data: {

                                labels: ["Đã thanh toán", "Còn nợ"],

                                datasets: [{

                                        data: [75, 25]

                                    }]

                            },

                            options: {

                                responsive: true

                            }

                        });

        </script>

    </body>

</html>