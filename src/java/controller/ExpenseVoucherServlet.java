package controller;

import dao.ExpenseVoucherDAO;
import dao.PermissionDAO;
import dao.RequestDisableDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import model.DangKy;
import model.ExpenseVoucher;

@WebServlet(name = "ExpenseVoucherServlet", urlPatterns = {"/phieuchi"})
public class ExpenseVoucherServlet extends HttpServlet {

    ExpenseVoucherDAO dao = new ExpenseVoucherDAO();
    PermissionDAO permissionDAO = new PermissionDAO();
    RequestDisableDAO requestDAO = new RequestDisableDAO();
    private static final String MODULE = "PHIEUCHI";

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

            case "softdelete":
                softDeleteExpense(request, response);
                break;

            case "restore":
                restoreExpense(request, response);
                break;

            case "requestdisable":
                requestDisableExpense(request, response);
                break;

            case "dismissrequest":
                dismissRequestExpense(request, response);
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

        }

    }

    // ==========================
    // Danh sách
    // ==========================
    private void listExpense(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        boolean canSoftDelete = hasAction(request, "XOAMEM");
        boolean canAdd = hasAction(request, "THEM");
        boolean canEdit = hasAction(request, "SUA");

        boolean showInactive = canSoftDelete
                && "1".equals(request.getParameter("showInactive"));

        ArrayList<ExpenseVoucher> list =
                dao.getAll(showInactive);

        HashSet<Integer> pendingIds = requestDAO.getPendingEntityIds(MODULE);

        request.setAttribute("listExpense", list);
        request.setAttribute("canSoftDelete", canSoftDelete);
        request.setAttribute("canAdd", canAdd);
        request.setAttribute("canEdit", canEdit);
        request.setAttribute("pendingIds", pendingIds);
        request.setAttribute("showInactive", showInactive);

        request.getRequestDispatcher("/view/phieuchi.jsp")
                .forward(request, response);

    }

    // ==========================
    // Kiểm tra quyền hành động chi tiết (THEM/SUA/XOAMEM)
    // ==========================
    private boolean hasAction(HttpServletRequest request, String actionCode) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            return false;
        }

        DangKy user = (DangKy) session.getAttribute("user");

        if (user == null) {
            return false;
        }

        return permissionDAO.hasPermission(
                user.getRoleId(), MODULE + "_" + actionCode);

    }

    private int currentUserId(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        DangKy user = session == null ? null
                : (DangKy) session.getAttribute("user");

        return user == null ? 0 : user.getUserId();

    }

    // ==========================
    // Form thêm
    // ==========================
    private void showAddForm(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!hasAction(request, "THEM")) {
            response.sendRedirect(request.getContextPath() + "/phieuchi");
            return;
        }

        request.getRequestDispatcher("/view/addExpense.jsp")
                .forward(request, response);

    }

    // ==========================
    // Form sửa
    // ==========================
    private void showEditForm(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!hasAction(request, "SUA")) {
            response.sendRedirect(request.getContextPath() + "/phieuchi");
            return;
        }

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

        if (!hasAction(request, "THEM")) {
            response.sendRedirect(request.getContextPath() + "/phieuchi");
            return;
        }

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

        if (!hasAction(request, "SUA")) {
            response.sendRedirect(request.getContextPath() + "/phieuchi");
            return;
        }

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
    // Xóa mềm (vô hiệu hóa) - Admin/Giám đốc
    // ==========================
    private void softDeleteExpense(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        if (!hasAction(request, "XOAMEM")) {
            response.sendRedirect(request.getContextPath() + "/phieuchi");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));

        dao.softDelete(id);

        requestDAO.resolve(MODULE, id, currentUserId(request), "Approved");

        util.ActivityLogger.log(request, "XOA", "Phiếu chi",
                "Vô hiệu hóa phiếu chi #" + id);

        response.sendRedirect(request.getContextPath() + "/phieuchi");

    }

    // ==========================
    // Khôi phục phiếu chi đã bị vô hiệu hóa
    // ==========================
    private void restoreExpense(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        if (!hasAction(request, "XOAMEM")) {
            response.sendRedirect(request.getContextPath() + "/phieuchi");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));

        dao.restore(id);

        util.ActivityLogger.log(request, "SUA", "Phiếu chi",
                "Khôi phục phiếu chi #" + id);

        response.sendRedirect(request.getContextPath() + "/phieuchi");

    }

    // ==========================
    // Nhân viên "Xin quyền vô hiệu hóa"
    // ==========================
    private void requestDisableExpense(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        ExpenseVoucher e = dao.getById(id);

        String label = e != null ? e.getExpenseName() : ("#" + id);

        requestDAO.create(MODULE, id, label, currentUserId(request));

        util.ActivityLogger.log(request, "SUA", "Phiếu chi",
                "Đề xuất vô hiệu hóa phiếu chi \"" + label + "\"");

        response.sendRedirect(request.getContextPath() + "/phieuchi");

    }

    // ==========================
    // Admin/Giám đốc bỏ qua đề xuất
    // ==========================
    private void dismissRequestExpense(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        if (!hasAction(request, "XOAMEM")) {
            response.sendRedirect(request.getContextPath() + "/phieuchi");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));

        requestDAO.resolve(MODULE, id, currentUserId(request), "Rejected");

        response.sendRedirect(request.getContextPath() + "/phieuchi");

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

        HashSet<Integer> pendingIds = requestDAO.getPendingEntityIds(MODULE);

        request.setAttribute(
                "listExpense",
                list);
        request.setAttribute("canSoftDelete", hasAction(request, "XOAMEM"));
        request.setAttribute("pendingIds", pendingIds);
        request.setAttribute("showInactive", false);

        request.getRequestDispatcher(
                "/view/phieuchi.jsp")
                .forward(request, response);

    }

    @Override
    public String getServletInfo() {

        return "Expense Voucher Servlet";

    }

}