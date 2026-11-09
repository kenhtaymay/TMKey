# Checklist Play Console – TMKey

App: **TMKey - Telex cho Q25 và P26** (`vn.taymay.keyboard`)

## ✅ Đã xong
- Tạo app (Ứng dụng, Miễn phí, tiếng Việt)
- Chính sách quyền riêng tư: https://kenhtaymay.github.io/TMKey/privacy-policy.html
- Thông tin đăng nhập (quyền truy cập ứng dụng): **Không** có phần bị hạn chế
- Quảng cáo: **Không** chứa quảng cáo
- Đối tượng mục tiêu: **13-15, 16-17, 18+**
- Mã nhận dạng quảng cáo (Ad ID): **Không**
- Ứng dụng của chính phủ: **Không**
- Trang store: tên, mô tả ngắn, mô tả đầy đủ (đã lưu nháp)

## ⏳ Bạn cần tự làm (Play Console → Nội dung ứng dụng)

### 1. Tính năng tài chính
Tick **"Ứng dụng của tôi không cung cấp tính năng tài chính nào"** → Tiếp theo → Lưu.

### 2. Ứng dụng sức khoẻ
Không chọn tính năng sức khoẻ nào (hoặc tick "không có tính năng sức khoẻ") → Lưu.

### 3. An toàn dữ liệu (Data safety)
- Ứng dụng có thu thập hoặc chia sẻ dữ liệu người dùng thuộc loại bắt buộc không? → **Không**
- (Nếu hỏi) Toàn bộ dữ liệu có được mã hoá khi truyền không? → bỏ qua / không áp dụng
- Xem lại → Lưu

### 4. Mức phân loại nội dung (IARC)
- Email liên hệ: email của bạn
- Danh mục: **Tất cả các loại ứng dụng khác** (All other app types)
- Mọi câu hỏi về bạo lực, tình dục, ngôn ngữ, chất cấm, cờ bạc… → **Không**
- "Người dùng có thể tương tác/chia sẻ nội dung?" → **Không**
- "Có chia sẻ vị trí?" → **Không**; "Có mua hàng kỹ thuật số?" → **Không**
- "Ứng dụng có phải trình duyệt web/công cụ tìm kiếm?" → **Không**
- Lưu → Gửi

### 5. Tư cách người bán (Trader status – EU DSA)
Đây là thông tin pháp lý của bạn, tự quyết định:
- Nếu bạn **có** thu tiền (Nhắc Tốc Độ có doanh thu, tài khoản đã khai báo là người bán) → chọn **Có, là người bán** cho app này. Thông tin liên hệ người bán sẽ hiển thị công khai ở EU.
- Điều khoản dịch vụ: có thể để trống.

## 🖼 Đồ hoạ trang store (Phát triển → Trang thông tin trên Cửa hàng Play)
Các file nằm ở `docs/store/`:

| Ô trên Play Console | File |
|---|---|
| Biểu tượng ứng dụng (512×512) | `icon-512.png` |
| Hình ảnh nổi bật (1024×500) | `feature-graphic.png` |
| Ảnh chụp màn hình điện thoại (2–8 ảnh) | `screenshot-1-telex.png`, `screenshot-2-welcome.png`, `screenshot-3-telex-guide.png`, `screenshot-4-shortcuts.png` |

Danh mục ứng dụng (Cài đặt cửa hàng): **Công cụ (Tools)**. Email liên hệ hiển thị công khai: email của bạn.

## 🚀 Thử nghiệm khép kín (bắt buộc với tài khoản này)
1. Kiểm thử và phát hành → **Thử nghiệm khép kín** → tạo kênh (Alpha) → Tạo bản phát hành
2. Tải lên `app/build/outputs/bundle/playRelease/app-play-release.aab`
3. Tên bản phát hành: `1.1 (5)`; ghi chú: "Bản đầu tiên: gõ Telex cho Zinwa Q25/P26"
4. Người kiểm thử: tạo danh sách email (≥ 12 người, nên mời 15–20 để dự phòng) hoặc dùng Google Group
5. Gửi xét duyệt bản thử nghiệm khép kín
6. Gửi link tham gia thử nghiệm cho người kiểm thử; họ phải **chọn tham gia và giữ app cài đặt 14 ngày liên tục**
7. Sau 14 ngày → Trang tổng quan → **Đăng ký phát hành công khai** (trả lời vài câu hỏi về quá trình thử nghiệm)

Gợi ý tìm người kiểm thử: nhóm Facebook Zinwa Q25 / BlackBerry Việt Nam, khán giả Kênh Táy Máy.
