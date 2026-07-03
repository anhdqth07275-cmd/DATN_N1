<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">
<title>Phiếu thu - Phiếu chi</title>

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
    padding:20px;
    box-shadow:0 0 5px #ddd;
}

.top{
    display:flex;
    justify-content:space-between;
    margin-bottom:20px;
}

</style>

</head>

<body>

<div class="wrapper">

    <jsp:include page="menu.jsp"/>

    <div class="content">

        <div class="box">

            <ul class="nav nav-tabs mb-4">

                <li class="nav-item">

                    <a class="nav-link active">
                        Phiếu thu
                    </a>

                </li>

                <li class="nav-item">

                    <a class="nav-link">
                        Phiếu chi
                    </a>

                </li>

            </ul>

            <div class="top">

                <button class="btn btn-primary">
                    + Thêm phiếu chi
                </button>

                <input
                    type="text"
                    class="form-control"
                    style="width:250px"
                    placeholder="Tìm kiếm...">

            </div>

            <table class="table table-bordered table-hover">

                <tr>

                    <th>Mã PC</th>
                    <th>Ngày chi</th>
                    <th>Danh mục</th>
                    <th>Nội dung</th>
                    <th>Số tiền</th>
                    <th>Nhân viên</th>

                </tr>

                <tr>

                    <td>PC0001</td>
                    <td>20/05/2024</td>
                    <td>Tiền lương</td>
                    <td>Lương T5/2024</td>
                    <td>20,000,000</td>
                    <td>Admin</td>

                </tr>

                <tr>

                    <td>PC0002</td>
                    <td>20/05/2024</td>
                    <td>Văn phòng phẩm</td>
                    <td>Mua giấy, bút</td>
                    <td>1,200,000</td>
                    <td>Admin</td>

                </tr>

                <tr>

                    <td>PC0003</td>
                    <td>19/05/2024</td>
                    <td>Điện nước</td>
                    <td>Tiền điện T5</td>
                    <td>2,500,000</td>
                    <td>Admin</td>

                </tr>

            </table>

        </div>

    </div>

</div>

</body>
</html>