package controller;

import dao.CustomerDAO;
import dao.HoaDonDAO;
import dao.InvoiceDetailDAO;
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

@WebServlet(name = "HoaDonServlet", urlPatterns = {"/hoadon"})
public class HoaDonServlet extends HttpServlet {

    HoaDonDAO dao = new HoaDonDAO();

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

        ArrayList<HoaDon> list = dao.getAll();

        request.setAttribute("listHoaDon", list);

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

        response.sendRedirect(request.getContextPath() + "/hoadon");

    }

    // ==========================
    // Xóa
    // ==========================
    private void deleteHoaDon(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

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

        boolean ok = dao.delete(id);

        if (!ok) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/hoadon?error="
                    + java.net.URLEncoder.encode(
                            "Xóa hóa đơn thất bại. Vui lòng thử lại.",
                            "UTF-8"));

            return;

        }

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

        request.setAttribute("listHoaDon", list);

        request.getRequestDispatcher("/view/qlhoadon.jsp")
                .forward(request, response);

    }

    @Override
    public String getServletInfo() {
        return "HoaDon Servlet";
    }

}
