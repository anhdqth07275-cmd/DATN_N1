<%-- 
    Document   : menu
    Created on : Jun 20, 2026, 10:46:02 AM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<style>
    .sidebar {
    width: 250px;
    height: fit-content;
    position: sticky;
    top: 0;
    background: #2c3e50;
}
        .h4{
            color: white
        }
        .logo{
    text-align:center;
    padding:40px 0 25px 0;
    border-bottom:1px solid #334155;
}

.logo h3{
    color:#fff;
    font-size:22px;
    font-weight:700;
    letter-spacing:1px;
    margin:0;
}
    </style>
<div class="sidebar">
    
    <div class="logo">
        <h4>QUẢN LÍ TÀI CHÍNH</h4>
    </div>
    

    <a href="trangchu.jsp">🏠 Trang chủ</a>

    <a href="qlkh.jsp">👤 Khách hàng</a>

    <a href="listhoadon.jsp">📄 Hóa đơn</a>

    <a href="congno.jsp">💰 Công nợ</a>

    <a href="phieuthu_chi.jsp">💵 Thu chi</a>

    <a href="baocao.jsp">📊 Báo cáo</a>

    <a href="hethong.jsp">⚙ Hệ thống</a>

    <a href="dangnhap.jsp">🚪 Đăng xuất</a>

</div>
