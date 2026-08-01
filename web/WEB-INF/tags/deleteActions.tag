<%@ tag pageEncoding="UTF-8" %>
<%@ attribute name="baseUrl" required="true" rtexprvalue="true" %>
<%@ attribute name="entityId" required="true" rtexprvalue="true" %>
<%@ attribute name="entityLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="isActive" required="true" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ attribute name="canSoftDelete" required="true" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ attribute name="pendingRequest" required="false" rtexprvalue="true" type="java.lang.Boolean" %>

<%
    // Nhãn dùng trong hộp thoại xác nhận / gửi lên server
    String labelForJs = (entityLabel == null ? "" : entityLabel).replace("'", "\\'");
    boolean pending = pendingRequest != null && pendingRequest;
    boolean privileged = canSoftDelete != null && canSoftDelete;
%>

<div class="d-inline-flex align-items-center gap-1">

<% if (isActive) { %>

    <%-- ================= BẢN GHI ĐANG HOẠT ĐỘNG ================= --%>

    <% if (privileged) { %>

        <%-- Admin / Giám đốc: nếu NV đã gửi yêu cầu thì hiện badge cảnh báo --%>
        <% if (pending) { %>
        <span class="badge bg-warning text-dark" title="Nhân viên đã đề xuất vô hiệu hóa bản ghi này"
              style="font-size:11px;">
            ⚠ NV đề xuất
        </span>
        <a href="<%=baseUrl%>?action=dismissrequest&id=<%=entityId%>"
           class="btn btn-outline-secondary btn-sm" title="Bỏ qua đề xuất (không vô hiệu hóa)"
           onclick="return confirm('Bỏ qua đề xuất vô hiệu hóa \'<%=labelForJs%>\'?')">
            ✕
        </a>
        <% } %>

        <% if (canSoftDelete != null && canSoftDelete) { %>
        <a href="<%=baseUrl%>?action=softdelete&id=<%=entityId%>"
           class="btn btn-outline-warning btn-sm" title="Vô hiệu hóa (ẩn, có thể khôi phục)"
           onclick="return confirm('Vô hiệu hóa \'<%=labelForJs%>\'? Bản ghi sẽ bị ẩn khỏi hệ thống, bạn có thể khôi phục lại sau.')">
            🚫
        </a>
        <% } %>

    <% } else { %>

        <%-- Nhân viên: không có quyền xóa -> chỉ được đề xuất --%>
        <% if (pending) { %>
        <span class="btn btn-outline-secondary btn-sm disabled" title="Đã gửi yêu cầu, đang chờ Admin/Giám đốc xem xét">
            ⏳ Đã gửi yêu cầu
        </span>
        <% } else { %>
        <a href="<%=baseUrl%>?action=requestdisable&id=<%=entityId%>"
           class="btn btn-outline-warning btn-sm" title="Xin quyền vô hiệu hóa - gửi đề xuất cho Admin/Giám đốc"
           onclick="return confirm('Gửi đề xuất vô hiệu hóa \'<%=labelForJs%>\' tới Admin/Giám đốc?')">
            🔔 Xin quyền vô hiệu hóa
        </a>
        <% } %>

    <% } %>

<% } else { %>

    <%-- ================= BẢN GHI ĐÃ BỊ VÔ HIỆU HÓA ================= --%>

    <% if (canSoftDelete != null && canSoftDelete) { %>
    <a href="<%=baseUrl%>?action=restore&id=<%=entityId%>"
       class="btn btn-outline-success btn-sm" title="Khôi phục lại hoạt động"
       onclick="return confirm('Khôi phục \'<%=labelForJs%>\'?')">
        ↩️ Khôi phục
    </a>
    <% } %>

    <% if (!privileged) { %>
    <span class="badge bg-secondary">Đã vô hiệu hóa</span>
    <% } %>

<% } %>

</div>
