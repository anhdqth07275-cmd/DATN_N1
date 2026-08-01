<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.DangKy"%>
<%@page import="model.Debt"%>
<%@page import="dao.DebtDAO"%>
<%@page import="dao.PermissionDAO"%>
<%@page import="java.util.ArrayList"%>

<%
    DangKy menuUser = (DangKy) session.getAttribute("user");
    boolean menuIsAdmin = menuUser != null
            && "Quản trị viên".equals(menuUser.getRoleName());

    String menuCtx = request.getContextPath();
    String menuURI = request.getRequestURI();

    // ==========================
    // Chuông cảnh báo công nợ - "nhắc đúng tài khoản": nhân viên
    // không có quyền CONGNO_XEMTATCA chỉ thấy cảnh báo của hóa đơn
    // do CHÍNH mình lập; Admin/Giám đốc thấy toàn bộ.
    // ==========================
    ArrayList<Debt> menuDueSoon = new ArrayList<>();
    ArrayList<Debt> menuOverdue = new ArrayList<>();
    int menuAlertCount = 0;

    if (menuUser != null) {

        PermissionDAO menuPermissionDAO = new PermissionDAO();
        DebtDAO menuDebtDAO = new DebtDAO();

        boolean menuSeeAll = menuPermissionDAO.hasPermission(
                menuUser.getRoleId(), "CONGNO_XEMTATCA");

        Integer menuScopeUserId = menuSeeAll ? null : menuUser.getUserId();

        menuDueSoon = menuDebtDAO.getDueSoon(3, menuScopeUserId, 5);
        menuOverdue = menuDebtDAO.getOverdue(menuScopeUserId, 5);

        menuAlertCount = menuDebtDAO.countDueSoon(3, menuScopeUserId)
                + menuDebtDAO.countOverdue(menuScopeUserId);

    }
%>

<style>

    *{
        margin:0;
        padding:0;
        box-sizing:border-box;
    }

    .sidebar{
        width:260px;
        min-height:100vh;
        background:#2c3e50;
        flex-shrink:0;
    }

    .logo{
        text-align: center;
        padding: 40px 0 25px;
        border-bottom: 1px solid #45576d;
    }

    .logo h4{
        color: #fff;
        font-size: 22px;
        font-weight: bold;
        margin: 0;
        letter-spacing: 1px;
    }

    .sidebar a{
        display:block;
        color:white;
        text-decoration:none;
        padding:18px 28px;
        font-size:18px;
        transition:.3s;
    }

    .sidebar a:hover{
        background:#34495e;
        color:white;
    }

    .sidebar .active{
        background:#0d6efd;
    }

    /* ===== Chuông cảnh báo công nợ ===== */
    .notif-bell{
        position:fixed;
        bottom:20px;
        right:30px;
        z-index:1000;
        width:70px;
        height:70px;
        border-radius:50%;
        background:#fff;
        box-shadow:0 3px 10px rgba(0,0,0,.15);
        display:flex;
        align-items:center;
        justify-content:center;
        cursor:pointer;
        font-size:20px;
    }

    .notif-badge{
        position:absolute;
        top:-4px;
        right:-4px;
        background:#dc3545;
        color:#fff;
        border-radius:50%;
        min-width:20px;
        height:20px;
        font-size:11px;
        font-weight:700;
        display:flex;
        align-items:center;
        justify-content:center;
        padding:0 4px;
    }

    .notif-dropdown{
        position:fixed;
        bottom:106px;
        right:30px;
        z-index:1000;
        width:340px;
        max-height:420px;
        overflow-y:auto;
        background:#fff;
        border-radius:12px;
        box-shadow:0 8px 24px rgba(0,0,0,.18);
        display:none;
    }

    .notif-dropdown.show{
        display:block;
    }

    .sapdenhan{
        padding:12px 16px 6px;
        margin:0;
        font-size:13px;
        font-weight:bold;
        color: black !important;
        text-transform:uppercase;
    }

    .notif-item{
        display:block;
        padding:10px 16px;
        text-decoration:none;
        color:#212529;
        border-top:1px solid #f1f3f5;
    }

    .notif-item:hover{
        background:#f8fbff;
    }

    .notif-item .notif-name{
        font-weight:bold;
        font-size:14px;
    }

    .notif-item .notif-sub{
        font-size:12px;
        color:#000000;
    }

    .notif-empty{
        padding:16px;
        text-align:center;
        color:#adb5bd;
        font-size:13px;
    }

