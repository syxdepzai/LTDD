# 🎥 SCRIPT THUYẾT TRÌNH - 3 BÀI TẬP (7-10 PHÚT)

---

## 🎬 PHẦN 1 – MỞ ĐẦU (30–40 giây)

**“Xin chào thầy và các bạn.**  \
Em là **[Tên]**, sinh viên lớp **[Lớp]**.

Trong bài thuyết trình hôm nay, em sẽ giới thiệu sản phẩm thực hành Android của mình với **3 phần chính**:
- **Bài 1:** Custom Adapter cho **ListView**  \
- **Bài 2:** Custom Adapter cho **GridView**  \
- **Bài 3:** Danh sách bài hát với **RecyclerView**

Tất cả đều được em code trong cùng một app Android, có màn hình **Menu** để chọn từng bài.  \
Bây giờ em xin trình bày lần lượt từ phần lý thuyết đến code và demo.”

---

## 📚 PHẦN 2 – LÝ THUYẾT CHUNG (1–2 phút)

**“Trước hết, em xin giải thích sơ qua lý thuyết để lát nữa xem code sẽ dễ hiểu hơn.**

Theo tài liệu trang 139, **Adapter** là cầu nối giữa dữ liệu và View: dữ liệu của mình thường nằm trong **ArrayList**, còn View là **ListView, GridView hoặc RecyclerView**. **Custom Adapter** là khi mình **tự thiết kế lại giao diện từng dòng** chứ không dùng layout mặc định của hệ thống.

Điểm quan trọng là **ViewHolder Pattern** (trang 160–162): ViewHolder giữ reference các View để không phải gọi `findViewById()` nhiều lần. Điều này giúp:
- Giảm số lần tìm View tốn kém  \
- Danh sách **cuộn mượt hơn**, nhất là khi có nhiều item

**So sánh nhanh 3 loại:**
- **ListView:** Hiển thị danh sách dọc, ViewHolder dùng hay không dùng đều được, thích hợp cho các danh sách đơn giản.  \
- **GridView:** Cũng là AdapterView nhưng hiển thị dạng **lưới 2 chiều**, phù hợp cho gallery ảnh, icon.  \
- **RecyclerView:** Phiên bản nâng cấp, ViewHolder **bắt buộc**, dùng **LayoutManager** linh hoạt, hỗ trợ **ItemAnimator, ItemDecoration**, được Google khuyến nghị dùng cho app hiện đại.

Sau đây em sẽ trình bày lần lượt 3 bài mà em đã làm.”

---

## 💻 PHẦN 3 – BÀI 1: CUSTOM ADAPTER LISTVIEW (2–3 phút)

> **[Chạy app → Màn hình Menu → Bấm “BÀI 1: CUSTOM ADAPTER LISTVIEW”]**

**“Đầu tiên là Bài 1 – Custom Adapter ListView.**

### 1. Giao diện tổng quan

Khi vào màn hình này, thầy sẽ thấy:
- Tiêu đề: **“LISTVIEW – DANH SÁCH MÔN HỌC”**
- Bên dưới là một **ListView** hiển thị khoảng **20 môn học**
- Mỗi dòng có: một hình tròn màu (icon), tên môn học và mô tả ngắn

### 2. Bước 1 – Model `MonHoc.java`

Em tạo class `MonHoc` với 3 thuộc tính:
- `name`: tên môn học  \
- `desc`: mô tả môn học  \
- `pic`: id hình ảnh trong thư mục `drawable`

Class này giúp em **đóng gói dữ liệu 1 môn học thành 1 object**, thay vì dùng nhiều mảng rời rạc.

### 3. Bước 2 – Layout dòng `row_monhoc.xml`

Layout cho **một dòng** ListView:
- `LinearLayout` ngang  \
- Bên trái là `ImageView` 60x60dp hiển thị icon màu  \
- Bên phải là `LinearLayout` dọc chứa 2 `TextView`: tên và mô tả môn học

Mỗi item trong ListView sẽ dùng lại layout này.

### 4. Bước 3 – Layout màn hình `activity_listview.xml`

Layout chính gồm:
- Một `TextView` tiêu đề ở trên  \
- Một `ListView` bên dưới, id là `listViewMonHoc`, để gắn Adapter và hiển thị danh sách.

### 5. Bước 4 – Custom Adapter `MonHocAdapter.java`

