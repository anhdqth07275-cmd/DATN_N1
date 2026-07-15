<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.InvoiceDetail"%>

<%
int invoiceId=(Integer)request.getAttribute("invoiceId");

ArrayList<InvoiceDetail> list=
(ArrayList<InvoiceDetail>)request.getAttribute("listDetail");
%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Chi tiết hóa đơn</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
rel="stylesheet">

<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
rel="stylesheet">

<style>

body{

    background:#eef2f7;
    font-family:Segoe UI;

}

.container-box{

    width:1100px;

    margin:30px auto;

    background:white;

    border-radius:15px;

    padding:30px;

    box-shadow:0 5px 15px rgba(0,0,0,.08);

}

h2{

    text-align:center;

    margin-bottom:30px;

    font-weight:bold;

}

.table td{

    vertical-align:middle;

}

.table th{

    text-align:center;

}

.total{

    font-size:24px;

    color:red;

    font-weight:bold;

}

</style>

</head>

<body>

<div class="container-box">

<h2>

<i class="bi bi-receipt"></i>

CHI TIẾT HÓA ĐƠN

</h2>

<form

action="<%=request.getContextPath()%>/invoice-detail"

method="post">

<input
type="hidden"
name="action"
value="insert">

<input
type="hidden"
name="invoiceId"
value="<%=invoiceId%>">

<div class="row">

<div class="col-md-4">

<label>

Tên hàng

</label>

<input
name="itemName"
class="form-control"
required>

</div>

<div class="col-md-2">

<label>

ĐVT

</label>

<input
name="unit"
class="form-control"
required>

</div>

<div class="col-md-2">

<label>

Số lượng

</label>

<input
type="number"
min="1"
name="quantity"
class="form-control"
required>

</div>

<div class="col-md-2">

<label>

Đơn giá

</label>

<input
type="number"
min="0"
step="0.01"
name="unitPrice"
class="form-control"
required>

</div>

<div class="col-md-2 d-flex align-items-end">

<button

class="btn btn-primary w-100">

<i class="bi bi-plus-circle"></i>

Thêm

</button>

</div>

</div>

</form>

<hr>

<table

class="table table-bordered table-hover">

<thead class="table-primary">

<tr>

<th>STT</th>

<th>Tên hàng</th>

<th>ĐVT</th>

<th>SL</th>

<th>Đơn giá</th>

<th>Thành tiền</th>

<th width="130">

Thao tác

</th>

</tr>

</thead>

<tbody>

<%

double tong=0;

int stt=1;

if(list!=null){

for(InvoiceDetail d:list){

tong+=d.getSubtotal();

%>

<tr>

<td align="center">

<%=stt++%>

</td>

<td>

<%=d.getItemName()%>

</td>

<td>

<%=d.getUnit()%>

</td>

<td align="center">

<%=d.getQuantity()%>

</td>

<td align="right">

<%=String.format("%,.0f",d.getUnitPrice())%>

</td>

<td align="right">

<%=String.format("%,.0f",d.getSubtotal())%>

</td>

<td align="center">

<a

href="<%=request.getContextPath()%>/invoice-detail?action=edit&detailId=<%=d.getDetailId()%>"

class="btn btn-warning btn-sm">

<i class="bi bi-pencil-square"></i>

</a>

<a

href="<%=request.getContextPath()%>/invoice-detail?action=delete&detailId=<%=d.getDetailId()%>&invoiceId=<%=invoiceId%>"

class="btn btn-danger btn-sm"

onclick="return confirm('Xóa dòng này?')">

<i class="bi bi-trash"></i>

</a>

</td>

</tr>

<%

}

}

%>

</tbody>

</table>

<div class="row mt-4">

<div class="col-md-8">

</div>

<div class="col-md-4 text-end">

<div class="total">

Tổng:

<%=String.format("%,.0f",tong)%> VNĐ

</div>

</div>

</div>

<div class="text-center mt-4">

<a

href="<%=request.getContextPath()%>/hoadon"

class="btn btn-success">

<i class="bi bi-check-circle-fill"></i>

Hoàn tất hóa đơn

</a>

</div>

</div>

</body>

</html>