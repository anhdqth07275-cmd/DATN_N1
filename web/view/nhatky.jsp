<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.ActivityLog"%>
<%@page import="model.ActivitySummary"%>

<%
    ArrayList<ActivityLog> listLog =
            (ArrayList<ActivityLog>) request.getAttribute("listLog");
    ActivitySummary summary =
            (ActivitySummary) request.getAttribute("summary");
    String fromDate = (String) request.getAttribute("fromDate");
    String toDate = (String) request.getAttribute("toDate");

    java.text.SimpleDateFormat sdf =
            new java.text.SimpleDateFormat("HH:mm dd/MM/yyyy");
%>

<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">
    <title>Nhật ký hoạt động</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
          rel="stylesheet">

    <style>

        *{margin:0;padding:0;box-sizing:border-box;}

        body{background:#edf2f7;font-family:"Segoe UI",sans-serif;}

        .wrapper{display:flex;min-height:100vh;}

        .content{flex:1;padding:30px;}

        .box{background:#fff;border-radius:15px;padding:30px;
             box-shadow:0 5px 15px rgba(0,0,0,.08);}

        .page-title h2{color:#0f172a;font-weight:700;margin-bottom:5px;}

        .page-title p{color:#64748b;margin:0 0 20px 0;}

        .breadcrumb-link{color:#64748b;text-decoration:none;}

        .filter-bar{
            display:flex;
            align-items:end;
            gap:15px;
            background:#f8fafc;
            border:1px solid #e2e8f0;
            border-radius:10px;
            padding:18px;
            margin-bottom:25px;
            flex-wrap:wrap;
        }

        .filter-bar .field label{
            display:block;
            font-size:13px;
            color:#64748b;
            margin-bottom:5px;
        }

        .summary-cards{
            display:grid;
            grid-template-columns:repeat(6, 1fr);
            gap:15px;
            margin-bottom:25px;
        }

        .summary-card{
            border-radius:12px;
            padding:18px;
            color:#fff;
        }

        .summary-card .num{
            font-size:22px;
            font-weight:700;
        }

        .summary-card .label{
            font-size:13px;
            opacity:.9;
        }

        .card-thu{background:#0d6efd;}
        .card-chi{background:#0dcaf0;}
        .card-them{background:#198754;}
        .card-sua{background:#ffc107;}
        .card-xoa{background:#dc3545;}
        .card-login{background:#6c757d;}

        thead{background:#0d6efd;color:white;}

        thead th{text-align:center;}

        tbody td{vertical-align:middle;}

        tbody tr:hover{background:#f5f9ff;transition:.2s;}

        .empty-row{height:80px;text-align:center;color:#6c757d;}

        @media (max-width: 1200px){
            .summary-cards{grid-template-columns:repeat(3, 1fr);}
        }

        @media (max-width: 700px){
            .summary-cards{grid-template-columns:repeat(2, 1fr);}
        }

    </style>

</head>
<body>

<div class="wrapper">

    <jsp:include page="menu.jsp"/>

    <div class="content">

        <div class="box">

            <div class="page-title">

                <a class="breadcrumb-link"
                   href="<%=request.getContextPath()%>/view/hethong.jsp">
                    <i class="bi bi-arrow-left"></i> Hệ thống
                </a>

                <h2 class="mt-2">
                    <i class="bi bi-journal-text"></i>
                    Nhật ký hoạt động
                </h2>

                <p>
                    Theo dõi và tổng hợp toàn bộ hoạt động của người dùng
                    trong hệ thống theo khoảng thời gian.
                </p>

            </div>

            <!-- Thanh chọn khoảng thời gian -->
            <form class="filter-bar" method="get"
                  action="<%=request.getContextPath()%>/nhatky">

                <div class="field">
                    <label>Từ ngày</label>
                    <input type="date" name="fromDate" class="form-control"
                           value="<%=fromDate%>">
                </div>

                <div class="field">
                    <label>Đến ngày</label>
                    <input type="date" name="toDate" class="form-control"
                           value="<%=toDate%>">
                </div>

                <div class="field">
                    <button class="btn btn-primary">
                        <i class="bi bi-funnel-fill"></i>
                        Lọc dữ liệu
                    </button>
                </div>

            </form>

            <!-- Tổng hợp -->
            <% if (summary != null) { %>

            <div class="summary-cards">

                <div class="summary-card card-thu">
                    <div class="num">
                        <%=String.format("%,.0f", summary.getTotalReceipt())%> đ
                    </div>
                    <div class="label">
                        Tổng thu (<%=summary.getReceiptCount()%> phiếu)
                    </div>
                </div>

                <div class="summary-card card-chi">
                    <div class="num">
                        <%=String.format("%,.0f", summary.getTotalExpense())%> đ
                    </div>
                    <div class="label">
                        Tổng chi (<%=summary.getExpenseCount()%> phiếu)
                    </div>
                </div>

                <div class="summary-card card-them">
                    <div class="num"><%=summary.getAddCount()%></div>
                    <div class="label">Lượt thêm mới</div>
                </div>

                <div class="summary-card card-sua">
                    <div class="num"><%=summary.getEditCount()%></div>
                    <div class="label">Lượt cập nhật</div>
                </div>

                <div class="summary-card card-xoa">
                    <div class="num"><%=summary.getDeleteCount()%></div>
                    <div class="label">Lượt xóa</div>
                </div>

                <div class="summary-card card-login">
                    <div class="num"><%=summary.getLoginCount()%></div>
                    <div class="label">Lượt đăng nhập</div>
                </div>

            </div>

            <% } %>

            <!-- Bảng chi tiết -->
            <table class="table table-bordered table-hover align-middle">

                <thead>
                <tr>
                    <th width="150">Thời gian</th>
                    <th>Người thực hiện</th>
                    <th width="160">Vai trò</th>
                    <th width="120">Hành động</th>
                    <th width="140">Module</th>
                    <th>Nội dung</th>
                    <th width="130">Số tiền</th>
                </tr>
                </thead>

                <tbody>

                <%
                    if (listLog != null && !listLog.isEmpty()) {

                        for (ActivityLog log : listLog) {
                %>

                <tr>

                    <td><%=sdf.format(log.getCreatedDate())%></td>

                    <td>
                        <%=log.getFullName() != null ? log.getFullName() : log.getUsername()%>
                        <div class="text-muted small">@<%=log.getUsername()%></div>
                    </td>

                    <td><%=log.getRoleName()%></td>

                    <td align="center">
                        <span class="badge <%=log.getActionBadgeClass()%>">
                            <%=log.getActionLabel()%>
                        </span>
                    </td>

                    <td><%=log.getModule()%></td>

                    <td><%=log.getDescription()%></td>

                    <td align="right">
                        <%
                            if (log.getAmount() != null) {
                        %>
                        <%=String.format("%,.0f", log.getAmount())%> đ
                        <%
                            } else {
                        %>
                        -
                        <%
                            }
                        %>
                    </td>

                </tr>

                <%
                        }

                    } else {
                %>

                <tr>
                    <td colspan="7" class="empty-row">
                        <i class="bi bi-database-exclamation"></i>
                        Không có hoạt động nào trong khoảng thời gian đã chọn.
                    </td>
                </tr>

                <% } %>

                </tbody>

            </table>

        </div>

    </div>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
