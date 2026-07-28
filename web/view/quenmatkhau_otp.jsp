<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Nhập mã OTP</title>
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
        .input-group input{width:100%;height:42px;border:1px solid #ccc;border-radius:6px;padding-left:12px;font-size:22px;letter-spacing:8px;text-align:center;}
        .btn{width:100%;height:45px;border:none;border-radius:6px;background:#0b4f86;color:white;font-size:17px;cursor:pointer;transition:.3s;}
        .btn:hover{background:#08375d;}
        .register{text-align:center;margin-top:20px;}
        .register a, .register button{text-decoration:none;color:#0b4f86;font-weight:bold;background:none;border:none;font-size:14px;cursor:pointer;}
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
        <h2>NHẬP MÃ OTP</h2>
        <div class="subtitle">
            Mã xác thực đã được gửi tới email
            <b><%= session.getAttribute("fp_email") %></b>
        </div>

        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
        <div class="error"><%=error%></div>
        <%
            }
            String success = (String) request.getAttribute("success");
            if (success != null) {
        %>
        <div class="success"><%=success%></div>
        <%
            }
        %>

        <form action="${pageContext.request.contextPath}/quenmatkhau" method="post">
            <input type="hidden" name="action" value="verifyOtp">
            <div class="input-group">
                <label>Mã OTP (6 chữ số)</label>
                <input type="text" name="otp" maxlength="6" pattern="\d{6}"
                       inputmode="numeric" placeholder="------" required autofocus>
            </div>
            <button class="btn" type="submit">XÁC NHẬN</button>
        </form>

        <div class="register">
            <form action="${pageContext.request.contextPath}/quenmatkhau" method="post" style="display:inline">
                <input type="hidden" name="action" value="resendOtp">
                <button type="submit">Không nhận được mã? Gửi lại</button>
            </form>
            <br><br>
            <a href="${pageContext.request.contextPath}/dangnhap">&larr; Quay lại đăng nhập</a>
        </div>
    </div>
</div>
</body>
</html>
