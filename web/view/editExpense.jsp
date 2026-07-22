<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.ExpenseVoucher"%>

<%
    ExpenseVoucher expense =
            (ExpenseVoucher) request.getAttribute("expense");
%>

<!DOCTYPE html>

<html>

    <head>

        <meta charset="UTF-8">

        <title>Cập nhật phiếu chi</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
              rel="stylesheet">

        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
              rel="stylesheet">

        <style>

            body{

                background:#edf2f7;

                font-family:"Segoe UI",sans-serif;

            }

            .box{

                width:700px;

                margin:40px auto;

                background:#fff;

                padding:30px;

                border-radius:15px;

                box-shadow:0 5px 15px rgba(0,0,0,.08);

            }

            h3{

                color:#dc3545;

                font-weight:700;

                margin-bottom:25px;

            }

        </style>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dark-theme.css">
</head>

    <body>

        <div class="box">

            <h3>

                <i class="bi bi-pencil-square"></i>

                Cập nhật phiếu chi

            </h3>

            <form action="<%=request.getContextPath()%>/phieuchi"

                  method="post">

                <input
                    type="hidden"
                    name="action"
                    value="update">

                <input
                    type="hidden"
                    name="expenseId"
                    value="<%=expense.getExpenseId()%>">
                <!-- Tên khoản chi -->

                <div class="mb-3">

                    <label class="form-label">

                        Tên khoản chi

                    </label>

                    <input

                        type="text"

                        name="expenseName"

                        class="form-control"

                        value="<%=expense.getExpenseName()%>"

                        required>

                </div>


                <!-- Số tiền -->

                <div class="mb-3">

                    <label class="form-label">

                        Số tiền

                    </label>

                    <input

                        type="number"

                        id="amount"

                        name="amount"

                        class="form-control"

                        value="<%=expense.getAmount()%>"

                        min="1000"

                        step="1000"

                        required>

                </div>


                <!-- Mô tả -->

                <div class="mb-3">

                    <label class="form-label">

                        Mô tả

                    </label>

                    <textarea

                        name="description"

                        rows="4"

                        class="form-control"><%=expense.getDescription()==null?"":expense.getDescription()%></textarea>

                </div>
                <div class="d-flex justify-content-between">

                    <a href="<%=request.getContextPath()%>/phieuchi"
                       class="btn btn-secondary">

                        <i class="bi bi-arrow-left"></i>

                        Quay lại

                    </a>

                    <button
                        type="submit"
                        class="btn btn-danger">

                        <i class="bi bi-pencil-square"></i>

                        Cập nhật phiếu chi

                    </button>

                </div>

            </form>

        </div>

        <script>

            const form = document.querySelector("form");

            const expenseName =
                    document.querySelector("input[name='expenseName']");

            const amount =
                    document.getElementById("amount");

        // Không cho nhập số âm
            amount.addEventListener("input", function () {

                if (Number(this.value) < 0) {

                    this.value = 0;

                }

            });

        // Validate
            form.addEventListener("submit", function (e) {

                if (expenseName.value.trim() == "") {

                    alert("Vui lòng nhập tên khoản chi!");

                    expenseName.focus();

                    e.preventDefault();

                    return;

                }

                if (Number(amount.value) <= 0) {

                    alert("Số tiền phải lớn hơn 0!");

                    amount.focus();

                    e.preventDefault();

                    return;

                }

            });

        </script>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

    </body>

</html>