`MonHocAdapter` **kế thừa** `BaseAdapter` và override 4 phương thức:
- `getCount()`: trả về số lượng môn học  \
- `getItem()` và `getItemId()`: trả về item và id tương ứng  \
- **`getView()`**: quyết định mỗi dòng hiển thị như thế nào

Trong `getView()` em áp dụng **ViewHolder Pattern**:
- Nếu `convertView == null`:
  - Inflate layout `row_monhoc`  \
  - Tạo `ViewHolder`, ánh xạ `ImageView` và các `TextView`  \
  - Gọi `setTag(viewHolder)` để lưu lại
- Nếu `convertView != null`:
  - Lấy lại `viewHolder` bằng `getTag()`  \
  - **Không cần `findViewById()` nữa**

Sau đó em lấy `MonHoc` tại vị trí `position` và gán dữ liệu vào các View.

### 6. Bước 5 – `ListViewActivity.java`

Trong `onCreate()` của `ListViewActivity`:
- Khởi tạo `ArrayList<MonHoc>`  \
- `findViewById` tới `listViewMonHoc`  \
- Gọi `themDuLieu()` để thêm khoảng 20 môn học mẫu  \
- Tạo `MonHocAdapter` và `setAdapter()` cho ListView
- Bắt sự kiện:
  - **Click**: hiện `Toast` “Bạn chọn: tên môn học”  \
  - **Long click**: hiện `Toast` “Long Click: tên môn học”

**[Demo nhanh:** click vài môn, cuộn lên xuống để thầy thấy danh sách hoạt động mượt nhờ ViewHolder.]”

---

## 🔲 PHẦN 4 – BÀI 2: CUSTOM ADAPTER GRIDVIEW (1–2 phút)

> **[Quay lại Menu → Bấm “BÀI 2: CUSTOM ADAPTER GRIDVIEW”]**

**“Tiếp theo là Bài 2 – Custom Adapter GridView.**

Ở bài này, em **tái sử dụng lại** model `MonHoc`, adapter `MonHocAdapter` và layout dòng `row_monhoc.xml`. Điểm khác chủ yếu là **cách hiển thị**.

### 1. Layout `activity_gridview.xml`

Em dùng `GridView` với các thuộc tính:
- `numColumns="2"` → hiển thị **2 cột**
- `horizontalSpacing` và `verticalSpacing` → tạo khoảng trống giữa các ô

Nhờ đó, các môn học được hiển thị dạng lưới, nhìn trực quan hơn khi có nhiều item.

### 2. `GridViewActivity.java`

Trong `GridViewActivity`:
- Khởi tạo `arrayListMonHoc` và `gridViewMonHoc`  \
- Gọi `themDuLieu()` để thêm khoảng 12 môn học  \
- Tạo `MonHocAdapter` và `setAdapter()` cho `gridViewMonHoc`
- Bắt sự kiện **click** và **long click** tương tự ListView

**[Demo:** cho thầy xem lưới 2 cột, click vài ô, cuộn xuống.]  \
Em nhấn mạnh rằng GridView phù hợp khi muốn **tận dụng không gian màn hình** và trình bày dữ liệu dạng ô, ví dụ gallery ảnh hoặc menu icon.”

---

## ⚡ PHẦN 5 – BÀI 3: RECYCLERVIEW (2–3 phút)

> **[Quay lại Menu → Bấm “BÀI 3: RECYCLERVIEW”]**

**“Cuối cùng là Bài 3 – RecyclerView.**

Đây là control **hiện đại nhất**, trong tài liệu trang 186–198 và được Google khuyến nghị dùng thay cho ListView trong các app mới.

RecyclerView là **phiên bản nâng cấp**:
- ViewHolder **bắt buộc** → luôn dùng mô hình tối ưu  \
- Dùng **LayoutManager** để quyết định hiển thị dọc, ngang, lưới…  \
- Hỗ trợ **ItemAnimator, ItemDecoration**, rất dễ mở rộng về sau

### 1. Model `SongModel.java`

Em tạo model `SongModel` implements `Serializable` với 4 thuộc tính:  \
`mCode`, `mTitle`, `mLyric`, `mArtist` – đại diện cho một bài hát.

### 2. Layout dòng `row_item_song.xml`

Layout dòng gồm 4 `TextView` xếp dọc:
- Mã bài hát (code) – chữ nhỏ, màu xám
- Tiêu đề (title) – chữ to, đậm
- Lời bài hát (lyric) – mô tả
- Ca sĩ (artist) – chữ nhỏ, xám

### 3. Adapter `SongAdapter.java`

