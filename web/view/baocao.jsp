<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<title>Báo cáo</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
      rel="stylesheet">

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

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

.card-box{
    background:white;
    border-radius:12px;
    padding:20px;
    box-shadow:0 0 5px #ddd;
    text-align:center;
}

.box{
    background:white;
    border-radius:12px;
    padding:20px;
    box-shadow:0 0 5px #ddd;
    margin-top:25px;
}

</style>

</head>

<body>

<div class="wrapper">

    <jsp:include page="menu.jsp"/>

    <div class="content">

        <h3 class="mb-4">
            BÁO CÁO - THỐNG KÊ
        </h3>

        <div class="row">

            <div class="col-md-3">
                <div class="card-box">
                    <h6>Báo cáo doanh thu</h6>
                </div>
            </div>

            <div class="col-md-3">
                <div class="card-box">
                    <h6>Báo cáo công nợ</h6>
                </div>
            </div>

            <div class="col-md-3">
                <div class="card-box">
                    <h6>Báo cáo chi thu</h6>
                </div>
            </div>

            <div class="col-md-3">
                <div class="card-box">
                    <h6>Báo cáo thanh toán</h6>
                </div>
            </div>

        </div>

        <div class="box">

            <div class="row mb-4">

                <div class="col-md-4">

                    <label>
                        Khoảng thời gian
                    </label>

                    <select class="form-select">

                        <option>Tháng này</option>
                        <option>Quý này</option>
                        <option>Năm nay</option>

                    </select>

                </div>

                <div class="col-md-2 d-flex align-items-end">

                    <button class="btn btn-primary">
                        Xem báo cáo
                    </button>

                </div>

            </div>

            <canvas id="chartReport"></canvas>

        </div>

    </div>

</div>

<script>

new Chart(
document.getElementById("chartReport"),
{
    type:"bar",

    data:{
        labels:[
        "T1","T2","T3","T4",
        "T5","T6"
        ],

        datasets:[{

            label:"Doanh thu",

            data:[
            50,100,80,120,90,150
            ]

        }]
    }
});

</script>

</body>
</html>