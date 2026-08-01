/* ============================================================
   BỔ SUNG "HẠN THANH TOÁN" (due_date) CHO HÓA ĐƠN
   ------------------------------------------------------------
   - Hạn thanh toán do người dùng nhập khi LẬP hóa đơn (bắt buộc
     >= ngày lập hóa đơn - được kiểm tra ở tầng ứng dụng).
   - Khi công nợ được tạo từ hóa đơn (DebtDAO.createFromInvoice),
     hạn thanh toán của công nợ (Debt.due_date) sẽ LẤY THEO hạn
     thanh toán của chính hóa đơn đó, thay vì cộng cứng 30 ngày
     như trước đây.

   Chạy script này trên database QuanLyTaiChinhCongNo_lan1, SAU
   KHI đã có sẵn bảng Invoice.
   ============================================================ */

USE QuanLyTaiChinhCongNo_lan1;
GO

-- 1. Thêm cột due_date (cho phép NULL trước để không phá dữ liệu cũ)
IF NOT EXISTS (
    SELECT * FROM sys.columns
    WHERE Name = 'due_date' AND Object_ID = Object_ID('Invoice')
)
BEGIN
    ALTER TABLE Invoice ADD due_date DATE NULL;
END
GO

-- 2. Dữ liệu cũ (nếu có) chưa có hạn thanh toán -> tạm gán
--    invoice_date + 30 ngày (giữ đúng quy tắc mặc định trước đây)
--    để cột có thể chuyển sang NOT NULL an toàn.
UPDATE Invoice
SET due_date = DATEADD(DAY, 30, invoice_date)
WHERE due_date IS NULL;
GO

-- 3. Từ nay bắt buộc mọi hóa đơn phải có hạn thanh toán
IF EXISTS (
    SELECT * FROM sys.columns
    WHERE Name = 'due_date' AND Object_ID = Object_ID('Invoice')
      AND is_nullable = 1
)
BEGIN
    ALTER TABLE Invoice ALTER COLUMN due_date DATE NOT NULL;
END
GO

PRINT N'Đã bổ sung cột Hạn thanh toán (due_date) cho bảng Invoice.';
