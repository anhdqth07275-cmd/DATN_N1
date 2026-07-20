package controller;

import dao.HoaDonDAO;
import dao.ReceiptDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import model.DangKy;
import model.HoaDon;
import model.Receipt;

@WebServlet(name = "ReceiptServlet", urlPatterns = {"/phieuthu"})
public class ReceiptServlet extends HttpServlet {

    ReceiptDAO dao = new ReceiptDAO();

    HoaDonDAO hoaDonDAO = new HoaDonDAO();

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

                deleteReceipt(request, response);

                break;

            case "search":

                searchReceipt(request, response);

                break;

            default:

                listReceipt(request, response);

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

                insertReceipt(request, response);

                break;

            case "update":

                updateReceipt(request, response);

                break;

            case "search":

                searchReceipt(request, response);

                break;

        }

    }
    // ==========================
    // Danh sách phiếu thu
    // ==========================

    private void listReceipt(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        ArrayList<Receipt> list = dao.getAll();

        request.setAttribute("listReceipt", list);

        request.getRequestDispatcher("/view/phieuthu.jsp")
                .forward(request, response);

    }
    // ==========================
    // Form thêm
    // ==========================

    private void showAddForm(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        ArrayList<HoaDon> listHoaDon
                = hoaDonDAO.getUnpaidInvoice();

        request.setAttribute(
                "listHoaDon",
                listHoaDon);

        request.getRequestDispatcher(
                "/view/addReceipt.jsp")
                .forward(request, response);

    }
    // ==========================
    // Form sửa
    // ==========================

    private void showEditForm(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int id
                = Integer.parseInt(
                        request.getParameter("id"));

        Receipt receipt
                = dao.getById(id);

        ArrayList<HoaDon> listHoaDon
                = hoaDonDAO.getUnpaidInvoice();

        request.setAttribute(
                "receipt",
                receipt);

        request.setAttribute(
                "listHoaDon",
                listHoaDon);

        request.getRequestDispatcher(
                "/view/editReceipt.jsp")
                .forward(request, response);

    }
    // ==========================
// Thêm phiếu thu
// ==========================

    private void insertReceipt(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();

        DangKy user
                = (DangKy) session.getAttribute("user");

        Receipt r = new Receipt();

        r.setInvoiceId(
                Integer.parseInt(
                        request.getParameter("invoiceId")));

        r.setUserId(
                user.getUserId());

        r.setAmount(
                Double.parseDouble(
                        request.getParameter("amount")));

        r.setPaymentMethod(
                request.getParameter("paymentMethod"));

        r.setNote(
                request.getParameter("note"));

        dao.insert(r);

        response.sendRedirect(
                request.getContextPath()
                + "/phieuthu");

    }
// ==========================
// Cập nhật phiếu thu
// ==========================

    private void updateReceipt(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        Receipt r = new Receipt();

        r.setReceiptId(
                Integer.parseInt(
                        request.getParameter("receiptId")));

        r.setInvoiceId(
                Integer.parseInt(
                        request.getParameter("invoiceId")));

        r.setAmount(
                Double.parseDouble(
                        request.getParameter("amount")));

        r.setPaymentMethod(
                request.getParameter("paymentMethod"));

        r.setNote(
                request.getParameter("note"));

        dao.update(r);

        response.sendRedirect(
                request.getContextPath()
                + "/phieuthu");

    }
// ==========================
// Xóa phiếu thu
// ==========================

    private void deleteReceipt(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        int id
                = Integer.parseInt(
                        request.getParameter("id"));

        dao.delete(id);

        response.sendRedirect(
                request.getContextPath()
                + "/phieuthu");

    }
// ==========================
// Tìm kiếm
// ==========================

    private void searchReceipt(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String keyword
                = request.getParameter("keyword");

        ArrayList<Receipt> list
                = dao.search(keyword);

        request.setAttribute(
                "listReceipt",
                list);

        request.getRequestDispatcher(
                "/view/phieuthu.jsp")
                .forward(request, response);

    }

    @Override
    public String getServletInfo() {

        return "Receipt Servlet";

    }
}
