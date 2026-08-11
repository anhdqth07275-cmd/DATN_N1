<%@ tag pageEncoding="UTF-8" %>
<%@ attribute name="currentPage" required="true" rtexprvalue="true" type="java.lang.Integer" %>
<%@ attribute name="totalPages" required="true" rtexprvalue="true" type="java.lang.Integer" %>
<%@ attribute name="pageUrl" required="true" rtexprvalue="true" %>

<%--
    Tag phân trang dùng chung cho các trang danh sách.

    - currentPage : trang hiện tại (bắt đầu từ 1)
    - totalPages  : tổng số trang
    - pageUrl     : URL nền (đã bao gồm các tham số khác như keyword,
                    showInactive,... và kết thúc bằng "?" hoặc "&") để
                    tag chỉ cần nối thêm "page=<số trang>" vào sau.
--%>

<%
    int cPage = currentPage == null ? 1 : currentPage;
    int tPages = totalPages == null ? 1 : totalPages;

    if (tPages < 1) {
        tPages = 1;
    }

    if (cPage < 1) {
        cPage = 1;
    }

    if (cPage > tPages) {
        cPage = tPages;
    }
%>

<nav aria-label="Phân trang">

    <ul class="pagination">

        <%-- Nút Previous --%>
        <li class="page-item <%=(cPage <= 1) ? "disabled" : ""%>">

            <a class="page-link"
               href="<%=(cPage <= 1) ? "#" : (pageUrl + "page=" + (cPage - 1))%>"
               aria-label="Previous">

                <span aria-hidden="true">&laquo;</span>
                Trước

            </a>

        </li>

        <%-- Các số trang --%>
        <% for (int i = 1; i <= tPages; i++) { %>

        <li class="page-item <%=(i == cPage) ? "active" : ""%>">

            <a class="page-link" href="<%=pageUrl%>page=<%=i%>">

                <%=i%>

            </a>

        </li>

        <% } %>

        <%-- Nút Next --%>
        <li class="page-item <%=(cPage >= tPages) ? "disabled" : ""%>">

            <a class="page-link"
               href="<%=(cPage >= tPages) ? "#" : (pageUrl + "page=" + (cPage + 1))%>"
               aria-label="Next">

                Sau
                <span aria-hidden="true">&raquo;</span>

            </a>

        </li>

    </ul>

    <div class="text-center text-muted" style="margin-top:-15px;font-size:13px;">

        Trang <%=cPage%> / <%=tPages%>

    </div>

</nav>
