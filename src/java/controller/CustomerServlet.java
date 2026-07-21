/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CustomerDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import model.Customer;

/**
 *
 * @author Admin
 */
@WebServlet(name = "CustomerServlet", urlPatterns = {"/khachhang"})
public class CustomerServlet extends HttpServlet {

    CustomerDAO dao = new CustomerDAO();

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

        request.setAttribute("listCustomer", list);

        request.getRequestDispatcher("/view/qlkh.jsp")
                .forward(request, response);

    }

    private void deleteCustomer(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        dao.delete(id);

        util.ActivityLogger.log(request, "XOA", "Khách hàng",
                "Xóa khách hàng #" + id);

        response.sendRedirect(request.getContextPath() + "/khachhang");

    }

    private void updateCustomer(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

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

        int id = Integer.parseInt(request.getParameter("id"));

        Customer customer = dao.getById(id);

        request.setAttribute("customer", customer);

        request.getRequestDispatcher("/view/editCustomer.jsp")
                .forward(request, response);

    }

    private void showAddForm(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/view/addCustomer.jsp")
                .forward(request, response);

    }

    private void insertCustomer(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

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

        ArrayList<Customer> list = dao.getAll();

        request.setAttribute("listCustomer", list);

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
