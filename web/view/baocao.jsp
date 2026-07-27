<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="model.DangKy"%>
<%@page import="model.Report"%>
<%@page import="model.Debt"%>
<%@page import="java.util.ArrayList"%>

<%
DangKy user = (DangKy) session.getAttribute("user");

if (user == null) {
    response.sendRedirect(request.getContextPath() + "/dangnhap");
    return;
}

Report report = (Report) request.getAttribute("report");

if (report == null) {
    response.sendRedirect(request.getContextPath() + "/baocao");
    return;
}

ArrayList<Debt> topDebts = report.getTopDebts();
%>
<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<title>Báo cáo - Thống kê</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
      rel="stylesheet">

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<style>

*{
    box-sizing:border-box;
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
}

.topbar{
    background:white;
    padding:15px 30px;
    display:flex;
    justify-content:space-between;
    align-items:center;
    box-shadow:0 0 5px #ccc;
}

.topbar h3{
    margin:0;
}

.main{
    padding:30px;
}

.card-box{
    background:white;
    border-radius:12px;
    padding:20px;
    box-shadow:0 0 5px #ddd;
    height:100%;
}

.card-box h6{
    color:gray;
    margin-bottom:10px;
}

.card-box h4{
    font-weight:bold;
    margin:0;
}

.box{
    background:white;
    border-radius:12px;
    padding:20px;
    box-shadow:0 0 5px #ddd;
    margin-top:25px;
}

.table td, .table th{
    font-size:14px;
    vertical-align:middle;
}

.badge-status{
    font-size:12px;
}

</style>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dark-theme.css">
</head>

<body>

