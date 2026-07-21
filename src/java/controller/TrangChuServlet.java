package controller;

import dao.DashboardDAO;
import dao.DebtDAO;
import dao.HoaDonDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.Dashboard;

@WebServlet(name = "TrangChuServlet", urlPatterns = {"/trangchu"})
public class TrangChuServlet extends HttpServlet {

    DashboardDAO dashboardDAO = new DashboardDAO();
    HoaDonDAO hoaDonDAO = new HoaDonDAO();
    DebtDAO debtDAO = new DebtDAO();

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

        Dashboard dashboard =
                dashboardDAO.getDashboard();

        request.setAttribute(
                "dashboard",
                dashboard);

        request.setAttribute(
                "listInvoice",
                hoaDonDAO.getTop5Newest());

        request.setAttribute(
                "listDebt",
                debtDAO.getTop5Overdue());

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