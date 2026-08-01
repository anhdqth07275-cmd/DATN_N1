package controller;

import dao.DebtDAO;
import dao.HoaDonDAO;
import dao.PermissionDAO;
import dao.ReceiptDAO;
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
import model.HoaDon;
import model.Receipt;

@WebServlet(name = "ReceiptServlet", urlPatterns = {"/phieuthu"})
public class ReceiptServlet extends HttpServlet {

    ReceiptDAO dao = new ReceiptDAO();

    HoaDonDAO hoaDonDAO = new HoaDonDAO();

    DebtDAO debtDAO = new DebtDAO();

    PermissionDAO permissionDAO = new PermissionDAO();
    RequestDisableDAO requestDAO = new RequestDisableDAO();
    private static final String MODULE = "PHIEUTHU";

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

                softDeleteReceipt(request, response);

                break;

            case "restore":

                restoreReceipt(request, response);

                break;

            case "requestdisable":

                requestDisableReceipt(request, response);

                break;

            case "dismissrequest":

                dismissRequestReceipt(request, response);

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

        boolean canSoftDelete = hasAction(request, "XOAMEM");
        boolean canAdd = hasAction(request, "THEM");
        boolean canEdit = hasAction(request, "SUA");

        boolean showInactive = canSoftDelete
                && "1".equals(request.getParameter("showInactive"));

        ArrayList<Receipt> list = dao.getAll(showInactive);

        HashSet<Integer> pendingIds = requestDAO.getPendingEntityIds(MODULE);

        request.setAttribute("listReceipt", list);
        request.setAttribute("canSoftDelete", canSoftDelete);
        request.setAttribute("canAdd", canAdd);
        request.setAttribute("canEdit", canEdit);
        request.setAttribute("pendingIds", pendingIds);
        request.setAttribute("showInactive", showInactive);

        request.getRequestDispatcher("/view/phieuthu.jsp")
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
            response.sendRedirect(request.getContextPath() + "/phieuthu");
            return;
        }

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

        if (!hasAction(request, "SUA")) {
            response.sendRedirect(request.getContextPath() + "/phieuthu");
            return;
        }

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

        if (!hasAction(request, "THEM")) {
            response.sendRedirect(request.getContextPath() + "/phieuthu");
            return;
        }

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

        util.ActivityLogger.log(request, "THU", "Phiếu thu",
                "Lập phiếu thu cho hóa đơn #" + invoiceId, amount);

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

        if (!hasAction(request, "SUA")) {
            response.sendRedirect(request.getContextPath() + "/phieuthu");
            return;
        }

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

        util.ActivityLogger.log(request, "SUA", "Phiếu thu",
                "Cập nhật phiếu thu #" + receiptId, amount);

        response.sendRedirect(
                request.getContextPath()
                + "/phieuthu");

    }
    // ==========================
    // Xóa mềm (vô hiệu hóa) - Admin/Giám đốc. Đồng bộ lại công nợ vì
    // phiếu thu bị ẩn không còn được tính là "đã thu" nữa.
    // ==========================
    private void softDeleteReceipt(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        if (!hasAction(request, "XOAMEM")) {
            response.sendRedirect(request.getContextPath() + "/phieuthu");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));

        Receipt r = dao.getById(id);

        dao.softDelete(id);

        if (r != null) {
            debtDAO.updateFromInvoice(r.getInvoiceId());
        }

        requestDAO.resolve(MODULE, id, currentUserId(request), "Approved");

        util.ActivityLogger.log(request, "XOA", "Phiếu thu",
                "Vô hiệu hóa phiếu thu #" + id);

        response.sendRedirect(request.getContextPath() + "/phieuthu");

    }

    // ==========================
    // Khôi phục phiếu thu đã bị vô hiệu hóa - đồng bộ lại công nợ
    // ==========================
    private void restoreReceipt(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        if (!hasAction(request, "XOAMEM")) {
            response.sendRedirect(request.getContextPath() + "/phieuthu");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));

        Receipt r = dao.getById(id);

        dao.restore(id);

        if (r != null) {
            debtDAO.updateFromInvoice(r.getInvoiceId());
        }

        util.ActivityLogger.log(request, "SUA", "Phiếu thu",
                "Khôi phục phiếu thu #" + id);

        response.sendRedirect(request.getContextPath() + "/phieuthu");

    }

    // ==========================
    // Nhân viên "Xin quyền vô hiệu hóa"
    // ==========================
    private void requestDisableReceipt(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        requestDAO.create(MODULE, id, "Phiếu thu #" + id,
                currentUserId(request));

        util.ActivityLogger.log(request, "SUA", "Phiếu thu",
                "Đề xuất vô hiệu hóa phiếu thu #" + id);

        response.sendRedirect(request.getContextPath() + "/phieuthu");

    }

    // ==========================
    // Admin/Giám đốc bỏ qua đề xuất
    // ==========================
    private void dismissRequestReceipt(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        if (!hasAction(request, "XOAMEM")) {
            response.sendRedirect(request.getContextPath() + "/phieuthu");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));

        requestDAO.resolve(MODULE, id, currentUserId(request), "Rejected");

        response.sendRedirect(request.getContextPath() + "/phieuthu");

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

        HashSet<Integer> pendingIds = requestDAO.getPendingEntityIds(MODULE);

        request.setAttribute(
                "listReceipt",
                list);
        request.setAttribute("canSoftDelete", hasAction(request, "XOAMEM"));
        request.setAttribute("pendingIds", pendingIds);
        request.setAttribute("showInactive", false);

        request.getRequestDispatcher(
                "/view/phieuthu.jsp")
                .forward(request, response);

    }

    @Override
    public String getServletInfo() {

        return "Receipt Servlet";

    }
}
