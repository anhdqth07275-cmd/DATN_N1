package util;

import dao.ActivityLogDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.ActivityLog;
import model.DangKy;

/**
 * Hàm tiện ích dùng để ghi nhật ký hoạt động (Nhật ký hoạt động trong phần
 * Hệ thống) từ các servlet nghiệp vụ khác, mà không cần lặp lại logic lấy
 * thông tin người dùng từ session ở mỗi nơi.
 *
 * Việc ghi log không được để làm ảnh hưởng luồng nghiệp vụ chính, nên mọi
 * lỗi xảy ra khi ghi log đều được nuốt (chỉ in ra console) thay vì ném lên.
 */
public class ActivityLogger {

    private static final ActivityLogDAO dao = new ActivityLogDAO();

    public static void log(HttpServletRequest request, String actionType,
            String module, String description) {

        log(request, actionType, module, description, null);

    }

    public static void log(HttpServletRequest request, String actionType,
            String module, String description, Double amount) {

        try {

            HttpSession session = request.getSession(false);

            if (session == null) {
                return;
            }

            DangKy user = (DangKy) session.getAttribute("user");

            if (user == null) {
                return;
            }

            ActivityLog log = new ActivityLog();

            log.setUserId(user.getUserId());
            log.setUsername(user.getUsername());
            log.setFullName(user.getFullName());
            log.setRoleName(user.getRoleName());
            log.setActionType(actionType);
            log.setModule(module);
            log.setDescription(description);
            log.setAmount(amount);

            dao.insert(log);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}
