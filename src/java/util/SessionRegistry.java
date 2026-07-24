package util;

import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import model.DangKy;

/**
 * Theo dõi session HTTP đang hoạt động của từng user (userId -> HttpSession).
 *
 * Mục đích: khi QTV cập nhật vai trò / khóa tài khoản của 1 user tại màn hình
 * Quản lý người dùng, hệ thống cần áp dụng thay đổi đó NGAY LẬP TỨC lên
 * session đang sống của user đó, thay vì chỉ update dưới database rồi chờ
 * user tự đăng xuất - đăng nhập lại mới nhận được quyền mới (lỗ hổng nghiệp
 * vụ: user vẫn dùng được quyền cũ / bị hạ quyền nhưng vẫn thao tác được cho
 * tới khi session hết hạn).
 *
 * Lưu ý: đây là giải pháp lưu trong bộ nhớ (in-memory), phù hợp với mô hình
 * 1 server của đồ án. Nếu deploy nhiều instance (clustering) thì cần thay
 * bằng giải pháp tập trung (Redis, DB session, ...).
 */
public class SessionRegistry {

    private static final Map<Integer, HttpSession> ACTIVE_SESSIONS
            = new ConcurrentHashMap<>();

    private SessionRegistry() {
    }

    // ==========================
    // Gọi khi user đăng nhập thành công
    // ==========================
    public static void register(int userId, HttpSession session) {

        ACTIVE_SESSIONS.put(userId, session);

    }

    // ==========================
    // Gọi khi user đăng xuất hoặc session hết hạn/bị hủy
    // ==========================
    public static void unregister(int userId) {

        ACTIVE_SESSIONS.remove(userId);

    }

    // Gỡ theo sessionId, dùng trong HttpSessionListener khi không còn giữ
    // trực tiếp userId (session sắp bị hủy).
    public static void unregisterBySessionId(String sessionId) {

        ACTIVE_SESSIONS.values().removeIf(
                s -> s.getId().equals(sessionId));

    }

    // ==========================
    // QTV vừa đổi vai trò / thông tin của user -> nếu user đó đang có
    // session hoạt động, cập nhật lại ngay session đó với dữ liệu mới nhất.
    // Trả về true nếu có session đang hoạt động và đã được cập nhật.
    // ==========================
    public static boolean refreshUser(int userId, DangKy freshUser) {

        HttpSession session = ACTIVE_SESSIONS.get(userId);

        if (session == null) {
            return false;
        }

        try {

            session.setAttribute("user", freshUser);

            return true;

        } catch (IllegalStateException e) {

            // Session đã invalidate (vd user vừa đăng xuất) -> dọn registry
            ACTIVE_SESSIONS.remove(userId);

            return false;

        }

    }

    // ==========================
    // QTV khóa tài khoản -> buộc đăng xuất user đó ngay lập tức, không chờ
    // session hết hạn (30 phút) mới bị chặn.
    // Trả về true nếu user đó đang có session hoạt động và đã bị hủy.
    // ==========================
    public static boolean forceLogout(int userId) {

        HttpSession session = ACTIVE_SESSIONS.remove(userId);

        if (session == null) {
            return false;
        }

        try {

            session.invalidate();

            return true;

        } catch (IllegalStateException e) {

            // Session đã bị hủy trước đó rồi, bỏ qua
            return false;

        }

    }

}
