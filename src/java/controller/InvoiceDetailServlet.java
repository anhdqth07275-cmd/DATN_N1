package controller;

import dao.DebtDAO;
import dao.InvoiceDetailDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import model.InvoiceDetail;

@WebServlet(name = "InvoiceDetailServlet", urlPatterns = {"/invoice-detail"})
public class InvoiceDetailServlet extends HttpServlet {

    InvoiceDetailDAO dao = new InvoiceDetailDAO();
    DebtDAO debtDAO = new DebtDAO();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            action = "add";
        }

        switch (action) {

            case "add":
                showAdd(request, response);
                break;

            case "edit":
                showEdit(request, response);
                break;

            case "delete":
                deleteDetail(request, response);
                break;

            default:
                showAdd(request, response);
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
                insertDetail(request, response);
                break;

            case "update":
                updateDetail(request, response);
                break;

        }

    }

    // ==========================
    // Hiển thị form thêm
    // ==========================
    private void showAdd(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int invoiceId
                = Integer.parseInt(request.getParameter("id"));

        ArrayList<InvoiceDetail> list
                = dao.getByInvoiceId(invoiceId);

        request.setAttribute("invoiceId", invoiceId);

        request.setAttribute("listDetail", list);

        request.getRequestDispatcher("/view/addInvoiceDetail.jsp")
                .forward(request, response);

    }

    // ==========================
    // Hiển thị form sửa
    // ==========================
    private void showEdit(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int detailId
                = Integer.parseInt(request.getParameter("detailId"));

        InvoiceDetail detail
                = dao.getById(detailId);

        request.setAttribute("detail", detail);

        request.getRequestDispatcher("/view/editInvoiceDetail.jsp")
                .forward(request, response);

    }
    // ==========================
// Thêm chi tiết hóa đơn
// ==========================

    private void insertDetail(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        InvoiceDetail d = new InvoiceDetail();

        d.setInvoiceId(
                Integer.parseInt(
                        request.getParameter("invoiceId")));

        d.setItemName(
                request.getParameter("itemName"));

        d.setUnit(
                request.getParameter("unit"));

        d.setQuantity(
                Integer.parseInt(
                        request.getParameter("quantity")));

        d.setUnitPrice(
                Double.parseDouble(
                        request.getParameter("unitPrice")));

        dao.insert(d);

        debtDAO.createFromInvoice(d.getInvoiceId());

        debtDAO.updateFromInvoice(d.getInvoiceId());

        response.sendRedirect(
                request.getContextPath()
                + "/invoice-detail?action=add&id="
                + d.getInvoiceId());

    }
// ==========================
// Cập nhật chi tiết hóa đơn
// ==========================

    private void updateDetail(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        InvoiceDetail d = new InvoiceDetail();

        d.setDetailId(
                Integer.parseInt(
                        request.getParameter("detailId")));

        d.setInvoiceId(
                Integer.parseInt(
                        request.getParameter("invoiceId")));

        d.setItemName(
                request.getParameter("itemName"));

        d.setUnit(
                request.getParameter("unit"));

        d.setQuantity(
                Integer.parseInt(
                        request.getParameter("quantity")));

        d.setUnitPrice(
                Double.parseDouble(
                        request.getParameter("unitPrice")));

        dao.update(d);

        // Đồng bộ công nợ
        debtDAO.createFromInvoice(d.getInvoiceId());
        debtDAO.updateFromInvoice(d.getInvoiceId());
        response.sendRedirect(
                request.getContextPath()
                + "/invoice-detail?action=add&id="
                + d.getInvoiceId());

    }
// ==========================
// Xóa chi tiết hóa đơn
// ==========================

    private void deleteDetail(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        int detailId
                = Integer.parseInt(
                        request.getParameter("detailId"));

        int invoiceId
                = Integer.parseInt(
                        request.getParameter("invoiceId"));

        dao.delete(detailId, invoiceId);

        // Đồng bộ công nợ
        debtDAO.createFromInvoice(invoiceId);
        debtDAO.updateFromInvoice(invoiceId);
        response.sendRedirect(
                request.getContextPath()
                + "/invoice-detail?action=add&id="
                + invoiceId);

    }
}