<div class="wrapper">

    <jsp:include page="menu.jsp"/>

    <div class="content">

        <div class="topbar">

            <h3>📊 Báo cáo - Thống kê</h3>

            <div class="user-box">
                👤 <strong><%= user.getFullName() %></strong>
            </div>

        </div>

        <div class="main">

            <div class="print-only" style="margin-bottom:20px;">
                <h2 style="margin:0;">SME:FAD — Báo cáo tài chính &amp; công nợ</h2>
                <p style="margin:4px 0 0;">
                    Khoảng thời gian: <strong><%= report.getRangeLabel() %></strong>
                    &nbsp;|&nbsp; Người lập: <strong><%= user.getFullName() %></strong>
                    &nbsp;|&nbsp; Ngày in:
                    <strong><%= new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date()) %></strong>
                </p>
                <hr>
            </div>

            <form method="get"
                  action="${pageContext.request.contextPath}/baocao"
                  class="box d-flex align-items-end gap-3 flex-wrap">

                <div>
                    <label class="form-label mb-1">Khoảng thời gian</label>

                    <select name="range" class="form-select">

                        <option value="thisMonth"
                                <%= "thisMonth".equals(report.getRange()) ? "selected" : "" %>>
                            Tháng này
                        </option>

                        <option value="thisQuarter"
                                <%= "thisQuarter".equals(report.getRange()) ? "selected" : "" %>>
                            Quý này
                        </option>

                        <option value="thisYear"
                                <%= "thisYear".equals(report.getRange()) ? "selected" : "" %>>
                            Năm nay
                        </option>

                    </select>
                </div>

                <div>
                    <button type="submit" class="btn btn-primary">
                        Xem báo cáo
                    </button>
                </div>

                <div>
                    <button type="button" class="btn btn-outline-secondary"
                            onclick="window.print()">
                        🖨️ In báo cáo
                    </button>
                </div>

                <div class="ms-auto text-muted align-self-center">
                    Dữ liệu theo: <strong><%= report.getRangeLabel() %></strong>
                </div>

            </form>

            <div class="row mt-3 g-3">

                <div class="col-md-3">
                    <div class="card-box border-start border-4 border-primary">
                        <h6>💰 Báo cáo doanh thu</h6>
                        <h4 class="text-primary"><%= report.getRevenueMoney() %> VNĐ</h4>
                    </div>
                </div>

                <div class="col-md-3">
                    <div class="card-box border-start border-4 border-warning">
                        <h6>📋 Báo cáo công nợ</h6>
                        <h4 class="text-warning"><%= report.getDebtMoney() %> VNĐ</h4>
                    </div>
                </div>

                <div class="col-md-3">
                    <div class="card-box border-start border-4 border-danger">
                        <h6>💸 Báo cáo chi thu</h6>
                        <h4 class="text-success">+<%= report.getReceiptMoney() %></h4>
                        <h4 class="text-danger">-<%= report.getExpenseMoney() %></h4>
                    </div>
                </div>

                <div class="col-md-3">
                    <div class="card-box border-start border-4 border-success">
                        <h6>✅ Báo cáo thanh toán</h6>
                        <h4 class="text-success"><%= report.getReceiptMoney() %> VNĐ</h4>
                    </div>
                </div>

            </div>

            <div class="row">

                <div class="col-md-8">

                    <div class="box">

                        <h5>📈 Doanh thu theo tháng (năm nay)</h5>

                        <canvas id="chartRevenue" height="110"></canvas>

                    </div>

                </div>

                <div class="col-md-4">

                    <div class="box">

                        <h5>🧾 Trạng thái hóa đơn</h5>

                        <canvas id="chartInvoiceStatus"></canvas>

                    </div>

                </div>

            </div>

            <div class="box">

                <h5>📋 Top khách hàng có công nợ cao nhất</h5>

                <table class="table table-hover">

                    <thead>
                        <tr>
                            <th>Mã CN</th>
                            <th>Khách hàng</th>
                            <th>Mã hóa đơn</th>
                            <th>Số tiền còn nợ</th>
                            <th>Hạn thanh toán</th>
                            <th>Trạng thái</th>
                        </tr>
                    </thead>

                    <tbody>

                        <%
                        if (topDebts != null && !topDebts.isEmpty()) {

                            for (Debt d : topDebts) {
                        %>

                        <tr>
                            <td><%= d.getDebtCode() %></td>
                            <td><%= d.getCustomerName() %></td>
                            <td><%= d.getInvoiceCode() %></td>
                            <td class="text-danger"><%= d.getMoney() %> VNĐ</td>
                            <td><%= d.getDateVN() %></td>
                            <td>
                                <span class="badge bg-warning text-dark badge-status">
                                    <%= d.getStatus() %>
                                </span>
                            </td>
                        </tr>

                        <%
                            }

                        } else {
                        %>

                        <tr>
                            <td colspan="6" class="text-center text-muted">
                                Không có công nợ nào.
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

<script>

const fmtVND = (v) => new Intl.NumberFormat('vi-VN').format(Math.round(v)) + " VNĐ";

Chart.defaults.color = "#b7c0d4";
Chart.defaults.borderColor = "#1e2740";
Chart.defaults.font.family = "'Manrope', 'Segoe UI', sans-serif";

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

const ctxRevenue = document.getElementById("chartRevenue").getContext("2d");
const gradBar = ctxRevenue.createLinearGradient(0, 0, 0, 260);
gradBar.addColorStop(0, "#2dd4bf");
gradBar.addColorStop(1, "#a78bfa");

new Chart(ctxRevenue, {

    type: "bar",

    data: {
        labels: <%= report.getMonthLabelsJson() %>,
        datasets: [{
            label: "Doanh thu",
            data: <%= report.getMonthlyRevenueJson() %>,
            backgroundColor: gradBar,
            borderRadius: 8,
            maxBarThickness: 34,
            hoverBackgroundColor: "#5eead4"
        }]
    },

    options: {
        responsive: true,
        plugins: {
            legend: { display: false },
            tooltip: { ...tooltipStyle, callbacks: { label: (c) => fmtVND(c.parsed.y) } }
        },
        scales: {
            x: { grid: { display: false }, ticks: { color: "#8f9ab2" } },
            y: {
                grid: { color: "#1e2740" },
                ticks: { color: "#8f9ab2", callback: (v) => (v / 1000000) + "tr" }
            }
        }
    }

});

const paidCnt = <%= report.getPaidInvoiceCount() %>;
const unpaidCnt = <%= report.getUnpaidInvoiceCount() %>;

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
        ctx.fillText(paidCnt + unpaidCnt, cx, cy - 10);
        ctx.font = "600 12px 'Manrope', sans-serif";
        ctx.fillStyle = "#8f9ab2";
        ctx.fillText("HÓA ĐƠN", cx, cy + 14);
        ctx.restore();
    }
};

new Chart(document.getElementById("chartInvoiceStatus"), {

    type: "doughnut",

    data: {
        labels: ["Đã thanh toán", "Còn nợ"],
        datasets: [{
            data: [paidCnt, unpaidCnt],
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
            legend: { labels: { color: "#b7c0d4", usePointStyle: true, pointStyle: "circle", padding: 18 } },
            tooltip: { ...tooltipStyle, callbacks: { label: (c) => c.label + ": " + c.parsed + " hóa đơn" } }
        }
    },

    plugins: [centerTextPlugin]

});

</script>

</body>
</html>
