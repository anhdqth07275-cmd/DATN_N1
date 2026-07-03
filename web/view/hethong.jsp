<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">
<title>Hệ thống</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
      rel="stylesheet">

<style>
    
.sidebar{
    width:240px;
    background:#0f172a;
    min-height:100vh;
}

.logo{
    text-align:center;
    padding-top:40px;
    padding-bottom:25px;
    border-bottom:1px solid #334155;
}

.logo h4{
    color:#ffffff;       /* Chữ QUẢN LÍ TÀI CHÍNH màu trắng */
    margin:0;
    font-size:22px;
    font-weight:bold;
}

.sidebar a{
    display:block;
    color:#ffffff;       /* Các mục menu màu trắng */
    text-decoration:none;
    padding:15px 20px;
    font-size:16px;
}

.sidebar a:hover{
    background:#1e293b;
    color:#ffffff;       /* Hover vẫn giữ màu trắng */
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
    padding:30px;
}

.box{
    background:white;
    border-radius:12px;
    padding:25px;
    box-shadow:0 0 5px #ddd;
}

.list-group-item{
    padding:20px;
    font-size:18px;
}

</style>

</head>

<body>

<div class="wrapper">

    <jsp:include page="menu.jsp"/>

    <div class="content">

        <div class="box">

            <h3 class="mb-4">

                HỆ THỐNG

            </h3>

            <ul class="list-group">

                <li class="list-group-item">
                    Người dùng
                </li>

                

                <li class="list-group-item">
                    Phân quyền
                </li>

              

                <li class="list-group-item">
                    Nhật ký hoạt động
                </li>

                <li class="list-group-item">
                    Sao lưu dữ liệu
                </li>

            </ul>

        </div>

    </div>

</div>

</body>
</html>