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

ArrayList<Debt> listDueSoon =
        (ArrayList<Debt>) request.getAttribute("listDueSoon");

ArrayList<Debt> listOverdue =
        (ArrayList<Debt>) request.getAttribute("listOverdue");
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

        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dark-theme.css">
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

                                <div class="icon-badge">💰</div>

                                <h6>Doanh thu chưa thực hiện</h6>

                                <h3 class="text-primary">

                                    <%=String.format("%,.0f",
                            dashboard.getTotalRevenue())%>

                                    VNĐ

                                </h3>

                            </div>

                        </div>

                        <div class="col-md-3">

                            <div class="card-stat">

                                <div class="icon-badge">💵</div>

                                <h6>Tổng doanh thu</h6>

                                <h3 class="text-success">

                                    <%=String.format("%,.0f",
                            dashboard.getTotalReceiptAmount())%>

                                    VNĐ

                                </h3>

                            </div>

                        </div>

                        <div class="col-md-3">

                            <div class="card-stat">

                                <div class="icon-badge">💸</div>

                                <h6>Tổng chi</h6>

                                <h3 class="text-danger">

                                    <%=String.format("%,.0f",
                            dashboard.getTotalExpenseAmount())%>

                                    VNĐ

                                </h3>

                            </div>

                        </div>

                        <div class="col-md-3">

                            <div class="card-stat">

                                <div class="icon-badge">📋</div>

                                <h6>Công nợ</h6>

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

                            <div class="box mb-3">

                                <h5>
                                    ⏰ SẮP ĐẾN HẠN (trong 3 ngày)
                                    <% if (listDueSoon != null && !listDueSoon.isEmpty()) { %>
                                    <span class="badge bg-warning text-dark"><%=listDueSoon.size()%></span>
                                    <% } %>
                                </h5>

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

                                        if(listDueSoon!=null && !listDueSoon.isEmpty()){

                                            for(Debt d:listDueSoon){

                                        %>

                                        <tr>

                                            <td><%=d.getDebtCode()%></td>

                                            <td><%=d.getCustomerName()%></td>

                                            <td class="text-warning">

                                                <%=d.getMoney()%> VNĐ

                                            </td>

                                            <td>

                                                <%=d.getDateVN()%>

                                            </td>

                                        </tr>

                                        <%

                                            }

                                        } else {

                                        %>

                                        <tr>
                                            <td colspan="4" class="text-center text-muted p-3">
                                                Không có công nợ sắp đến hạn.
                                            </td>
                                        </tr>

                                        <%

                                        }

                                        %>

                                    </tbody>

                                </table>

                            </div>

                            <div class="box">

                                <h5>
                                    🚨 ĐÃ QUÁ HẠN
                                    <% if (listOverdue != null && !listOverdue.isEmpty()) { %>
                                    <span class="badge bg-danger"><%=listOverdue.size()%></span>
                                    <% } %>
                                </h5>

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

                                        if(listOverdue!=null && !listOverdue.isEmpty()){

                                            for(Debt d:listOverdue){

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

                                        } else {

                                        %>

                                        <tr>
                                            <td colspan="4" class="text-center text-muted p-3">
                                                Không có công nợ quá hạn.
                                            </td>
                                        </tr>

                                        <%

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

            // ===== Định dạng tiền VNĐ dùng chung cho tooltip =====
            const fmtVND = (v) => new Intl.NumberFormat('vi-VN').format(Math.round(v)) + " VNĐ";

            Chart.defaults.color = "#b7c0d4";
            Chart.defaults.borderColor = "#1e2740";
            Chart.defaults.font.family = "'Manrope', 'Segoe UI', sans-serif";

            // Style chung cho tooltip - đồng bộ theme tối, viền gradient thương hiệu
            const tooltipStyle = {
                backgroundColor: "#1a2437",
                titleColor: "#f3f5fa",
                bodyColor: "#eef1f8",
                borderColor: "#2dd4bf",
                borderWidth: 1,
                padding: 12,
                cornerRadius: 10,
                displayColors: false,
                titleFont: { family: "'Manrope', sans-serif", weight: "700" },
                bodyFont: { family: "'JetBrains Mono', monospace", size: 13 }
            };

            // ===== BIỂU ĐỒ 1: DOANH THU THEO THÁNG (dữ liệu thật, gradient fill) =====
            const ctx1 = document.getElementById("chart1").getContext("2d");

            const gradFill = ctx1.createLinearGradient(0, 0, 0, 280);
            gradFill.addColorStop(0, "rgba(45,212,191,.35)");
            gradFill.addColorStop(1, "rgba(167,139,250,.02)");

            const gradLine = ctx1.createLinearGradient(0, 0, 800, 0);
            gradLine.addColorStop(0, "#2dd4bf");
            gradLine.addColorStop(1, "#a78bfa");

            new Chart(ctx1, {

                type: "line",

                data: {
                    labels: ["T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12"],
                    datasets: [{
                        label: "Doanh thu",
                        data: <%= dashboard.getMonthlyRevenueJson() %>,
                        borderColor: gradLine,
                        backgroundColor: gradFill,
                        borderWidth: 3,
                        fill: true,
                        tension: .4,
                        pointRadius: 3,
                        pointHoverRadius: 6,
                        pointBackgroundColor: "#0a0e18",
                        pointBorderColor: "#2dd4bf",
                        pointBorderWidth: 2
                    }]
                },

                options: {
                    responsive: true,
                    interaction: { mode: "index", intersect: false },
                    plugins: {
                        legend: { display: false },
                        tooltip: { ...tooltipStyle,
                            callbacks: { label: (c) => fmtVND(c.parsed.y) }
                        }
                    },
                    scales: {
                        x: { grid: { display: false }, ticks: { color: "#8f9ab2" } },
                        y: {
                            grid: { color: "#1e2740" },
                            ticks: {
                                color: "#8f9ab2",
                                callback: (v) => (v / 1000000) + "tr"
                            }
                        }
                    }
                }

            });

            // ===== BIỂU ĐỒ 2: CÔNG NỢ THEO TRẠNG THÁI (dữ liệu thật, doughnut có số ở tâm) =====
            const paidCount = <%= dashboard.getPaidInvoiceCount() %>;
            const unpaidCount = <%= dashboard.getUnpaidInvoiceCount() %>;
            const totalInvoiceCount = paidCount + unpaidCount;

            const centerTextPlugin = {
                id: "centerText",
                afterDraw(chart) {
                    const { ctx, chartArea } = chart;
                    if (!chartArea) return;
                    const cx = (chartArea.left + chartArea.right) / 2;
                    const cy = (chartArea.top + chartArea.bottom) / 2;
                    ctx.save();
                    ctx.textAlign = "center";
                    ctx.textBaseline = "middle";
                    ctx.font = "700 26px 'JetBrains Mono', monospace";
                    ctx.fillStyle = "#f3f5fa";
                    ctx.fillText(totalInvoiceCount, cx, cy - 10);
                    ctx.font = "600 12px 'Manrope', sans-serif";
                    ctx.fillStyle = "#8f9ab2";
                    ctx.fillText("HÓA ĐƠN", cx, cy + 14);
                    ctx.restore();
                }
            };

            new Chart(document.getElementById("chart2"), {

                type: "doughnut",

                data: {
                    labels: ["Đã thanh toán", "Còn nợ"],
                    datasets: [{
                        data: [paidCount, unpaidCount],
                        backgroundColor: ["#2dd4bf", "#a78bfa"],
                        borderColor: "#131b2e",
                        borderWidth: 4,
                        hoverOffset: 6
                    }]
                },

                options: {
                    responsive: true,
                    cutout: "72%",
                    plugins: {
                        legend: {
                            labels: { color: "#b7c0d4", usePointStyle: true, pointStyle: "circle", padding: 18 }
                        },
                        tooltip: { ...tooltipStyle,
                            callbacks: { label: (c) => c.label + ": " + c.parsed + " hóa đơn" }
                        }
                    }
                },

                plugins: [centerTextPlugin]

            });

        </script>

    </body>

</html>