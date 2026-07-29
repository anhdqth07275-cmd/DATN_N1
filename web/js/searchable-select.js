/**
 * SEARCHABLE SELECT
 * -----------------------------------------------------------------------
 * Biến 1 thẻ <select> có sẵn thành ô tìm kiếm (search box) + danh sách kết
 * quả lọc theo thời gian gõ, dùng cho các trường có nhiều lựa chọn (khách
 * hàng, hóa đơn...) mà chọn qua <select> xổ xuống thông thường sẽ rất khó
 * dùng khi dữ liệu lớn (hàng trăm khách hàng chẳng hạn).
 *
 * Nguyên tắc: <select> gốc vẫn được GIỮ NGUYÊN trong form (chỉ ẩn đi bằng
 * CSS), nên toàn bộ code cũ đang đọc giá trị select đó (submit form, hoặc
 * các script khác nghe sự kiện "change" trên select) đều chạy y nguyên,
 * không cần sửa gì thêm ở phía server hay các script khác.
 *
 * Cách dùng:
 *   <select id="customerId" name="customerId">
 *       <option value="">-- Chọn khách hàng --</option>
 *       <option value="1" data-search="0901234567" data-sub="0901234567">
 *           Công ty Minh Phát
 *       </option>
 *       ...
 *   </select>
 *
 *   <script>
 *       initSearchableSelect('customerId', {
 *           placeholder: 'Tìm kiếm khách hàng theo tên/SĐT...'
 *       });
 *   </script>
 *
 * Thuộc tính tùy chọn trên mỗi <option>:
 *   data-search : text bổ sung để tìm kiếm nhưng không cần hiển thị trong
 *                 dòng chính (ví dụ số điện thoại, mã hóa đơn...)
 *   data-sub    : dòng phụ hiển thị nhỏ bên dưới trong danh sách kết quả
 */
function initSearchableSelect(selectId, opts) {

    opts = opts || {};

    const select = document.getElementById(selectId);

    if (!select) {
        return;
    }

    const placeholder = opts.placeholder || "Tìm kiếm...";
    const emptyText = opts.emptyText || "Không tìm thấy kết quả phù hợp";

    // Đọc toàn bộ <option> (bỏ option rỗng "-- Chọn --") thành dữ liệu để lọc
    const items = Array.from(select.options)
            .filter(o => o.value !== "")
            .map(o => ({
                value: o.value,
                label: o.textContent.trim().replace(/\s+/g, " "),
                search: (o.textContent + " " + (o.dataset.search || "")).toLowerCase(),
                sub: o.dataset.sub || "",
                optionEl: o
            }));

    // Ẩn select gốc (vẫn nằm trong form để submit / các script khác dùng)
    select.style.display = "none";

    const wrap = document.createElement("div");
    wrap.className = "ss-wrap";

    const input = document.createElement("input");
    input.type = "text";
    input.className = "form-control ss-input";
    input.placeholder = placeholder;
    input.autocomplete = "off";

    const dropdown = document.createElement("div");
    dropdown.className = "ss-dropdown";
    dropdown.style.display = "none";

    wrap.appendChild(input);
    wrap.appendChild(dropdown);
    select.parentNode.insertBefore(wrap, select);

    let filtered = items;
    let activeIndex = -1;

    function removeTones(str) {
        return str.normalize("NFD")
                .replace(/[\u0300-\u036f]/g, "")
                .replace(/đ/g, "d").replace(/Đ/g, "D");
    }

    function render(list) {

        dropdown.innerHTML = "";

        if (list.length === 0) {
            const empty = document.createElement("div");
            empty.className = "ss-empty";
            empty.textContent = emptyText;
            dropdown.appendChild(empty);
            return;
        }

        list.forEach((item, idx) => {

            const el = document.createElement("div");
            el.className = "ss-item" + (idx === activeIndex ? " active" : "");

            el.innerHTML = "<div class='ss-item-main'></div>"
                    + (item.sub ? "<div class='ss-item-sub'></div>" : "");

            el.querySelector(".ss-item-main").textContent = item.label;

            if (item.sub) {
                el.querySelector(".ss-item-sub").textContent = item.sub;
            }

            // dùng mousedown thay vì click để chạy trước sự kiện "blur" của input
            el.addEventListener("mousedown", (e) => {
                e.preventDefault();
                choose(item);
            });

            dropdown.appendChild(el);

        });

    }

    function choose(item) {

        select.value = item.value;
        input.value = item.label;

        // Bắn sự kiện change để các script khác đang nghe trên select gốc
        // (ví dụ hiển thị thông tin hóa đơn ở addReceipt.jsp) vẫn hoạt động
        select.dispatchEvent(new Event("change"));

        close();

    }

    function open() {
        dropdown.style.display = "block";
    }

    function close() {
        dropdown.style.display = "none";
        activeIndex = -1;
    }

    function doFilter() {

        const q = removeTones(input.value.trim().toLowerCase());

        filtered = q === ""
                ? items
                : items.filter(it => removeTones(it.search).includes(q));

        activeIndex = -1;

        render(filtered);
        open();

    }

    function scrollActiveIntoView() {
        const activeEl = dropdown.querySelector(".ss-item.active");
        if (activeEl) {
            activeEl.scrollIntoView({block: "nearest"});
        }
    }

    input.addEventListener("focus", doFilter);
    input.addEventListener("input", doFilter);

    input.addEventListener("keydown", (e) => {

        if (dropdown.style.display === "none") {
            return;
        }

        if (e.key === "ArrowDown") {

            e.preventDefault();
            activeIndex = Math.min(activeIndex + 1, filtered.length - 1);
            render(filtered);
            scrollActiveIntoView();

        } else if (e.key === "ArrowUp") {

            e.preventDefault();
            activeIndex = Math.max(activeIndex - 1, 0);
            render(filtered);
            scrollActiveIntoView();

        } else if (e.key === "Enter") {

            e.preventDefault();

            if (activeIndex >= 0 && filtered[activeIndex]) {
                choose(filtered[activeIndex]);
            }

        } else if (e.key === "Escape") {

            close();

        }

    });

    // Đóng dropdown khi click ra ngoài
    document.addEventListener("click", (e) => {
        if (!wrap.contains(e.target)) {
            close();
        }
    });

    // Nếu select gốc đã có sẵn giá trị (trang sửa / có filter GET) thì
    // hiển thị đúng nhãn tương ứng ngay từ đầu
    if (select.value) {
        const current = items.find(it => it.value === select.value);
        if (current) {
            input.value = current.label;
        }
    }

}
