package controller;

import dao.PermissionDAO;
import dao.RoleDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import model.DangKy;
import model.Permission;
import model.Role;
import util.ActivityLogger;

/**
 * Phân quyền: QTV chọn 1 vai trò rồi tick chọn những phần (module) mà vai
 * trò đó được truy cập. Chỉ Quản trị viên mới được vào servlet này.
 */
@WebServlet(name = "PermissionServlet", urlPatterns = {"/phanquyen"})
public class PermissionServlet extends HttpServlet {

    PermissionDAO permissionDAO = new PermissionDAO();
    RoleDAO roleDAO = new RoleDAO();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!checkAdmin(request, response)) {
            return;
        }

        ArrayList<Role> listRole = roleDAO.getAllRoles();

        int roleId;

        String roleIdParam = request.getParameter("roleId");

        if (roleIdParam != null && !roleIdParam.isEmpty()) {

            roleId = Integer.parseInt(roleIdParam);

        } else if (!listRole.isEmpty()) {

            roleId = listRole.get(0).getRoleId();

        } else {

            roleId = 0;

        }

        ArrayList<Permission> listPermission
                = permissionDAO.getPermissionsByRole(roleId);

        request.setAttribute("listRole", listRole);
        request.setAttribute("listPermission", listPermission);
        request.setAttribute("selectedRoleId", roleId);

        request.getRequestDispatcher("/view/phanquyen.jsp")
                .forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!checkAdmin(request, response)) {
            return;
        }

        request.setCharacterEncoding("UTF-8");

        int roleId = Integer.parseInt(request.getParameter("roleId"));

        String[] permissionIdsStr
                = request.getParameterValues("permissionIds");

        int[] permissionIds;

        if (permissionIdsStr == null) {

            permissionIds = new int[0];

        } else {

            permissionIds = new int[permissionIdsStr.length];

            for (int i = 0; i < permissionIdsStr.length; i++) {

                permissionIds[i] = Integer.parseInt(permissionIdsStr[i]);

            }

        }

        permissionDAO.saveRolePermissions(roleId, permissionIds);

        ArrayList<Role> listRole = roleDAO.getAllRoles();

        String roleName = "";

        for (Role r : listRole) {

            if (r.getRoleId() == roleId) {

                roleName = r.getRoleName();

                break;

            }

        }

        ActivityLogger.log(request, "SUA", "Phân quyền",
                "Cập nhật phân quyền cho vai trò \"" + roleName + "\"");

        response.sendRedirect(
                request.getContextPath()
                + "/phanquyen?roleId=" + roleId
                + "&message="
                + java.net.URLEncoder.encode(
                        "Cập nhật phân quyền thành công!", "UTF-8"));

    }

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

    @Override
    public String getServletInfo() {

        return "Permission Servlet";

    }

}
