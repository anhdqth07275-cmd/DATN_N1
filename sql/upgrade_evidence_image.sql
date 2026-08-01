/* ============================================================
   BỔ SUNG "ẢNH MINH CHỨNG" (evidence_image) CHO PHIẾU THU / PHIẾU CHI
   ------------------------------------------------------------
   - Cho phép người lập phiếu đính kèm 1 ảnh minh chứng (hóa đơn,
     biên lai, chứng từ chuyển khoản...) khi lập/sửa phiếu thu
     hoặc phiếu chi, nhằm tăng tính minh bạch trong quản lý tiền.
   - Cột lưu ĐƯỜNG DẪN TƯƠNG ĐỐI tới file ảnh trong thư mục
     uploads/ của ứng dụng web (vd: uploads/receipts/xxxx.jpg),
     KHÔNG lưu trực tiếp dữ liệu ảnh (BLOB) trong CSDL.
   - Cho phép NULL vì đây là trường tùy chọn, không bắt buộc.

   Chạy script này trên database QuanLyTaiChinhCongNo_lan1, SAU
   KHI đã có sẵn bảng Receipt và Expense_Voucher.
   ============================================================ */

USE QuanLyTaiChinhCongNo_lan1;
GO

-- 1. Thêm cột evidence_image cho bảng Receipt (Phiếu thu)
IF NOT EXISTS (
    SELECT * FROM sys.columns
    WHERE Name = 'evidence_image' AND Object_ID = Object_ID('Receipt')
)
BEGIN
    ALTER TABLE Receipt ADD evidence_image NVARCHAR(255) NULL;
END
GO

-- 2. Thêm cột evidence_image cho bảng Expense_Voucher (Phiếu chi)
IF NOT EXISTS (
    SELECT * FROM sys.columns
    WHERE Name = 'evidence_image' AND Object_ID = Object_ID('Expense_Voucher')
)
BEGIN
    ALTER TABLE Expense_Voucher ADD evidence_image NVARCHAR(255) NULL;
END
GO

PRINT N'Đã bổ sung cột Ảnh minh chứng (evidence_image) cho bảng Receipt và Expense_Voucher.';
