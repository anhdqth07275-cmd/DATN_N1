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
    

    <a href="trangchu.jsp">🏠 Trang chủ</a>

    <a href="qlkh.jsp">👤 Khách hàng</a>

    <a href="listhoadon.jsp">📄 Hóa đơn</a>

    <a href="congno.jsp">💰 Công nợ</a>

    <a href="phieuthu_chi.jsp">💵 Thu chi</a>

    <a href="baocao.jsp">📊 Báo cáo</a>

    <a href="hethong.jsp">⚙ Hệ thống</a>

    <a href="dangnhap.jsp">🚪 Đăng xuất</a>

</div>
