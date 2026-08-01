package controller;

import dao.RoleDAO;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import model.DangKy;
import model.Role;
import util.ActivityLogger;

/**
 * Quản lý Người dùng trong phần Hệ thống.
 * Chỉ Quản trị viên (QTV) mới được truy cập servlet này.
 */
@WebServlet(name = "UserServlet", urlPatterns = {"/nguoidung"})
public class UserServlet extends HttpServlet {

    UserDAO dao = new UserDAO();
    RoleDAO roleDAO = new RoleDAO();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!checkAdmin(request, response)) {
            return;
        }

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

            case "lock":
                changeStatus(request, response, false);
                break;

            case "unlock":
                changeStatus(request, response, true);
                break;

            case "resetpassword":
                resetPassword(request, response);
                break;

            case "search":
                searchUser(request, response);
                break;

            default:
                listUser(request, response);
                break;

        }

    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!checkAdmin(request, response)) {
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if (action == null) {
            action = "";
        }

        switch (action) {

            case "insert":
                insertUser(request, response);
                break;

            case "update":
                updateUser(request, response);
                break;

            case "search":
                searchUser(request, response);
                break;

        }

    }

    // ==========================
    // Chuẩn hóa & validate số điện thoại (phần nhập sau mã vùng +84).
    // - Cho phép để trống (không bắt buộc ở form quản lý người dùng).
    // - Nếu có nhập, bắt buộc đúng 9 hoặc 10 chữ số.
    // - Trả về số điện thoại đầy đủ kèm mã vùng để lưu CSDL, vd:
    //   "912345678" -> "+84912345678". Trả về null nếu để trống.
    // ==========================
    private String normalizePhone(String rawInput) {

        if (rawInput == null) {
            return null;
        }

        String digits = rawInput.trim();

        if (digits.isEmpty()) {
            return null;
        }

        if (!digits.matches("[0-9]{9,10}")) {

            throw new IllegalArgumentException(
                    "Số điện thoại không hợp lệ! Vui lòng nhập 9 hoặc "
                    + "10 chữ số sau mã vùng +84, hoặc để trống.");

        }

        return "+84" + digits;

    }

    // ==========================
    // Chỉ Quản trị viên mới được vào phần quản lý người dùng
    // ==========================
    private boolean checkAdmin(HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(false);

        DangKy user = (session == null)
                ? null : (DangKy) session.getAttribute("user");

        if (user == null) {

            response.sendRedirect(request.getContextPath() + "/dangnhap");

            return false;

        }

        if (!"Quản trị viên".equals(user.getRoleName())) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/trangchu?error="
                    + java.net.URLEncoder.encode(
                            "Bạn không có quyền truy cập chức năng này.",
                            "UTF-8"));

            return false;

        }

        return true;

    }

    private void listUser(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        ArrayList<DangKy> list = dao.getAll();

        request.setAttribute("listUser", list);

        request.getRequestDispatcher("/view/nguoidung.jsp")
                .forward(request, response);

    }

    private void searchUser(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");

        ArrayList<DangKy> list = dao.search(keyword);

        request.setAttribute("listUser", list);

        request.getRequestDispatcher("/view/nguoidung.jsp")
                .forward(request, response);

    }

    private void showAddForm(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        ArrayList<Role> listRole = roleDAO.getAllRoles();

        request.setAttribute("listRole", listRole);

        request.getRequestDispatcher("/view/addUser.jsp")
                .forward(request, response);

    }

    private void showEditForm(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        DangKy user = dao.getById(id);

        ArrayList<Role> listRole = roleDAO.getAllRoles();

        request.setAttribute("user", user);
        request.setAttribute("listRole", listRole);

        request.getRequestDispatcher("/view/editUser.jsp")
                .forward(request, response);

    }

    private void insertUser(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");

        if (dao.checkUsername(username)) {

            request.setAttribute("error", "Tên đăng nhập đã tồn tại!");

            showAddForm(request, response);

            return;

        }

        String phone;

        try {

            phone = normalizePhone(request.getParameter("phone"));

        } catch (IllegalArgumentException ex) {

            request.setAttribute("error", ex.getMessage());

            showAddForm(request, response);

            return;

        }

        DangKy u = new DangKy();

        u.setUsername(username);
        u.setPassword(request.getParameter("password"));
        u.setFullName(request.getParameter("fullName"));
        u.setEmail(request.getParameter("email"));
        u.setPhone(phone);
        u.setRoleId(Integer.parseInt(request.getParameter("roleId")));
        u.setStatus(true);

        if (dao.insert(u)) {

            ActivityLogger.log(request, "THEM", "Người dùng",
                    "Thêm người dùng mới \"" + username + "\"");

            response.sendRedirect(request.getContextPath() + "/nguoidung");

        } else {

            request.setAttribute("error", "Thêm người dùng thất bại!");

            showAddForm(request, response);

        }

    }

    private void updateUser(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int userId = Integer.parseInt(request.getParameter("userId"));

        String phone;

        try {

            phone = normalizePhone(request.getParameter("phone"));

        } catch (IllegalArgumentException ex) {

            request.setAttribute("error", ex.getMessage());

            // Nạp lại đúng người dùng + danh sách vai trò để hiển thị lại
            // form sửa kèm thông báo lỗi, thay vì mất hết dữ liệu đang sửa.
            DangKy userToEdit = dao.getById(userId);

            ArrayList<Role> listRole = roleDAO.getAllRoles();

            request.setAttribute("user", userToEdit);
            request.setAttribute("listRole", listRole);

            request.getRequestDispatcher("/view/editUser.jsp")
                    .forward(request, response);

            return;

        }

        DangKy u = new DangKy();

        u.setUserId(userId);
        u.setFullName(request.getParameter("fullName"));
        u.setEmail(request.getParameter("email"));
        u.setPhone(phone);
        u.setRoleId(Integer.parseInt(request.getParameter("roleId")));
        u.setStatus("1".equals(request.getParameter("status")));

        dao.update(u);

        // ==========================
        // FIX LỖ HỔNG NGHIỆP VỤ: nếu user này đang có session hoạt động
        // (đang đăng nhập trên hệ thống), áp dụng NGAY vai trò/quyền mới
        // vào session đó. Trước đây session chỉ được nạp 1 lần lúc đăng
        // nhập nên user phải đăng xuất - đăng nhập lại mới thấy quyền mới,
        // thậm chí nếu bị hạ quyền thì vẫn thao tác được với quyền cũ cho
        // tới khi session hết hạn (30 phút) -> rất rủi ro về bảo mật.
        // ==========================
        DangKy freshUser = dao.getById(u.getUserId());

        boolean sessionRefreshed = false;

        if (freshUser != null) {
            sessionRefreshed = util.SessionRegistry.refreshUser(
                    u.getUserId(), freshUser);
        }

        // Nếu tài khoản vừa bị khóa (status = false), buộc đăng xuất ngay
        // lập tức thay vì chờ session tự hết hạn.
        if (!u.isStatus()) {
            util.SessionRegistry.forceLogout(u.getUserId());
        }

        ActivityLogger.log(request, "SUA", "Người dùng",
                "Cập nhật thông tin người dùng #" + u.getUserId()
                + (sessionRefreshed
                        ? " (đã áp dụng quyền mới ngay lên session đang hoạt động)"
                        : ""));

        response.sendRedirect(request.getContextPath() + "/nguoidung");

    }

    // ==========================
    // Khóa / mở khóa tài khoản
    // ==========================
    private void changeStatus(HttpServletRequest request,
            HttpServletResponse response, boolean status)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        dao.setStatus(id, status);

        // Khóa tài khoản -> buộc đăng xuất session đang hoạt động ngay lập
        // tức, không để user tiếp tục thao tác cho tới khi session hết hạn.
        String note = "";

        if (!status) {

            boolean forced = util.SessionRegistry.forceLogout(id);

            note = forced ? " (đã buộc đăng xuất ngay)" : "";

        }

        ActivityLogger.log(request, "SUA", "Người dùng",
                (status ? "Mở khóa" : "Khóa") + " tài khoản người dùng #" + id
                + note);

        response.sendRedirect(request.getContextPath() + "/nguoidung");

    }

    // ==========================
    // Đặt lại mật khẩu về mặc định, dùng khi nhân viên quên mật khẩu
    // ==========================
    private void resetPassword(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        String defaultPassword = "123456";

        dao.resetPassword(id, defaultPassword);

        ActivityLogger.log(request, "SUA", "Người dùng",
                "Đặt lại mật khẩu cho người dùng #" + id);

        response.sendRedirect(
                request.getContextPath()
                + "/nguoidung?message="
                + java.net.URLEncoder.encode(
                        "Đã đặt lại mật khẩu về mặc định: " + defaultPassword,
                        "UTF-8"));

    }

    @Override
    public String getServletInfo() {

        return "User Servlet";

    }

}
