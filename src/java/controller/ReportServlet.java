package controller;

import dao.ReportDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.Report;

@WebServlet(name = "ReportServlet", urlPatterns = {"/baocao"})
public class ReportServlet extends HttpServlet {

    ReportDAO reportDAO = new ReportDAO();

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

        String range = request.getParameter("range");

        Report report = reportDAO.getReport(range);

        request.setAttribute("report", report);

        request.getRequestDispatcher("/view/baocao.jsp")
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

        return "Report Servlet";

    }

}
