package util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Helper dùng chung để lưu "ảnh minh chứng" (hóa đơn, biên lai, chứng từ
 * chuyển khoản...) đính kèm theo phiếu thu / phiếu chi, phục vụ mục tiêu
 * tăng tính minh bạch trong quản lý tiền.
 *
 * Ảnh được lưu vào thư mục vật lý uploads/&lt;subFolder&gt; ngay trong
 * ứng dụng web đang chạy; CSDL chỉ lưu ĐƯỜNG DẪN TƯƠNG ĐỐI (vd:
 * "uploads/receipts/xxxx.jpg") để có thể ghép trực tiếp với context
 * path khi hiển thị bằng thẻ &lt;img&gt;.
 *
 * Lưu ý: vì file được lưu trong thư mục triển khai (deploy) của
 * ứng dụng, ảnh có thể bị mất nếu deploy lại (redeploy/rebuild) WAR.
 * Với đồ án/ demo cục bộ thì cách này đơn giản và đủ dùng; khi lên
 * môi trường thật nên trỏ thư mục lưu ra ngoài thư mục ứng dụng.
 */
public class FileUploadHelper {

    private static final List<String> ALLOWED_EXT
            = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".webp");

    private static final long MAX_FILE_SIZE = 5L * 1024 * 1024; // 5MB

    // ==========================
    // Lưu ảnh minh chứng mới lên đĩa.
    // Trả về null nếu người dùng không chọn file nào (giữ nguyên ảnh cũ).
    // Ném IllegalArgumentException nếu file không hợp lệ (sai định dạng
    // hoặc vượt quá dung lượng cho phép) để tầng Servlet bắt và báo lỗi
    // thân thiện cho người dùng.
    // ==========================
    public static String saveEvidenceImage(HttpServletRequest request,
            Part filePart, String subFolder) throws IOException {

        if (filePart == null || filePart.getSize() == 0) {
            return null;
        }

        String originalName = extractFileName(filePart);

        String ext = getExtension(originalName).toLowerCase();

        if (!ALLOWED_EXT.contains(ext)) {

            throw new IllegalArgumentException(
                    "Ảnh minh chứng chỉ chấp nhận định dạng "
                    + "JPG, JPEG, PNG, GIF hoặc WEBP.");

        }

        if (filePart.getSize() > MAX_FILE_SIZE) {

            throw new IllegalArgumentException(
                    "Dung lượng ảnh minh chứng không được vượt quá 5MB.");

        }

        String uploadRealPath = request.getServletContext()
                .getRealPath("/uploads/" + subFolder);

        File uploadDir = new File(uploadRealPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String newFileName = UUID.randomUUID().toString()
                .replace("-", "") + ext;

        Path destination = Paths.get(uploadRealPath, newFileName);

        try (InputStream in = filePart.getInputStream()) {

            Files.copy(in, destination,
                    StandardCopyOption.REPLACE_EXISTING);

        }

        return "uploads/" + subFolder + "/" + newFileName;

    }

    // ==========================
    // Xóa ảnh minh chứng cũ khỏi đĩa (khi thay ảnh mới). Không ném lỗi
    // nếu xóa thất bại - chỉ log ra console để không làm gián đoạn
    // nghiệp vụ chính (thêm/sửa phiếu).
    // ==========================
    public static void deleteEvidenceImage(HttpServletRequest request,
            String relativePath) {

        if (relativePath == null || relativePath.isEmpty()) {
            return;
        }

        try {

            String realPath = request.getServletContext()
                    .getRealPath("/" + relativePath);

            if (realPath != null) {

                File f = new File(realPath);

                if (f.exists()) {
                    f.delete();
                }

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    private static String extractFileName(Part part) {

        String header = part.getHeader("content-disposition");

        if (header == null) {
            return "";
        }

        for (String token : header.split(";")) {

            if (token.trim().startsWith("filename")) {

                return token.substring(token.indexOf('=') + 1)
                        .trim().replace("\"", "");

            }

        }

        return "";

    }

    private static String getExtension(String fileName) {

        int dot = fileName.lastIndexOf(".");

        if (dot == -1) {
            return "";
        }

        return fileName.substring(dot);

    }

}
