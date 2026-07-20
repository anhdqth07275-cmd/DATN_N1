<%@page contentType="text/html" pageEncoding="UTF-8"%>

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

</style>
<div class="sidebar">

    <div class="logo">
        <h4>QUẢN LÍ TÀI CHÍNH</h4>
    </div>


    <a href="${pageContext.request.contextPath}/trangchu">
        🏠 Trang chủ
    </a>


    <a href="${pageContext.request.contextPath}/khachhang">
        👤 Khách hàng
    </a>
    <a href="${pageContext.request.contextPath}/hoadon">
        📄 Hóa đơn
    </a>

</a>
<a href="${pageContext.request.contextPath}/congno">
    💰 Công nợ
</a>

<a href="${pageContext.request.contextPath}/phieuthu">
    💵 Thu tiền
</a>
<a href="${pageContext.request.contextPath}/phieuchi">

    💸 chi tiền
</a>

<a href="baocao.jsp">📊 Báo cáo</a>

<a href="hethong.jsp">⚙ Hệ thống</a>

<hr>

<a href="${pageContext.request.contextPath}/dangxuat">
    🚪 Đăng xuất
</a>

</div>
