<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quên mật khẩu</title>
    <style>
        *{margin:0;padding:0;box-sizing:border-box;font-family:Arial, Helvetica, sans-serif;}
        body{background:#eef3f8;display:flex;justify-content:center;align-items:center;height:100vh;}
        .container{width:850px;display:flex;background:white;border-radius:15px;overflow:hidden;box-shadow:0 10px 30px rgba(0,0,0,.2);}
        .left{width:45%;background:#0b4f86;color:white;display:flex;justify-content:center;align-items:center;flex-direction:column;padding:40px;}
        .left h1{font-size:34px;margin-bottom:20px;}
        .left p{font-size:18px;text-align:center;line-height:28px;}
        .right{width:55%;padding:45px;}
        .right h2{text-align:center;color:#0b4f86;margin-bottom:10px;}
        .right .subtitle{text-align:center;color:#666;font-size:14px;margin-bottom:25px;}
        .input-group{margin-bottom:20px;}
        .input-group label{display:block;margin-bottom:6px;font-weight:bold;}
        .input-group input{width:100%;height:42px;border:1px solid #ccc;border-radius:6px;padding-left:12px;font-size:15px;}
        .btn{width:100%;height:45px;border:none;border-radius:6px;background:#0b4f86;color:white;font-size:17px;cursor:pointer;transition:.3s;}
        .btn:hover{background:#08375d;}
        .register{text-align:center;margin-top:20px;}
        .register a{text-decoration:none;color:#0b4f86;font-weight:bold;}
        .error{background:#ffdede;color:red;padding:12px;border-radius:6px;text-align:center;margin-bottom:15px;}
        .success{background:#ddffdd;color:green;padding:12px;border-radius:6px;text-align:center;margin-bottom:15px;}
    </style>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dark-theme.css">
</head>
<body>
<div class="container">
    <div class="left">
        <img class="brand-logo" src="${pageContext.request.contextPath}/img/logo.png" alt="SME:FAD">
        <h1>SME:FAD</h1>
        <p>Hệ thống quản lý tài chính<br>và công nợ khách hàng</p>
    </div>
    <div class="right">
        <h2>QUÊN MẬT KHẨU</h2>
        <div class="subtitle">Nhập email đã đăng ký với hệ thống để nhận mã OTP</div>

        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
        <div class="error"><%=error%></div>
        <%
            }
        %>

        <form action="${pageContext.request.contextPath}/quenmatkhau" method="post">
            <input type="hidden" name="action" value="sendOtp">
            <div class="input-group">
                <label>Email đã đăng ký</label>
                <input type="email" name="email" placeholder="vidu@gmail.com" required autofocus>
            </div>
            <button class="btn" type="submit">GỬI MÃ OTP</button>
        </form>

        <div class="register">
            <a href="${pageContext.request.contextPath}/dangnhap">&larr; Quay lại đăng nhập</a>
        </div>
    </div>
</div>
</body>
</html>
