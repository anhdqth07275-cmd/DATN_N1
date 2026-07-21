<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Đăng ký</title>

        <style>

            *{
                margin:0;
                padding:0;
                box-sizing:border-box;
                font-family:Arial, Helvetica, sans-serif;
            }

            body{

                background:#eef3f8;

                display:flex;

                justify-content:center;

                align-items:center;

                height:100vh;

            }

            .container{

                width:900px;

                background:white;

                display:flex;

                border-radius:15px;

                overflow:hidden;

                box-shadow:0 10px 30px rgba(0,0,0,.2);

            }

            .left{

                width:45%;

                background:#0b4f86;

                color:white;

                display:flex;

                justify-content:center;

                align-items:center;

                flex-direction:column;

                padding:40px;

            }

            .left h1{

                font-size:32px;

                margin-bottom:20px;

            }

            .left p{

                text-align:center;

                line-height:28px;

                font-size:18px;

            }

            .right{

                width:55%;

                padding:40px;

            }

            .right h2{

                text-align:center;

                color:#0b4f86;

                margin-bottom:30px;

            }

            .input-group{

                margin-bottom:18px;

            }

            .input-group label{

                display:block;

                margin-bottom:6px;

                font-weight:bold;

            }

            .input-group input{

                width:100%;

                height:42px;

                border:1px solid #ccc;

                border-radius:6px;

                padding-left:12px;

                font-size:15px;

            }

            .input-group select{

                width:100%;

                height:42px;

                border:1px solid #ccc;

                border-radius:6px;

                padding-left:12px;

                font-size:15px;

            }

            .btn{

                width:100%;

                height:45px;

                background:#0b4f86;

                color:white;

                border:none;

                border-radius:6px;

                font-size:17px;

                cursor:pointer;

                transition:.3s;

            }

            .btn:hover{

                background:#08375d;

            }

            .login-link{

                text-align:center;

                margin-top:18px;

            }

            .login-link a{

                text-decoration:none;

                color:#0b4f86;

                font-weight:bold;

            }

            .error{

                width:100%;

                padding:12px;

                background:#ffdddd;

                color:red;

                border-radius:6px;

                margin-bottom:15px;

                text-align:center;

            }

            .success{

                width:100%;

                padding:12px;

                background:#ddffdd;

                color:green;

                border-radius:6px;

                margin-bottom:15px;

                text-align:center;

            }

        </style>

    </head>

    <body>

        <div class="container">

            <div class="left">

                <h1>SMEFAD</h1>

                <p>

                    Hệ thống quản lý tài chính
                    <br>
                    và công nợ khách hàng

                </p>

            </div>

            <div class="right">

                <h2>ĐĂNG KÝ TÀI KHOẢN</h2>

                <%
                    String error = (String)request.getAttribute("error");

                    if(error!=null){
                %>

                <div class="error">

                    <%=error%>

                </div>

                <%
                    }
                %>

                <form action="${pageContext.request.contextPath}/dangky" method="post">

                    <div class="input-group">

                        <label>Họ và tên</label>

                        <input
                            type="text"
                            name="fullName"
                            required>

                    </div>

                    <div class="input-group">

                        <label>Tên đăng nhập</label>

                        <input
                            type="text"
                            name="username"
                            required>

                    </div>

                    <div class="input-group">

                        <label>Mật khẩu</label>

                        <input
                            type="password"
                            name="password"
                            required>

                    </div>

                    <div class="input-group">

                        <label>Email</label>

                        <input
                            type="email"
                            name="email"
                            required>

                    </div>

                    <div class="input-group">

                        <label>Số điện thoại</label>

                        <input
                            type="text"
                            name="phone"
                            required>

                    </div>
                    <button class="btn" type="submit">

                        ĐĂNG KÝ

                    </button>

                </form>

                <div class="login-link">

                    Đã có tài khoản?

                    <a href="${pageContext.request.contextPath}/dangnhap">
                        Đăng nhập
                    </a>

                </div>

            </div>

        </div>

    </body>

</html>