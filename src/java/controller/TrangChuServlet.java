package controller;

import dao.DashboardDAO;
import dao.DebtDAO;
import dao.HoaDonDAO;
import dao.PermissionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.DangKy;
import model.Dashboard;

@WebServlet(name = "TrangChuServlet", urlPatterns = {"/trangchu"})
public class TrangChuServlet extends HttpServlet {

    DashboardDAO dashboardDAO = new DashboardDAO();
    HoaDonDAO hoaDonDAO = new HoaDonDAO();
    DebtDAO debtDAO = new DebtDAO();
    PermissionDAO permissionDAO = new PermissionDAO();

    // Số ngày trước hạn để tính là "sắp đến hạn"
    private static final int DUE_SOON_DAYS = 3;

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/dangnhap");

            return;

        }

        DangKy user = (DangKy) session.getAttribute("user");

        boolean seeAll = permissionDAO.hasPermission(
                user.getRoleId(), "CONGNO_XEMTATCA");

        Integer scopeUserId = seeAll ? null : user.getUserId();

        Dashboard dashboard =
                dashboardDAO.getDashboard();

        request.setAttribute(
                "dashboard",
                dashboard);

        request.setAttribute(
                "listInvoice",
                hoaDonDAO.getTop5Newest());

        // Cảnh báo công nợ - chỉ của hóa đơn do chính nhân viên này
        // lập, trừ khi có quyền CONGNO_XEMTATCA (Admin/Giám đốc).
        request.setAttribute(
                "listDueSoon",
                debtDAO.getDueSoon(DUE_SOON_DAYS, scopeUserId, 5));

        request.setAttribute(
                "listOverdue",
                debtDAO.getOverdue(scopeUserId, 5));

        request.getRequestDispatcher("/view/trangchu.jsp")
                .forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);

    }

    @Override
    public String getServletInfo() {

        return "Trang Chu Servlet";

    }

}
