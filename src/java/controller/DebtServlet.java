package controller;

import dao.DebtDAO;
import dao.PermissionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import model.DangKy;
import model.Debt;

@WebServlet("/congno")
public class DebtServlet extends HttpServlet {

    DebtDAO dao = new DebtDAO();

    PermissionDAO permissionDAO = new PermissionDAO();

    private static final String MODULE = "CONGNO";

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {

            action = "list";

        }

        switch (action) {

            case "extend":

                showExtendForm(request, response);

                break;

            case "search":

                searchDebt(request, response);

                break;

            default:

                listDebt(request, response);

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

            case "updateDueDate":

                updateDueDate(request, response);

                break;

        }

    }

    // ==========================
    // Quyền "xem toàn bộ" (CONGNO_XEMTATCA) - nếu không có quyền
    // này thì chỉ được xem công nợ của hóa đơn do CHÍNH mình lập,
    // để cảnh báo/nhắc nợ luôn đúng người phụ trách.
    // Trả về null nếu được xem toàn bộ, ngược lại trả về userId
    // để DAO lọc.
    // ==========================
    private Integer scopeUserId(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            return -1; // chưa đăng nhập -> không thấy gì
        }

        DangKy user = (DangKy) session.getAttribute("user");

        if (user == null) {
            return -1;
        }

        boolean seeAll = permissionDAO.hasPermission(
                user.getRoleId(), MODULE + "_XEMTATCA");

        return seeAll ? null : user.getUserId();

    }

    // ==========================
    // Danh sách công nợ
    // ==========================
    private void listDebt(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        ArrayList<Debt> list = dao.getAll(scopeUserId(request));

        request.setAttribute("listDebt", list);

        request.getRequestDispatcher("/view/congno.jsp")
                .forward(request, response);

    }

    // ==========================
    // Tìm kiếm
    // ==========================
    private void searchDebt(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");

        ArrayList<Debt> list = dao.search(keyword, scopeUserId(request));

        request.setAttribute("listDebt", list);

        request.getRequestDispatcher("/view/congno.jsp")
                .forward(request, response);

    }

    // ==========================
    // Form gia hạn
    // ==========================
    private void showExtendForm(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int debtId =
                Integer.parseInt(request.getParameter("id"));

        Debt debt = dao.getById(debtId);

        request.setAttribute("debt", debt);

        request.getRequestDispatcher("/view/extendDebt.jsp")
                .forward(request, response);

    }

    // ==========================
    // Lưu ngày gia hạn
    // ==========================
    private void updateDueDate(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        int debtId =
                Integer.parseInt(request.getParameter("debtId"));

        Date dueDate =
                Date.valueOf(request.getParameter("dueDate"));

        dao.extendDebt(debtId, dueDate);

        response.sendRedirect(
                request.getContextPath() + "/congno");

    }

    @Override
    public String getServletInfo() {

        return "Debt Servlet";

    }

}