</style>
<div class="sidebar">

    <div class="logo">
        <div class="brand">
            <img src="<%=menuCtx%>/img/logo.png" alt="SME:FAD">
            <p class="brand-text">SME:FAD</p>
        </div>
        <span class="brand-sub">Quản lý tài chính &amp; công nợ</span>
    </div>

    <span class="nav-group-label">Tổng quan</span>

    <a class="<%=menuURI.endsWith("/trangchu") ? "active" : ""%>"
       href="${pageContext.request.contextPath}/trangchu">
        🏠 Trang chủ
    </a>

    <span class="nav-group-label">Nghiệp vụ</span>

    <a class="<%=menuURI.endsWith("/khachhang") ? "active" : ""%>"
       href="${pageContext.request.contextPath}/khachhang">
        👤 Khách hàng
    </a>

    <a class="<%=menuURI.endsWith("/hoadon") ? "active" : ""%>"
       href="${pageContext.request.contextPath}/hoadon">
        📄 Hóa đơn
    </a>

    <a class="<%=menuURI.endsWith("/congno") ? "active" : ""%>"
       href="${pageContext.request.contextPath}/congno">
        💰 Công nợ
    </a>

    <a class="<%=menuURI.endsWith("/phieuthu") ? "active" : ""%>"
       href="${pageContext.request.contextPath}/phieuthu">
        💵 Thu tiền
    </a>

    <a class="<%=menuURI.endsWith("/phieuchi") ? "active" : ""%>"
       href="${pageContext.request.contextPath}/phieuchi">
        💸 Chi tiền
    </a>

    <a class="<%=menuURI.endsWith("/baocao") ? "active" : ""%>"
       href="${pageContext.request.contextPath}/baocao">
        📊 Báo cáo
    </a>

    <% if (menuIsAdmin) { %>
    <span class="nav-group-label">Hệ thống</span>

    <a class="<%=menuURI.endsWith("hethong.jsp") ? "active" : ""%>"
       href="${pageContext.request.contextPath}/view/hethong.jsp">
        ⚙ Hệ thống
    </a>
    <% } %>

<hr>

<a href="${pageContext.request.contextPath}/dangxuat">
    🚪 Đăng xuất
</a>

</div>

<% if (menuUser != null) { %>

<div class="notif-bell" id="notifBell" onclick="toggleNotifDropdown()">
    🔔
    <% if (menuAlertCount > 0) { %>
    <span class="notif-badge"><%=menuAlertCount > 99 ? "99+" : menuAlertCount%></span>
    <% } %>
</div>

<div class="notif-dropdown" id="notifDropdown">

    <% if (!menuOverdue.isEmpty()) { %>
    <h6 class="sapdenhan">🚨 Đã quá hạn</h6>
    <% for (Debt d : menuOverdue) { %>
    <a class="notif-item"
       href="<%=menuCtx%>/congno?action=search&keyword=<%=java.net.URLEncoder.encode(d.getCustomerName(), "UTF-8")%>">
        <div class="notif-name"><%=d.getCustomerName()%></div>
        <div class="notif-sub">
            Còn nợ <%=d.getMoney()%> VNĐ — hạn <%=d.getDateVN()%>
        </div>
    </a>
    <% } %>
    <% } %>

    <% if (!menuDueSoon.isEmpty()) { %>
    <h6 class="sapdenhan">⏰ Sắp đến hạn (3 ngày)</h6>
    <% for (Debt d : menuDueSoon) { %>
    <a class="notif-item"
       href="<%=menuCtx%>/congno?action=search&keyword=<%=java.net.URLEncoder.encode(d.getCustomerName(), "UTF-8")%>">
        <div class="notif-name"><%=d.getCustomerName()%></div>
        <div class="notif-sub">
            Còn nợ <%=d.getMoney()%> VNĐ — hạn <%=d.getDateVN()%>
        </div>
    </a>
    <% } %>
    <% } %>

    <% if (menuOverdue.isEmpty() && menuDueSoon.isEmpty()) { %>
    <div class="notif-empty">Không có công nợ cần nhắc.</div>
    <% } %>

</div>

<script>
    function toggleNotifDropdown() {
        document.getElementById('notifDropdown').classList.toggle('show');
    }

    document.addEventListener('click', function (e) {
        var bell = document.getElementById('notifBell');
        var dropdown = document.getElementById('notifDropdown');
        if (bell && dropdown
                && !bell.contains(e.target)
                && !dropdown.contains(e.target)) {
            dropdown.classList.remove('show');
        }
    });
</script>

<% } %>
