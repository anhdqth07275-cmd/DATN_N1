<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Debt"%>

<%
    Debt debt = (Debt) request.getAttribute("debt");
%>

<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">

    <title>Gia hạn công nợ</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
          rel="stylesheet">

    <style>

        body{
            background:#edf2f7;
            font-family:"Segoe UI",sans-serif;
        }

        .card-box{
            width:650px;
            margin:50px auto;
            background:#fff;
            padding:30px;
            border-radius:15px;
            box-shadow:0 5px 15px rgba(0,0,0,.08);
        }

        h3{
            font-weight:700;
            color:#0d6efd;
            margin-bottom:25px;
        }

        .info{
            background:#f8f9fa;
            padding:15px;
            border-radius:10px;
            margin-bottom:20px;
        }

        .info p{
            margin-bottom:8px;
        }

    </style>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dark-theme.css">
</head>

<body>

<div class="card-box">

    <h3>

        <i class="bi bi-calendar-event"></i>

        Gia hạn công nợ

    </h3>

    <div class="info">

        <p>

            <strong>Mã công nợ:</strong>

            <%=debt.getDebtCode()%>

        </p>

        <p>

            <strong>Khách hàng:</strong>

            <%=debt.getCustomerName()%>

        </p>

        <p>

            <strong>Mã hóa đơn:</strong>

            <%=debt.getInvoiceCode()%>

        </p>

        <p>

            <strong>Còn nợ:</strong>

            <span class="text-danger">

                <%=debt.getMoney()%> VNĐ

            </span>

        </p>

        <p>

            <strong>Hạn hiện tại:</strong>

            <%=debt.getDateVN()%>

        </p>

    </div>

    <form action="<%=request.getContextPath()%>/congno"
          method="post">

        <input
            type="hidden"
            name="action"
            value="updateDueDate">

        <input
            type="hidden"
            name="debtId"
            value="<%=debt.getDebtId()%>">

        <div class="mb-3">

            <label class="form-label">

                Hạn thanh toán mới

            </label>

            <input
                type="date"
                name="dueDate"
                class="form-control"
                required>

        </div>

        <div class="d-flex justify-content-between">

            <a href="<%=request.getContextPath()%>/congno"
               class="btn btn-secondary">

                <i class="bi bi-arrow-left"></i>

                Quay lại

            </a>

            <button
                type="submit"
                class="btn btn-primary">

                <i class="bi bi-check-circle"></i>

                Lưu gia hạn

            </button>

        </div>

    </form>

</div>

</body>

</html>