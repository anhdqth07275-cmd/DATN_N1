package util;

/**
 * Cấu hình tài khoản Gmail dùng để gửi mã OTP cho chức năng
 * "Quên mật khẩu".
 *
 * CÁCH LẤY APP_PASSWORD (bắt buộc, không dùng được mật khẩu Gmail
 * thường vì Google đã chặn "less secure app"):
 * 1. Bật xác minh 2 bước cho tài khoản Gmail dùng để gửi mail:
 *    https://myaccount.google.com/security
 * 2. Vào https://myaccount.google.com/apppasswords, tạo 1 App
 *    password mới (chọn app "Mail"), Google sẽ cấp 1 chuỗi 16 ký tự.
 * 3. Dán chuỗi đó vào APP_PASSWORD bên dưới (bỏ hết dấu cách).
 */
public class MailConfig {

    // Email Gmail dùng để GỬI OTP đi (không phải email của người dùng)
    public static final String SENDER_EMAIL = "anhdqth07275@gmail.com";

    // App Password 16 ký tự lấy từ Google (KHÔNG phải mật khẩu Gmail thường)
    public static final String APP_PASSWORD = "hnzvwwvklavjrqqp";

    // Tên hiển thị của người gửi
    public static final String SENDER_NAME = "SME:FAD - He thong quan ly tai chinh cong no";

    // Thời gian sống của mã OTP (phút)
    public static final int OTP_EXPIRE_MINUTES = 5;

    public static final String SMTP_HOST = "smtp.gmail.com";
    public static final String SMTP_PORT = "587";
}
