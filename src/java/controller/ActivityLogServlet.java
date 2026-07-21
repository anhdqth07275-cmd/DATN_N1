package controller;

import dao.ActivityLogDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import model.ActivityLog;
import model.ActivitySummary;
import model.DangKy;

/**
 * Nhật ký hoạt động: có thanh chọn khoảng thời gian "từ ngày ... đến ngày
 * ..." và tổng hợp lại các hoạt động thu / chi / thêm / sửa / xóa / đăng
 * nhập trong khoảng thời gian đó. Chỉ Quản trị viên mới được xem toàn bộ
 * nhật ký của hệ thống.
 */
@WebServlet(name = "ActivityLogServlet", urlPatterns = {"/nhatky"})
public class ActivityLogServlet extends HttpServlet {

    ActivityLogDAO dao = new ActivityLogDAO();

    private static final SimpleDateFormat INPUT_FORMAT
            = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!checkAdmin(request, response)) {
            return;
        }

        String fromParam = request.getParameter("fromDate");
        String toParam = request.getParameter("toDate");

        Calendar calFrom = Calendar.getInstance();
        Calendar calTo = Calendar.getInstance();

        try {

            if (fromParam != null && !fromParam.isEmpty()) {

                Date d = INPUT_FORMAT.parse(fromParam);

                calFrom.setTime(d);

            } else {

                // Mặc định: đầu tháng hiện tại
                calFrom.set(Calendar.DAY_OF_MONTH, 1);

            }

            if (toParam != null && !toParam.isEmpty()) {

                Date d = INPUT_FORMAT.parse(toParam);

                calTo.setTime(d);

            }

        } catch (Exception e) {

            calFrom = Calendar.getInstance();
            calFrom.set(Calendar.DAY_OF_MONTH, 1);

            calTo = Calendar.getInstance();

        }

        calFrom.set(Calendar.HOUR_OF_DAY, 0);
        calFrom.set(Calendar.MINUTE, 0);
        calFrom.set(Calendar.SECOND, 0);
        calFrom.set(Calendar.MILLISECOND, 0);

        calTo.set(Calendar.HOUR_OF_DAY, 23);
        calTo.set(Calendar.MINUTE, 59);
        calTo.set(Calendar.SECOND, 59);
        calTo.set(Calendar.MILLISECOND, 999);

        Timestamp start = new Timestamp(calFrom.getTimeInMillis());
        Timestamp end = new Timestamp(calTo.getTimeInMillis());

        ArrayList<ActivityLog> listLog = dao.getLogs(start, end);

        ActivitySummary summary = dao.getSummary(start, end);

        request.setAttribute("listLog", listLog);
        request.setAttribute("summary", summary);

        request.setAttribute("fromDate", INPUT_FORMAT.format(calFrom.getTime()));
        request.setAttribute("toDate", INPUT_FORMAT.format(calTo.getTime()));

        request.getRequestDispatcher("/view/nhatky.jsp")
                .forward(request, response);

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

        return "Activity Log Servlet";

    }

}
