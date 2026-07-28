package util;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Random;

/**
 * Gửi mail (mã OTP quên mật khẩu) thông qua Gmail SMTP.
 *
 * Yêu cầu thư viện Jakarta Mail trong WEB-INF/lib, ví dụ file
 * "jakarta.mail-2.0.1.jar" (Maven: com.sun.mail:jakarta.mail:2.0.1) -
 * đây là 1 jar duy nhất gồm cả API lẫn implementation, không cần
 * thêm thư viện phụ nào khác.
 */
public class MailUtil {

    /**
     * Sinh 1 mã OTP gồm 6 chữ số (từ 000000 - 999999).
     */
    public static String generateOtp() {
        Random random = new Random();
        int number = random.nextInt(1_000_000);
        return String.format("%06d", number);
    }

    /**
     * Gửi mã OTP tới email của người dùng.
     *
     * @param toEmail email người nhận
     * @param otp     mã OTP đã sinh
     * @throws MessagingException nếu gửi mail thất bại (sai cấu hình,
     *                             mất kết nối, email không hợp lệ,...)
     */
    public static void sendOtpEmail(String toEmail, String otp) throws MessagingException {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", MailConfig.SMTP_HOST);
        props.put("mail.smtp.port", MailConfig.SMTP_PORT);

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        MailConfig.SENDER_EMAIL, MailConfig.APP_PASSWORD);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(
                    MailConfig.SENDER_EMAIL, MailConfig.SENDER_NAME));

            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(toEmail));

            message.setSubject("[SME:FAD] Ma xac thuc OTP - Quen mat khau");

            String content =
                    "<div style=\"font-family:Arial,Helvetica,sans-serif;font-size:15px;color:#222\">"
                    + "<p>Xin chào,</p>"
                    + "<p>Bạn (hoặc ai đó) vừa yêu cầu đặt lại mật khẩu cho tài khoản "
                    + "SME:FAD gắn với email này.</p>"
                    + "<p>Mã xác thực (OTP) của bạn là:</p>"
                    + "<p style=\"font-size:28px;font-weight:bold;letter-spacing:6px;"
                    + "color:#0b4f86\">" + otp + "</p>"
                    + "<p>Mã có hiệu lực trong <b>" + MailConfig.OTP_EXPIRE_MINUTES
                    + " phút</b>. Vui lòng không chia sẻ mã này cho bất kỳ ai.</p>"
                    + "<p>Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email.</p>"
                    + "</div>";

            message.setContent(content, "text/html; charset=UTF-8");

            Transport.send(message);

        } catch (UnsupportedEncodingException e) {
            throw new MessagingException("Lỗi cấu hình tên người gửi", e);
        }
    }
}
