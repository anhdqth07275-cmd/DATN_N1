package listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import util.SessionRegistry;

/**
 * Khi 1 session bị hủy (user đăng xuất, gọi session.invalidate(), hoặc
 * session tự hết hạn sau 30 phút không hoạt động) thì gỡ session đó khỏi
 * SessionRegistry để tránh giữ tham chiếu tới session đã chết (memory leak).
 */
@WebListener
public class SessionCleanupListener implements HttpSessionListener {

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

        SessionRegistry.unregisterBySessionId(se.getSession().getId());

    }

}