`SongAdapter` kế thừa `RecyclerView.Adapter<SongAdapter.SongViewHolder>`:
- `onCreateViewHolder()`: Inflate `row_item_song` và tạo `SongViewHolder`
- `onBindViewHolder()`: Lấy bài hát theo `position` và gán dữ liệu vào 4 `TextView`
- `getItemCount()`: Trả về số lượng bài hát trong danh sách

`SongViewHolder` kế thừa `RecyclerView.ViewHolder`, bên trong em:
- Ánh xạ `tvCode`, `tvTitle`, `tvLyric`, `tvArtist`
- Gán `OnClickListener` cho `itemView`: dùng `getAdapterPosition()` để lấy vị trí, sau đó show `Toast("Bạn chọn: " + tên bài hát)`.

### 4. `RecyclerViewActivity.java`

Trong `onCreate()` của `RecyclerViewActivity`:
- `setContentView(R.layout.activity_recyclerview)`  \
- Ánh xạ `rvSongs`  \
- Khởi tạo `mSongList = new ArrayList<>()` và gọi `themDuLieu()` để thêm khoảng 15 bài hát mẫu  \
- Tạo `SongAdapter` và gán bằng `rvSongs.setAdapter(mSongAdapter)`  \
- Rất quan trọng: gọi  \
  ```java
  rvSongs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
  ```  \
  Nếu không set LayoutManager thì RecyclerView **không hiển thị gì**.

**[Demo:** scroll danh sách bài hát, click vài dòng để Toast xuất hiện.]

Em nhấn mạnh: nhờ bắt buộc dùng ViewHolder và cơ chế tái sử dụng View, RecyclerView có **hiệu suất tốt hơn** và dễ mở rộng hơn rất nhiều so với ListView.”\n
---

## 🎯 PHẦN 6 – SO SÁNH & KẾT LUẬN (1 phút)

**“Tóm lại, em đã trình bày 3 bài:**

### So sánh nhanh:

| Đặc điểm | ListView | GridView | RecyclerView |
|----------|----------|----------|--------------|
| **Hiển thị** | Danh sách dọc | Lưới 2D | Linh hoạt (list, grid, staggered) |
| **ViewHolder** | Có thể dùng | Có thể dùng | **BẮT BUỘC** |
| **Hiệu suất** | Trung bình | Trung bình | **Cao nhất** |
| **Animation** | Hạn chế | Hạn chế | Có `ItemAnimator` |
| **Khuyến nghị** | Dự án cũ, đơn giản | Gallery/icon | App hiện đại |

### Kết luận

- **ListView:** Đơn giản, dễ dùng, phù hợp danh sách nhỏ, code nhanh.  \
- **GridView:** Hiển thị dạng lưới, phù hợp gallery, menu nhiều biểu tượng.  \
- **RecyclerView:** Mạnh mẽ nhất, linh hoạt nhất, hiệu suất cao, là lựa chọn chuẩn cho các app Android hiện đại.

Cả 3 bài của em đều:
- Dùng **Custom Adapter** để tùy chỉnh giao diện từng dòng  \
- Áp dụng **ViewHolder Pattern** để tối ưu hiệu suất  \
- Được triển khai bám sát theo tài liệu của thầy.

**Em xin cảm ơn thầy và các bạn đã lắng nghe.**”

---

## ⏱️ THỜI LƯỢNG DỰ KIẾN

| Phần | Thời gian |
|------|-----------|
| Mở đầu | ~30–40s |
| Lý thuyết chung | ~1 phút |
| Bài 1 – ListView | ~2–3 phút |
| Bài 2 – GridView | ~1–2 phút |
| Bài 3 – RecyclerView | ~2–3 phút |
| So sánh & Kết luận | ~1 phút |

Tổng: **khoảng 7–10 phút**, phù hợp yêu cầu.

---

## ✅ CHECKLIST TRƯỚC KHI THUYẾT TRÌNH

- [ ] Build app thành công, không lỗi  \
- [ ] Test cả 3 bài chạy OK (Menu, ListView, GridView, RecyclerView)  \
- [ ] Đọc script 2–3 lần để nói trôi chảy  \
- [ ] Chuẩn bị app sẵn trên emulator/điện thoại  \
- [ ] Tắt thông báo, tránh popup làm gián đoạn  \
- [ ] Zoom font Android Studio/Emulator cho dễ nhìn

**Chúc bạn thuyết trình thật tự tin và mượt mà!**
