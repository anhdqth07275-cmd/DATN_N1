/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CustomerDAO;
import dao.PermissionDAO;
import dao.RequestDisableDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import model.Customer;
import model.DangKy;

/**
 *
 * @author Admin
 */
@WebServlet(name = "CustomerServlet", urlPatterns = {"/khachhang"})
public class CustomerServlet extends HttpServlet {

    CustomerDAO dao = new CustomerDAO();
    PermissionDAO permissionDAO = new PermissionDAO();
    RequestDisableDAO requestDAO = new RequestDisableDAO();
    private static final String MODULE = "KHACHHANG";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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
                deleteCustomer(request, response);
                break;

            case "softdelete":
                softDeleteCustomer(request, response);
                break;

            case "restore":
                restoreCustomer(request, response);
                break;

            case "harddelete":
                hardDeleteCustomer(request, response);
                break;

            case "requestdisable":
                requestDisableCustomer(request, response);
                break;

            case "dismissrequest":
                dismissRequestCustomer(request, response);
                break;

            case "search":
                searchCustomer(request, response);
                break;

            default:
                listCustomer(request, response);
                break;
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        switch (action) {

            case "insert":

                insertCustomer(request, response);

                break;

            case "update":

                updateCustomer(request, response);

                break;
            case "delete":
                deleteCustomer(request, response);
                break;
            case "search":
                searchCustomer(request, response);
                break;

        }
    }

    private void searchCustomer(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");

        ArrayList<Customer> list = dao.search(keyword);

        HashSet<Integer> pendingIds = requestDAO.getPendingEntityIds(MODULE);

        request.setAttribute("listCustomer", list);
        request.setAttribute("canSoftDelete", hasAction(request, "XOAMEM"));
        request.setAttribute("canHardDelete", hasAction(request, "XOACUNG"));
        request.setAttribute("pendingIds", pendingIds);
        request.setAttribute("showInactive", false);

        request.getRequestDispatcher("/view/qlkh.jsp")
                .forward(request, response);

    }

    private void deleteCustomer(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        softDeleteCustomer(request, response);

    }

    // ==========================
    // Kiểm tra người dùng hiện tại có quyền hành động cụ thể trên module
    // này không (THEM/SUA/XOAMEM/XOACUNG) - đọc trực tiếp từ session.
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
    // Xóa mềm (vô hiệu hóa) - chỉ Admin/Giám đốc (có quyền KHACHHANG_XOAMEM)
    // ==========================
    private void softDeleteCustomer(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        if (!hasAction(request, "XOAMEM")) {
            response.sendRedirect(request.getContextPath() + "/khachhang");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));

        Customer c = dao.getById(id);

        dao.softDelete(id);

        // Nếu bản ghi này từng có yêu cầu "xin quyền vô hiệu hóa" đang chờ
        // của Nhân viên thì tự động đánh dấu yêu cầu đó đã được chấp thuận.
        requestDAO.resolve(MODULE, id, currentUserId(request), "Approved");

        util.ActivityLogger.log(request, "XOA", "Khách hàng",
                "Vô hiệu hóa khách hàng \""
                + (c != null ? c.getCustomerName() : "#" + id) + "\"");

        response.sendRedirect(request.getContextPath() + "/khachhang");

    }

    // ==========================
    // Khôi phục bản ghi đã bị vô hiệu hóa - chỉ Admin/Giám đốc
    // ==========================
    private void restoreCustomer(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        if (!hasAction(request, "XOAMEM")) {
            response.sendRedirect(request.getContextPath() + "/khachhang");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));

        Customer c = dao.getById(id);

        dao.restore(id);

        util.ActivityLogger.log(request, "SUA", "Khách hàng",
                "Khôi phục khách hàng \""
                + (c != null ? c.getCustomerName() : "#" + id) + "\"");

        response.sendRedirect(request.getContextPath() + "/khachhang");

    }

    // ==========================
    // Xóa vĩnh viễn - chỉ Admin (có quyền KHACHHANG_XOACUNG)
    // ==========================
    private void hardDeleteCustomer(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        if (!hasAction(request, "XOACUNG")) {
            response.sendRedirect(request.getContextPath() + "/khachhang");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));

        Customer c = dao.getById(id);

        dao.hardDelete(id);

        util.ActivityLogger.log(request, "XOA", "Khách hàng",
                "Xóa vĩnh viễn khách hàng \""
                + (c != null ? c.getCustomerName() : "#" + id) + "\"");

        response.sendRedirect(request.getContextPath() + "/khachhang");

    }

    // ==========================
    // Nhân viên "Xin quyền vô hiệu hóa" - chỉ đánh dấu, không xóa gì cả
    // ==========================
    private void requestDisableCustomer(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        Customer c = dao.getById(id);

        String label = c != null ? c.getCustomerName() : ("#" + id);

        requestDAO.create(MODULE, id, label, currentUserId(request));

        util.ActivityLogger.log(request, "SUA", "Khách hàng",
                "Đề xuất vô hiệu hóa khách hàng \"" + label + "\"");

        response.sendRedirect(request.getContextPath() + "/khachhang");

    }

    // ==========================
    // Admin/Giám đốc bỏ qua đề xuất (không vô hiệu hóa)
    // ==========================
    private void dismissRequestCustomer(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        if (!hasAction(request, "XOAMEM")) {
            response.sendRedirect(request.getContextPath() + "/khachhang");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));

        requestDAO.resolve(MODULE, id, currentUserId(request), "Rejected");

        response.sendRedirect(request.getContextPath() + "/khachhang");

    }

    private void updateCustomer(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        if (!hasAction(request, "SUA")) {
            response.sendRedirect(request.getContextPath() + "/khachhang");
            return;
        }

        Customer c = new Customer();

        c.setCustomerId(
                Integer.parseInt(request.getParameter("customerId"))
        );

        c.setCustomerName(request.getParameter("customerName"));
        c.setPhone(request.getParameter("phone"));
        c.setAddress(request.getParameter("address"));
        c.setEmail(request.getParameter("email"));

        c.setStatus(
                Boolean.parseBoolean(request.getParameter("status"))
        );

        dao.update(c);

        util.ActivityLogger.log(request, "SUA", "Khách hàng",
                "Cập nhật khách hàng \"" + c.getCustomerName() + "\"");

        response.sendRedirect(request.getContextPath() + "/khachhang");

    }

    private void showEditForm(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!hasAction(request, "SUA")) {
            response.sendRedirect(request.getContextPath() + "/khachhang");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));

        Customer customer = dao.getById(id);

        request.setAttribute("customer", customer);

        request.getRequestDispatcher("/view/editCustomer.jsp")
                .forward(request, response);

    }

    private void showAddForm(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!hasAction(request, "THEM")) {
            response.sendRedirect(request.getContextPath() + "/khachhang");
            return;
        }

        request.getRequestDispatcher("/view/addCustomer.jsp")
                .forward(request, response);

    }

    private void insertCustomer(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!hasAction(request, "THEM")) {
            response.sendRedirect(request.getContextPath() + "/khachhang");
            return;
        }

        Customer c = new Customer();

        c.setCustomerName(request.getParameter("customerName"));
        c.setPhone(request.getParameter("phone"));
        c.setAddress(request.getParameter("address"));
        c.setEmail(request.getParameter("email"));
        c.setStatus(true);

        if (dao.insert(c)) {

            util.ActivityLogger.log(request, "THEM", "Khách hàng",
                    "Thêm khách hàng \"" + c.getCustomerName() + "\"");

            response.sendRedirect(request.getContextPath() + "/khachhang");

        } else {

            request.setAttribute("error", "Thêm khách hàng thất bại!");

            request.getRequestDispatcher("/view/addCustomer.jsp")
                    .forward(request, response);

        }

    }

    private void listCustomer(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        boolean canSoftDelete = hasAction(request, "XOAMEM");
        boolean canHardDelete = hasAction(request, "XOACUNG");
        boolean canAdd = hasAction(request, "THEM");
        boolean canEdit = hasAction(request, "SUA");

        boolean showInactive = canSoftDelete
                && "1".equals(request.getParameter("showInactive"));

        ArrayList<Customer> list = dao.getAll(showInactive);

        HashSet<Integer> pendingIds = requestDAO.getPendingEntityIds(MODULE);

        request.setAttribute("listCustomer", list);
        request.setAttribute("canSoftDelete", canSoftDelete);
        request.setAttribute("canHardDelete", canHardDelete);
        request.setAttribute("canAdd", canAdd);
        request.setAttribute("canEdit", canEdit);
        request.setAttribute("pendingIds", pendingIds);
        request.setAttribute("showInactive", showInactive);

        request.getRequestDispatcher("/view/qlkh.jsp")
                .forward(request, response);

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
