package controller;

import dao.CustomerDAO;
import dao.HoaDonDAO;
import dao.InvoiceDetailDAO;
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
import model.HoaDon;

@WebServlet(name = "HoaDonServlet", urlPatterns = {"/hoadon"})
public class HoaDonServlet extends HttpServlet {

    HoaDonDAO dao = new HoaDonDAO();
    PermissionDAO permissionDAO = new PermissionDAO();
    RequestDisableDAO requestDAO = new RequestDisableDAO();
    private static final String MODULE = "HOADON";

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

            case "view":
                viewHoaDon(request, response);
                break;

            case "delete":
                deleteHoaDon(request, response);
                break;

            case "softdelete":
                softDeleteHoaDon(request, response);
                break;

            case "restore":
                restoreHoaDon(request, response);
                break;

            case "harddelete":
                deleteHoaDon(request, response);
                break;

            case "requestdisable":
                requestDisableHoaDon(request, response);
                break;

            case "dismissrequest":
                dismissRequestHoaDon(request, response);
                break;

            case "search":
                searchHoaDon(request, response);
                break;

            default:
                listHoaDon(request, response);
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
                insertHoaDon(request, response);
                break;

            case "update":
                updateHoaDon(request, response);
                break;

            case "delete":
                deleteHoaDon(request, response);
                break;

