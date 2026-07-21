package filter;

import dao.PermissionDAO;
import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.DangKy;

@WebFilter(urlPatterns = {
    "/trangchu",
    "/khachhang",
    "/hoadon",
    "/congno",
    "/phieuthu",
    "/phieuchi",
    "/baocao",
    "/nguoidung",
    "/phanquyen",
    "/nhatky"
})
public class AuthFilter implements Filter {

    private final PermissionDAO permissionDAO = new PermissionDAO();

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/dangnhap");
            return;
        }

        DangKy user = (DangKy) session.getAttribute("user");

        // Trang chủ và các trang quản trị (nguoidung/phanquyen/nhatky) đã tự
        // kiểm tra quyền QTV riêng trong servlet, ở đây chỉ chặn theo bảng
        // Phân quyền (RolePermission) đối với các module nghiệp vụ.
        String permissionCode = mapUrlToPermissionCode(req.getServletPath());

        if (permissionCode != null
                && !permissionDAO.hasPermission(user.getRoleId(), permissionCode)) {

            resp.sendRedirect(
                    req.getContextPath()
                    + "/trangchu?error="
                    + java.net.URLEncoder.encode(
                            "Bạn không có quyền truy cập chức năng này. "
                            + "Vui lòng liên hệ Quản trị viên.",
                            "UTF-8"));

            return;

        }

        chain.doFilter(request, response);
    }

    // Ánh xạ URL sang mã quyền (permission_code) tương ứng trong bảng
    // Permission, dùng để kiểm tra RolePermission. Trả về null nếu URL
    // không thuộc nhóm cần kiểm tra theo Phân quyền (được servlet tự lo).
    private String mapUrlToPermissionCode(String servletPath) {

        switch (servletPath) {

            case "/khachhang":
                return "KHACHHANG";

            case "/hoadon":
                return "HOADON";

            case "/congno":
                return "CONGNO";

            case "/phieuthu":
                return "PHIEUTHU";

            case "/phieuchi":
                return "PHIEUCHI";

            case "/baocao":
                return "BAOCAO";

            default:
                return null;

        }

    }
}
