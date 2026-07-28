package controller;

import dao.QuenMatKhauDAO;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.DangKy;
import util.MailConfig;
import util.MailUtil;

/**
 * Xử lý toàn bộ luồng "Quên mật khẩu":
 *  B1: Người dùng nhập Email đã đăng ký -> hệ thống sinh OTP, gửi qua Gmail.
 *  B2: Người dùng nhập mã OTP nhận được trong Gmail để xác thực.
 *  B3: Nhập mật khẩu mới + nhập lại mật khẩu mới -> cập nhật vào CSDL.
 *
 * Toàn bộ trạng thái tạm (email đang xử lý, OTP, thời điểm hết hạn,
 * đã xác thực OTP hay chưa) được lưu trong session, không lưu xuống
 * CSDL để tránh phải thay đổi cấu trúc bảng [User].
 */
@WebServlet(name = "QuenMatKhauServlet", urlPatterns = {"/quenmatkhau"})
public class QuenMatKhauServlet extends HttpServlet {

    private static final String SESS_EMAIL = "fp_email";
    private static final String SESS_OTP = "fp_otp";
    private static final String SESS_OTP_TIME = "fp_otp_time";
    private static final String SESS_VERIFIED = "fp_verified";

    private final QuenMatKhauDAO dao = new QuenMatKhauDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String step = request.getParameter("step");
        HttpSession session = request.getSession();

        if ("otp".equals(step) && session.getAttribute(SESS_EMAIL) != null) {

            request.getRequestDispatcher("/view/quenmatkhau_otp.jsp")
                    .forward(request, response);

        } else if ("reset".equals(step)
                && Boolean.TRUE.equals(session.getAttribute(SESS_VERIFIED))) {

            request.getRequestDispatcher("/view/quenmatkhau_reset.jsp")
                    .forward(request, response);

        } else {

            // Bắt đầu lại từ đầu: xoá trạng thái quên mật khẩu cũ (nếu có)
            clearSession(session);

            request.getRequestDispatcher("/view/quenmatkhau.jsp")
                    .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if (action == null) {
            action = "";
        }

        switch (action) {

            case "sendOtp":
                handleSendOtp(request, response);
                break;

            case "resendOtp":
                handleResendOtp(request, response);
                break;

            case "verifyOtp":
                handleVerifyOtp(request, response);
                break;

            case "resetPassword":
                handleResetPassword(request, response);
                break;

            default:
                response.sendRedirect(request.getContextPath() + "/quenmatkhau");
        }
    }

    // ============ BƯỚC 1: Nhập email, gửi OTP ============
    private void handleSendOtp(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        HttpSession session = request.getSession();

        if (email == null || email.trim().isEmpty()) {

            request.setAttribute("error", "Vui lòng nhập email!");
            request.getRequestDispatcher("/view/quenmatkhau.jsp")
                    .forward(request, response);
            return;
        }

        email = email.trim();

        DangKy user = dao.getUserByEmail(email);

        if (user == null) {

            request.setAttribute("error",
                    "Email này chưa được đăng ký với hệ thống hoặc tài khoản đã bị khóa!");
            request.getRequestDispatcher("/view/quenmatkhau.jsp")
                    .forward(request, response);
            return;
        }

        String otp = MailUtil.generateOtp();

        try {
            MailUtil.sendOtpEmail(email, otp);

        } catch (MessagingException e) {

            e.printStackTrace();

            request.setAttribute("error",
                    "Không thể gửi email chứa mã OTP. Vui lòng thử lại sau!");
            request.getRequestDispatcher("/view/quenmatkhau.jsp")
                    .forward(request, response);
            return;
        }

        session.setAttribute(SESS_EMAIL, email);
        session.setAttribute(SESS_OTP, otp);
        session.setAttribute(SESS_OTP_TIME, System.currentTimeMillis());
        session.setAttribute(SESS_VERIFIED, false);

        request.setAttribute("success",
                "Mã OTP đã được gửi tới email " + email + ". Vui lòng kiểm tra hộp thư đến (hoặc mục Spam).");

        request.getRequestDispatcher("/view/quenmatkhau_otp.jsp")
                .forward(request, response);
    }

