package util;

import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Tiện ích phân trang dùng chung cho các trang danh sách
 * (Khách hàng, Hóa đơn, Công nợ, Thu tiền, Chi tiền...).
 *
 * Quy ước: 10 dữ liệu / 1 trang.
 */
public class Paginator {

    public static final int PAGE_SIZE = 10;

    /**
     * Đọc số trang hiện tại từ tham số "page" trên request.
     * Mặc định/khi không hợp lệ -> trang 1.
     */
    public static int currentPage(HttpServletRequest request) {

        int page = 1;

        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {
            page = 1;
        }

        if (page < 1) {
            page = 1;
        }

        return page;

    }

    /**
     * Tính tổng số trang dựa trên tổng số bản ghi.
     * Luôn trả về tối thiểu 1 trang (để nút phân trang luôn hiển thị).
     */
    public static int totalPages(int totalItems) {

        int pages = (int) Math.ceil(totalItems / (double) PAGE_SIZE);

        return pages < 1 ? 1 : pages;

    }

    /**
     * Cắt danh sách theo trang hiện tại (10 phần tử / trang).
     * Nếu trang truyền vào vượt quá tổng số trang thì tự động
     * lấy trang cuối cùng.
     */
    public static <T> List<T> slice(List<T> list, int page) {

        if (list == null || list.isEmpty()) {
            return new ArrayList<T>();
        }

        int total = list.size();
        int lastPage = totalPages(total);

        int p = page;
        if (p > lastPage) {
            p = lastPage;
        }
        if (p < 1) {
            p = 1;
        }

        int from = (p - 1) * PAGE_SIZE;
        int to = Math.min(from + PAGE_SIZE, total);

        if (from < 0 || from >= total) {
            return new ArrayList<T>();
        }

        return new ArrayList<T>(list.subList(from, to));

    }

    /**
     * Xây dựng URL nền cho các liên kết phân trang: giữ nguyên toàn bộ
     * tham số hiện có trên request (keyword, showInactive, action...)
     * ngoại trừ "page", và kết thúc bằng "?" hoặc "&" để nơi gọi chỉ
     * cần nối thêm "page=<số trang>" vào phía sau.
     */
    public static String buildPageUrl(HttpServletRequest request, String basePath) {

        StringBuilder sb = new StringBuilder(basePath);
        boolean first = true;

        Enumeration<String> names = request.getParameterNames();

        while (names.hasMoreElements()) {

            String name = names.nextElement();

            if ("page".equals(name)) {
                continue;
            }

            String[] values = request.getParameterValues(name);

            if (values == null) {
                continue;
            }

            for (String v : values) {

                sb.append(first ? "?" : "&");
                first = false;

                try {
                    sb.append(URLEncoder.encode(name, "UTF-8"))
                            .append("=")
                            .append(URLEncoder.encode(v, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    // UTF-8 luôn được hỗ trợ trên mọi JVM - không bao giờ xảy ra
                }

            }

        }

        sb.append(first ? "?" : "&");

        return sb.toString();

    }

}
