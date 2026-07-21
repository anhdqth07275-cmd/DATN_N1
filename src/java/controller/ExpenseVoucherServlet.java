package controller;

import dao.ExpenseVoucherDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import model.DangKy;
import model.ExpenseVoucher;

@WebServlet(name = "ExpenseVoucherServlet", urlPatterns = {"/phieuchi"})
public class ExpenseVoucherServlet extends HttpServlet {

    ExpenseVoucherDAO dao = new ExpenseVoucherDAO();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {

            case "add":
                showAddForm(request, response);
                break;

            case "edit":
                showEditForm(request, response);
                break;

            case "delete":
                deleteExpense(request, response);
                break;

            case "search":
                searchExpense(request, response);
                break;

            default:
                listExpense(request, response);
                break;

        }

    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if (action == null) {
            action = "";
        }

        switch (action) {

            case "insert":
                insertExpense(request, response);
                break;

            case "update":
                updateExpense(request, response);
                break;

            case "search":
                searchExpense(request, response);
                break;

            case "delete":
                deleteExpense(request, response);
                break;

        }

    }

    // ==========================
    // Danh sách
    // ==========================
    private void listExpense(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        ArrayList<ExpenseVoucher> list =
                dao.getAll();

        request.setAttribute("listExpense", list);

        request.getRequestDispatcher("/view/phieuchi.jsp")
                .forward(request, response);

    }

    // ==========================
    // Form thêm
    // ==========================
    private void showAddForm(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/view/addExpense.jsp")
                .forward(request, response);

    }

    // ==========================
    // Form sửa
    // ==========================
    private void showEditForm(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        ExpenseVoucher expense =
                dao.getById(id);

        request.setAttribute("expense", expense);

        request.getRequestDispatcher("/view/editExpense.jsp")
                .forward(request, response);

    }
        // ==========================
    // Thêm phiếu chi
    // ==========================
    private void insertExpense(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();

        DangKy user =
                (DangKy) session.getAttribute("user");

        ExpenseVoucher expense =
                new ExpenseVoucher();

        expense.setUserId(user.getUserId());

        expense.setExpenseName(
                request.getParameter("expenseName"));

        expense.setAmount(
                Double.parseDouble(
                        request.getParameter("amount")));

        expense.setDescription(
                request.getParameter("description"));

        dao.insert(expense);

        util.ActivityLogger.log(request, "CHI", "Phiếu chi",
                "Lập phiếu chi \"" + expense.getExpenseName() + "\"",
                expense.getAmount());

        response.sendRedirect(
                request.getContextPath()
                + "/phieuchi");

    }

    // ==========================
    // Cập nhật phiếu chi
    // ==========================
    private void updateExpense(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        ExpenseVoucher expense =
                new ExpenseVoucher();

        expense.setExpenseId(
                Integer.parseInt(
                        request.getParameter("expenseId")));

        expense.setExpenseName(
                request.getParameter("expenseName"));

        expense.setAmount(
                Double.parseDouble(
                        request.getParameter("amount")));

        expense.setDescription(
                request.getParameter("description"));

        dao.update(expense);

        util.ActivityLogger.log(request, "SUA", "Phiếu chi",
                "Cập nhật phiếu chi #" + expense.getExpenseId(),
                expense.getAmount());

        response.sendRedirect(
                request.getContextPath()
                + "/phieuchi");

    }
        // ==========================
    // Xóa phiếu chi
    // ==========================
    private void deleteExpense(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(
                request.getParameter("id"));

        dao.delete(id);

        util.ActivityLogger.log(request, "XOA", "Phiếu chi",
                "Xóa phiếu chi #" + id);

        response.sendRedirect(
                request.getContextPath()
                + "/phieuchi");

    }

    // ==========================
    // Tìm kiếm phiếu chi
    // ==========================
    private void searchExpense(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String keyword =
                request.getParameter("keyword");

        ArrayList<ExpenseVoucher> list =
                dao.search(keyword);

        request.setAttribute(
                "listExpense",
                list);

        request.getRequestDispatcher(
                "/view/phieuchi.jsp")
                .forward(request, response);

    }

    @Override
    public String getServletInfo() {

        return "Expense Voucher Servlet";

    }

}