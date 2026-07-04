package controller;

import dao.HoaDonDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import model.HoaDon;

@WebServlet("/hoadon")
public class HoaDonServlet extends HttpServlet {

    HoaDonDao dao = new HoaDonDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        // Mở form thêm hóa đơn
        if ("add".equals(action)) {
            request.getRequestDispatcher("view/addhoadon.jsp").forward(request, response);
            return;
        }

        // Tìm kiếm
        if ("search".equals(action)) {

            String keyword = request.getParameter("keyword");

            ArrayList<HoaDon> list = dao.timKiemHoaDon(keyword);

            request.setAttribute("list", list);

            request.getRequestDispatcher("view/listhoadon.jsp")
                    .forward(request, response);

            return;
        }

        // Hiển thị tất cả hóa đơn
        request.setAttribute("list", dao.getAll());

        request.getRequestDispatcher("view/listhoadon.jsp")
                .forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {

            int customerId = Integer.parseInt(request.getParameter("customerId"));

            int userId = Integer.parseInt(request.getParameter("userId"));

            BigDecimal totalAmount = new BigDecimal(request.getParameter("totalAmount"));

            String status = request.getParameter("status");

            HoaDon hd = new HoaDon();

            hd.setCustomerId(customerId);

            hd.setUserId(userId);

            hd.setInvoiceDate(new Date());

            hd.setTotalAmount(totalAmount);

            hd.setStatus(status);

            boolean check = dao.themHoaDon(hd);

            if (check) {

                request.getSession().setAttribute("message",
                        "Thêm hóa đơn thành công!");

                response.sendRedirect("hoadon");

            } else {

                request.setAttribute("error",
                        "Thêm hóa đơn thất bại!");

                request.getRequestDispatcher("view/addhoadon.jsp")
                        .forward(request, response);

            }

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute("error",
                    "Dữ liệu không hợp lệ!");

            request.getRequestDispatcher("view/addhoadon.jsp")
                    .forward(request, response);

        }

    }

}