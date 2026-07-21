package controller;

import dao.DebtDAO;
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

    DebtDAO debtDAO = new DebtDAO();

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

        HoaDon invoice
                = hoaDonDAO.getById(receipt.getInvoiceId());

        // Số tiền đã thu của TẤT CẢ các phiếu thu khác (không tính
        // phiếu đang sửa), để tính đúng hạn mức tối đa có thể sửa
        // thành, thay vì lấy nhầm "còn nợ hiện tại" (vốn đã trừ đi
        // chính phiếu thu này rồi).
        double paidByOthers
                = debtDAO.getTotalPaid(receipt.getInvoiceId(), id);

        double maxAmount
                = invoice.getTotalAmount() - paidByOthers;

        if (maxAmount < 0) {
            maxAmount = 0;
        }

        request.setAttribute(
                "receipt",
                receipt);

        request.setAttribute(
                "invoice",
                invoice);

        request.setAttribute(
                "maxAmount",
                maxAmount);

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

        int invoiceId
                = Integer.parseInt(
                        request.getParameter("invoiceId"));

        double amount
                = Double.parseDouble(
                        request.getParameter("amount"));

        // Kiểm tra lại ở server, không chỉ dựa vào JS phía client
        // (JS có thể bị vô hiệu hóa hoặc bị lách qua).
        double remaining = debtDAO.getRemainingAmount(invoiceId);

        if (amount <= 0 || amount > remaining) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/phieuthu?action=add&error="
                    + java.net.URLEncoder.encode(
                            "Số tiền thu phải lớn hơn 0 và không "
                            + "được vượt quá số tiền còn nợ ("
                            + String.format("%,.0f", remaining)
                            + " đ).",
                            "UTF-8"));

            return;

        }

        Receipt r = new Receipt();

        r.setInvoiceId(invoiceId);

        r.setUserId(
                user.getUserId());

        r.setAmount(amount);

        r.setPaymentMethod(
                request.getParameter("paymentMethod"));

        r.setNote(
                request.getParameter("note"));

        dao.insert(r);

        // Đồng bộ lại công nợ + trạng thái hóa đơn ngay sau khi
        // ghi nhận phiếu thu, để tránh mâu thuẫn giữa 3 màn hình
        // hóa đơn / công nợ / thu tiền.
        debtDAO.updateFromInvoice(invoiceId);

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

        int receiptId
                = Integer.parseInt(
                        request.getParameter("receiptId"));

        int invoiceId
                = Integer.parseInt(
                        request.getParameter("invoiceId"));

        double amount
                = Double.parseDouble(
                        request.getParameter("amount"));

        HoaDon invoice = hoaDonDAO.getById(invoiceId);

        double paidByOthers
                = debtDAO.getTotalPaid(invoiceId, receiptId);

        double maxAmount = invoice.getTotalAmount() - paidByOthers;

        if (amount <= 0 || amount > maxAmount) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/phieuthu?action=edit&id=" + receiptId
                    + "&error="
                    + java.net.URLEncoder.encode(
                            "Số tiền thu phải lớn hơn 0 và không "
                            + "được vượt quá "
                            + String.format("%,.0f", maxAmount)
                            + " đ.",
                            "UTF-8"));

            return;

        }

        Receipt r = new Receipt();

        r.setReceiptId(receiptId);

        r.setInvoiceId(invoiceId);

        r.setAmount(amount);

        r.setPaymentMethod(
                request.getParameter("paymentMethod"));

        r.setNote(
                request.getParameter("note"));

        dao.update(r);

        // Đồng bộ lại công nợ + trạng thái hóa đơn sau khi sửa số
        // tiền của phiếu thu.
        debtDAO.updateFromInvoice(invoiceId);

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

        // Lấy invoiceId TRƯỚC khi xóa để còn đồng bộ lại công nợ/
        // trạng thái hóa đơn (hủy phiếu thu thì nợ phải tăng trở
        // lại tương ứng).
        Receipt r = dao.getById(id);

        dao.delete(id);

        if (r != null) {
            debtDAO.updateFromInvoice(r.getInvoiceId());
        }

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
