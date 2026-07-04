<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.HoaDon"%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>-Danh sách hóa đơn-</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

<style>

body{
    margin:0;
    background:#eef2f7;
    font-family:Arial;
}

.wrapper{
    display:flex;
    width:100%;
}

.content{
    flex:1;
    padding:35px;
}

.box{
    background:white;
    border-radius:18px;
    padding:30px;
    box-shadow:0 5px 20px rgba(0,0,0,.08);
}

.title{
    text-align:center;
    font-size:34px;
    font-weight:bold;
    margin-bottom:35px;
}

.top{
    display:flex;
    justify-content:space-between;
    margin-bottom:25px;
}

.search{
    width:420px;
}

table th{
    background:#212529;
    color:white;
    text-align:center;
}

table td{
    text-align:center;
    vertical-align:middle;
}

.pagination{
    justify-content:center;
    margin-top:20px;
}

</style>

</head>

<body>

<div class="wrapper">

<jsp:include page="menu.jsp"/>

<div class="content">

<div class="box">

<h1 class="title">
DANH SÁCH HÓA ĐƠN
</h1>

<div class="top">

<a class="btn btn-primary btn-lg"
href="${pageContext.request.contextPath}/hoadon?action=add">

+ Thêm hóa đơn

</a>

<form action="${pageContext.request.contextPath}/hoadon">

<div class="input-group search">

<input
class="form-control"
name="id"
placeholder="Nhập mã hóa đơn...">

<button class="btn btn-primary">

Tìm kiếm

</button>

</div>

</form>

</div>

<table class="table table-bordered table-hover">

<tr>

<th>Mã HĐ</th>
<th>Mã KH</th>
<th>Mã User</th>
<th>Ngày lập</th>
<th>Tổng tiền</th>
<th>Trạng thái</th>
<th>Chi tiết</th>

</tr>

<%

ArrayList<HoaDon> list=(ArrayList<HoaDon>)request.getAttribute("list");

if(list!=null){

for(HoaDon hd:list){

%>

<tr>

<td>HD<%=hd.getInvoiceId()%></td>

<td>KH<%=hd.getCustomerId()%></td>

<td>NV<%=hd.getUserId()%></td>

<td><%=hd.getInvoiceDate()%></td>

<td><%=hd.getTotalAmount()%></td>

<td><%=hd.getStatus()%></td>

<td>

<a class="btn btn-success btn-sm"

href="${pageContext.request.contextPath}/hoadon?action=detail&id=<%=hd.getInvoiceId()%>">

Xem

</a>

</td>

</tr>

<%

}

}

%>

</table>

<nav>

<ul class="pagination">

<li class="page-item active">
<a class="page-link">1</a>
</li>

<li class="page-item">
<a class="page-link">2</a>
</li>

<li class="page-item">
<a class="page-link">3</a>
</li>

</ul>

</nav>

</div>

</div>

</div>

</body>

</html>