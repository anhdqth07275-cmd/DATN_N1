<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashSet"%>
<%@page import="model.Receipt"%>
<%@taglib prefix="my" tagdir="/WEB-INF/tags" %>

<%
    ArrayList<Receipt> list =
            (ArrayList<Receipt>) request.getAttribute("listReceipt");

    Boolean canSoftDelete = (Boolean) request.getAttribute("canSoftDelete");
    Boolean canHardDelete = (Boolean) request.getAttribute("canHardDelete");
    Boolean showInactive = (Boolean) request.getAttribute("showInactive");
    HashSet<Integer> pendingIds = (HashSet<Integer>) request.getAttribute("pendingIds");
    if (canSoftDelete == null) canSoftDelete = false;
    if (canHardDelete == null) canHardDelete = false;
    if (showInactive == null) showInactive = false;
    if (pendingIds == null) pendingIds = new HashSet<>();
%>

<!DOCTYPE html>
<html>

    <head>

        <meta charset="UTF-8">

        <title>Quản lý phiếu thu</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
              rel="stylesheet">

        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
              rel="stylesheet">

        <style>

            *{
                margin:0;
                padding:0;
                box-sizing:border-box;
            }

            body{
                background:#edf2f7;
                font-family:"Segoe UI",sans-serif;
            }

            .wrapper{
                display:flex;
                min-height:100vh;
            }

            .content{
                flex:1;
                padding:30px;
            }

            .box{
                background:#fff;
                border-radius:15px;
                padding:30px;
                box-shadow:0 5px 15px rgba(0,0,0,.08);
            }

            .page-title{
                display:flex;
                justify-content:space-between;
                align-items:center;
                margin-bottom:25px;
            }

            .toolbar{
                display:flex;
                justify-content:space-between;
                margin-bottom:20px;
            }

        </style>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dark-theme.css">
</head>

    <body>

        <div class="wrapper">

            <jsp:include page="menu.jsp"/>

            <div class="content">

                <div class="box">

                    <div class="page-title">

                        <div>

                            <h2>

                                <i class="bi bi-cash-stack"></i>

                                Quản lý phiếu thu

                            </h2>

                            <p>

                                Quản lý các phiếu thu tiền khách hàng

                            </p>

                        </div>

                        <a href="<%=request.getContextPath()%>/phieuthu?action=add"
                           class="btn btn-primary">

                            <i class="bi bi-plus-circle"></i>

                            Thêm phiếu thu

                        </a>

                    </div>

                    <div class="toolbar">

                        <% if (canSoftDelete) { %>
                        <div class="form-check form-switch mb-0 me-3">
                            <input class="form-check-input" type="checkbox" id="showInactiveToggle"
                                   <%=showInactive ? "checked" : ""%>
                                   onchange="window.location.href='<%=request.getContextPath()%>/phieuthu?showInactive=' + (this.checked ? '1' : '0')">
                            <label class="form-check-label" for="showInactiveToggle" style="white-space:nowrap;">
                                Hiện cả đã vô hiệu hóa
                            </label>
                        </div>
                        <% } %>

                        <form action="<%=request.getContextPath()%>/phieuthu"
                              method="get"
                              class="d-flex">

                            <input
                                type="hidden"
                                name="action"
                                value="search">

                            <input
                                type="text"
                                name="keyword"
                                class="form-control me-2"
                                placeholder="Tên khách hàng hoặc mã phiếu">

                            <button
                                class="btn btn-success">

                                <i class="bi bi-search"></i>

                            </button>

                        </form>

                    </div>

                    <table class="table table-bordered table-hover">

                        <thead class="table-primary">

                            <tr>

                                <th width="90">Mã PT</th>

                                <th>Khách hàng</th>

                                <th width="120">Hóa đơn</th>

                                <th width="140">Ngày thu</th>

                                <th width="170">Số tiền</th>

                                <th width="140">Thanh toán</th>

                                <th>Ghi chú</th>

                                <th width="170">Người lập</th>

                                <th width="170">Thao tác</th>

                            </tr>

                        </thead>

                        <tbody>
                            <%

        if(list != null && !list.isEmpty()){

            for(Receipt r : list){

                            %>

                            <tr>

                                <td align="center">

                                    <%=r.getReceiptCode()%>

                                    <% if (!r.isActive()) { %>
                                    <br><span class="badge bg-secondary" style="font-size:10px;">Đã vô hiệu hóa</span>
                                    <% } %>

                                </td>

                                <td>

                                    <%=r.getCustomerName()%>

                                </td>

                                <td align="center">

                                    <%=r.getInvoiceCode()%>

                                </td>

                                <td align="center">

                                    <%=r.getDateVN()%>

                                </td>

                                <td align="right">

                                    <strong class="text-success">

                                        <%=r.getMoney()%> VNĐ

                                    </strong>

                                </td>

                                <td align="center">

                                    <%=r.getPaymentMethod()%>

                                </td>

                                <td>

                                    <%

                                        if(r.getNote()==null){

                                    %>

                                    -

                                    <%

                                        }else{

                                    %>

                                    <%=r.getNote()%>

                                    <%

                                        }

                                    %>

                                </td>

                                <td>

                                    <%=r.getUserName()%>

                                </td>

                                <td align="center">

                                    <a href="<%=request.getContextPath()%>/phieuthu?action=edit&id=<%=r.getReceiptId()%>"
                                       class="btn btn-warning btn-sm">

                                        <i class="bi bi-pencil-square"></i>

                                    </a>

                                    <my:deleteActions
                                        baseUrl="<%=request.getContextPath()%>/phieuthu"
                                        entityId="<%=r.getReceiptId()%>"
                                        entityLabel="<%=r.getReceiptCode()%>"
                                        isActive="<%=r.isActive()%>"
                                        canSoftDelete="<%=canSoftDelete%>"
                                        canHardDelete="<%=canHardDelete%>"
                                        pendingRequest="<%=pendingIds.contains(r.getReceiptId())%>" />

                                </td>

                            </tr>

                            <%

                                }

                            }else{

                            %>

                            <tr>

                                <td colspan="9"
                                    class="text-center p-4">

                                    <i class="bi bi-database-exclamation"></i>

                                    Chưa có phiếu thu.

                                </td>

                            </tr>

                            <%

                            }

                            %>
                        </tbody>

                    </table>

                </div>

            </div>

        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

    </body>

</html>