            case "search":
                searchHoaDon(request, response);
                break;

        }

    }

    // ==========================
    // Danh sách
    // ==========================
    private void listHoaDon(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        boolean canSoftDelete = hasAction(request, "XOAMEM");
        boolean canHardDelete = hasAction(request, "XOACUNG");

        boolean showInactive = canSoftDelete
                && "1".equals(request.getParameter("showInactive"));

        ArrayList<HoaDon> list = dao.getAll(showInactive);

        HashSet<Integer> pendingIds = requestDAO.getPendingEntityIds(MODULE);

        request.setAttribute("listHoaDon", list);
        request.setAttribute("canSoftDelete", canSoftDelete);
        request.setAttribute("canHardDelete", canHardDelete);
        request.setAttribute("pendingIds", pendingIds);
        request.setAttribute("showInactive", showInactive);

        request.getRequestDispatcher("/view/qlhoadon.jsp")
                .forward(request, response);

    }

    // ==========================
    // Form thêm
    // ==========================
    private void showAddForm(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        CustomerDAO customerDAO = new CustomerDAO();

        request.setAttribute("listCustomer",
                customerDAO.getAll());

        request.getRequestDispatcher("/view/addHoaDon.jsp")
                .forward(request, response);

    }

    // ==========================
    // Form sửa
    // ==========================
    private void showEditForm(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        HoaDon hd = dao.getById(id);
        CustomerDAO customerDAO = new CustomerDAO();

        request.setAttribute("listCustomer",
                customerDAO.getAll());
        request.setAttribute("hoaDon", hd);

        request.getRequestDispatcher("/view/editHoaDon.jsp")
                .forward(request, response);

    }

    // ==========================
    // Chi tiết
    // ==========================
    private void viewHoaDon(HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

    int id = Integer.parseInt(request.getParameter("id"));

    HoaDon hd = dao.getById(id);

    InvoiceDetailDAO detailDAO = new InvoiceDetailDAO();

    request.setAttribute("hoaDon", hd);

    request.setAttribute(
            "listDetail",
            detailDAO.getByInvoiceId(id));

    request.getRequestDispatcher("/view/viewHoaDon.jsp")
            .forward(request, response);

}

    // ==========================
    // Thêm
    // ==========================
    private void insertHoaDon(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();

        DangKy user = (DangKy) session.getAttribute("user");

        HoaDon hd = new HoaDon();

        hd.setCustomerId(
                Integer.parseInt(request.getParameter("customerId"))
        );

        hd.setUserId(user.getUserId());

        hd.setTotalAmount(0);

        // Hóa đơn mới luôn bắt đầu ở trạng thái "Chưa thanh toán":
        // tổng tiền = 0 nên chưa có ý nghĩa gì để đánh dấu "đã
        // thanh toán" ngay khi tạo. Trạng thái sẽ tự chuyển sang
        // "Đã thanh toán" khi có đủ phiếu thu (xem DebtDAO).
        hd.setStatus("Chưa thanh toán");

        int invoiceId = dao.insert(hd);

        util.ActivityLogger.log(request, "THEM", "Hóa đơn",
                "Tạo hóa đơn mới #" + invoiceId);

        response.sendRedirect(
                request.getContextPath()
                + "/invoice-detail?action=add&id="
                + invoiceId);

    }

    // ==========================
    // Cập nhật
    // ==========================
    private void updateHoaDon(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        HoaDon hd = new HoaDon();

        hd.setInvoiceId(
                Integer.parseInt(request.getParameter("invoiceId"))
        );

        hd.setCustomerId(
                Integer.parseInt(request.getParameter("customerId"))
        );

        // Không set lại totalAmount/status ở đây nữa:
        // - total_amount luôn do tổng chi tiết hóa đơn quyết định.
        // - status luôn do số tiền đã thu quyết định (DebtDAO).
        // dao.update() bên dưới cũng chỉ còn cập nhật customer_id.
        dao.update(hd);

        util.ActivityLogger.log(request, "SUA", "Hóa đơn",
                "Cập nhật hóa đơn #" + hd.getInvoiceId());

        response.sendRedirect(request.getContextPath() + "/hoadon");

    }

    // ==========================
    // Kiểm tra quyền hành động chi tiết (THEM/SUA/XOAMEM/XOACUNG)
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
    // Xóa vĩnh viễn - chỉ Admin (quyền HOADON_XOACUNG)
    // ==========================
    private void deleteHoaDon(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        if (!hasAction(request, "XOACUNG")) {
            response.sendRedirect(request.getContextPath() + "/hoadon");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));

        // Hóa đơn đã có phiếu thu (đã phát sinh giao dịch tiền)
        // thì không cho xóa, để không làm mất dấu vết thu tiền/
        // công nợ đã ghi nhận trước đó.
        if (!dao.canDelete(id)) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/hoadon?error="
                    + java.net.URLEncoder.encode(
                            "Không thể xóa hóa đơn đã có phiếu thu. "
                            + "Vui lòng xóa các phiếu thu liên quan trước.",
                            "UTF-8"));

            return;

        }

        boolean ok = dao.hardDelete(id);

        if (!ok) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/hoadon?error="
                    + java.net.URLEncoder.encode(
                            "Xóa hóa đơn thất bại. Vui lòng thử lại.",
                            "UTF-8"));

            return;

        }

        util.ActivityLogger.log(request, "XOA", "Hóa đơn",
                "Xóa vĩnh viễn hóa đơn #" + id);

        response.sendRedirect(request.getContextPath() + "/hoadon");

    }

    // ==========================
    // Xóa mềm (vô hiệu hóa) - Admin/Giám đốc (quyền HOADON_XOAMEM)
    // ==========================
    private void softDeleteHoaDon(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        if (!hasAction(request, "XOAMEM")) {
            response.sendRedirect(request.getContextPath() + "/hoadon");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));

        dao.softDelete(id);

        requestDAO.resolve(MODULE, id, currentUserId(request), "Approved");

        util.ActivityLogger.log(request, "XOA", "Hóa đơn",
                "Vô hiệu hóa hóa đơn #" + id);

        response.sendRedirect(request.getContextPath() + "/hoadon");

    }

    // ==========================
    // Khôi phục hóa đơn đã bị vô hiệu hóa
    // ==========================
    private void restoreHoaDon(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        if (!hasAction(request, "XOAMEM")) {
            response.sendRedirect(request.getContextPath() + "/hoadon");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));

        dao.restore(id);

        util.ActivityLogger.log(request, "SUA", "Hóa đơn",
                "Khôi phục hóa đơn #" + id);

        response.sendRedirect(request.getContextPath() + "/hoadon");

    }

    // ==========================
    // Nhân viên "Xin quyền vô hiệu hóa"
    // ==========================
    private void requestDisableHoaDon(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        HoaDon hd = dao.getById(id);

        String label = hd != null
                ? ("HD" + String.format("%04d", hd.getInvoiceId()))
                : ("#" + id);

        requestDAO.create(MODULE, id, label, currentUserId(request));

        util.ActivityLogger.log(request, "SUA", "Hóa đơn",
                "Đề xuất vô hiệu hóa hóa đơn " + label);

        response.sendRedirect(request.getContextPath() + "/hoadon");

    }

    // ==========================
    // Admin/Giám đốc bỏ qua đề xuất
    // ==========================
    private void dismissRequestHoaDon(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        if (!hasAction(request, "XOAMEM")) {
            response.sendRedirect(request.getContextPath() + "/hoadon");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));

        requestDAO.resolve(MODULE, id, currentUserId(request), "Rejected");

        response.sendRedirect(request.getContextPath() + "/hoadon");

    }

    // ==========================
    // Tìm kiếm
    // ==========================
    private void searchHoaDon(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");

        ArrayList<HoaDon> list = dao.search(keyword);

        HashSet<Integer> pendingIds = requestDAO.getPendingEntityIds(MODULE);

        request.setAttribute("listHoaDon", list);
        request.setAttribute("canSoftDelete", hasAction(request, "XOAMEM"));
        request.setAttribute("canHardDelete", hasAction(request, "XOACUNG"));
        request.setAttribute("pendingIds", pendingIds);
        request.setAttribute("showInactive", false);

        request.getRequestDispatcher("/view/qlhoadon.jsp")
                .forward(request, response);

    }

    @Override
    public String getServletInfo() {
        return "HoaDon Servlet";
    }

}