    // ============ Gửi lại mã OTP (khi hết hạn / chưa nhận được mail) ============
    private void handleResendOtp(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String email = (String) session.getAttribute(SESS_EMAIL);

        if (email == null) {
            response.sendRedirect(request.getContextPath() + "/quenmatkhau");
            return;
        }

        String otp = MailUtil.generateOtp();

        try {
            MailUtil.sendOtpEmail(email, otp);

        } catch (MessagingException e) {

            e.printStackTrace();

            request.setAttribute("error",
                    "Không thể gửi lại email chứa mã OTP. Vui lòng thử lại sau!");
            request.getRequestDispatcher("/view/quenmatkhau_otp.jsp")
                    .forward(request, response);
            return;
        }

        session.setAttribute(SESS_OTP, otp);
        session.setAttribute(SESS_OTP_TIME, System.currentTimeMillis());

        request.setAttribute("success", "Đã gửi lại mã OTP mới tới email " + email + ".");

        request.getRequestDispatcher("/view/quenmatkhau_otp.jsp")
                .forward(request, response);
    }

    // ============ BƯỚC 2: Xác thực mã OTP ============
    private void handleVerifyOtp(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        String email = (String) session.getAttribute(SESS_EMAIL);
        String realOtp = (String) session.getAttribute(SESS_OTP);
        Long otpTime = (Long) session.getAttribute(SESS_OTP_TIME);

        if (email == null || realOtp == null || otpTime == null) {
            response.sendRedirect(request.getContextPath() + "/quenmatkhau");
            return;
        }

        String inputOtp = request.getParameter("otp");

        long expireMillis = MailConfig.OTP_EXPIRE_MINUTES * 60L * 1000L;
        boolean expired = (System.currentTimeMillis() - otpTime) > expireMillis;

        if (expired) {

            request.setAttribute("error",
                    "Mã OTP đã hết hạn. Vui lòng bấm \"Gửi lại mã\" để nhận mã mới!");
            request.getRequestDispatcher("/view/quenmatkhau_otp.jsp")
                    .forward(request, response);
            return;
        }

        if (inputOtp == null || !inputOtp.trim().equals(realOtp)) {

            request.setAttribute("error", "Mã OTP không đúng. Vui lòng kiểm tra lại!");
            request.getRequestDispatcher("/view/quenmatkhau_otp.jsp")
                    .forward(request, response);
            return;
        }

        session.setAttribute(SESS_VERIFIED, true);

        request.getRequestDispatcher("/view/quenmatkhau_reset.jsp")
                .forward(request, response);
    }

    // ============ BƯỚC 3: Đặt mật khẩu mới ============
    private void handleResetPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        String email = (String) session.getAttribute(SESS_EMAIL);
        boolean verified = Boolean.TRUE.equals(session.getAttribute(SESS_VERIFIED));

        if (email == null || !verified) {
            response.sendRedirect(request.getContextPath() + "/quenmatkhau");
            return;
        }

        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (newPassword == null || newPassword.length() < 6) {

            request.setAttribute("error", "Mật khẩu mới phải có ít nhất 6 ký tự!");
            request.getRequestDispatcher("/view/quenmatkhau_reset.jsp")
                    .forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {

            request.setAttribute("error", "Mật khẩu nhập lại không khớp!");
            request.getRequestDispatcher("/view/quenmatkhau_reset.jsp")
                    .forward(request, response);
            return;
        }

        boolean result = dao.updatePasswordByEmail(email, newPassword);

        if (result) {

            clearSession(session);

            session.setAttribute("success",
                    "Đổi mật khẩu thành công! Vui lòng đăng nhập lại bằng mật khẩu mới.");

            response.sendRedirect(request.getContextPath() + "/dangnhap");

        } else {

            request.setAttribute("error", "Đặt lại mật khẩu thất bại. Vui lòng thử lại!");
            request.getRequestDispatcher("/view/quenmatkhau_reset.jsp")
                    .forward(request, response);
        }
    }

    private void clearSession(HttpSession session) {
        session.removeAttribute(SESS_EMAIL);
        session.removeAttribute(SESS_OTP);
        session.removeAttribute(SESS_OTP_TIME);
        session.removeAttribute(SESS_VERIFIED);
    }
}
