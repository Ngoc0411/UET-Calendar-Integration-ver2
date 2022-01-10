## Cài đặt và cấu hình
### Cài đặt Java runtimes
Để chạy Fuse Tooling với phiên bản CodeReady Studio mới nhất, cần cài đặt cả Java 8 và Java 11 trên máy cục bộ của mình. Java 8 JVM là bắt buộc để khởi chạy các ứng dụng nhắm mục tiêu Fuse trên Karaf và Java 11 JVM là bắt buộc để chạy CodeReady Studio.
#### Install Java JDK 8
    • Bước 1: Vào website download của Oracle theo link sau: https://www.oracle.com/java/technologies/downloads/#java8
    • Bước 2: Chọn hệ điều hành và file cấu hình phù hợp với máy tính và download

    • Bước 3: Run file vừa download về.
            5.1.2. Install Java JDK 11
    • Bước 1: Vào website download của Oracle theo link sau: https://www.oracle.com/java/technologies/downloads/#java11
    • Bước 2: Chọn hệ điều hành và file cấu hình phù hợp với máy tính và download.

    • Bước 3: Run file vừa download về.
### Cài đặt Fuse on Karaf
Với dự án này, nhóm sử dụng phiên bản Fuse 7.9 on Karaf. Gói cài đặt tiêu chuẩn của Fuse 7.9 on Karaf có sẵn trên Red Hat Customer Portal, từ đây bạn có thể tải xuống.
#### Đăng ký tài khoản trên Red Hat Customer Portal.
    • Bước 1: Truy cập vào trang web Red Hat Customer Portal

    • Bước 2: Lựa chọn phần “Register” trên góc phải trang web để đăng ký tài khoản. Sau đó điền đầy đủ thông tin và ấn “Create my account”.

    • Bước 3: Sau khi đăng ký xong thì truy cập vào trang web Red Hat Customer Portal bằng tài khoản vừa đăng ký.
            5.2.2. Download CodeReady Studio installer và Fuse on Karaf installer
    • Bước 1: download CodeReady Studio installer.
    • Bước 2: download Fuse on Karaf installer.
#### Cài đặt cấu hình
    • Bước 1: Giải nén tập dữ liệu .zip vừa tải xuống cho Fuse on Apache Karaf vào một vị trí thuận tiện trên hệ thống tệp, FUSE_INSTALL.
    • Bước 2: Thêm người dùng quản trị viên vào Fuse runtime.
        ◦ Mở file FUSE_INSTALL/etc/users.properties trong một trình soạn thảo văn bản.
        ◦ Xóa ký tự # ở đầu dòng bắt đầu bằng #admin = admin.
        ◦ Xóa ký tự # ở đầu dòng bắt đầu bằng # _g_\:admingroup.
        ◦ Tùy chỉnh tên người dùng, USERNAME, và mật khẩu, PASSWORD, của mục nhập người dùng.

        ◦ Lưu file etc/users.properties
    • Bước 3: chạy code CodeReady Studio installer như sau:

    • Bước 4: Trong quá trình cài đặt:
        ◦ Chấp nhận các điều khoản và điều kiện.
        ◦ Chọn đường dẫn cài đặt mong muốn.
        ◦ Chọn Java 11 JVM.
        ◦ Tại bước Select Platforms and Servers, hãy cấu hình thời gian chạy Fuse on Karaf bằng cách ấn vào Add và duyệt đến vị trí của thư mục FUSE_INSTALL.
        ◦ Tại bước Select Additional Features to Install, chọn Red Hat Fuse Tooling.

    • Bước 5: CodeReady Studio khởi động. Khi hộp thoại Searching for runtimes xuất hiện, bấm OK để tạo Fuse on Karaf runtime.

    • Bước 6: Chuyển đổi JVM mặc định cho các dự án mới từ Java 11 sang Java 8:
        ◦ Di chuyển tới Window→Preferences→Java→Installed JREs.
        ◦ Trong Installed JREs, bấm Add để mở trình hướng dẫn Add JRE.

        ◦ Trong hộp thoại JRE Type, chọn Standard VM và bấm Next.
        ◦ Trong hộp thoại JRE Definition, hãy dán đường dẫn tuyệt đối đến thư mục chính của cài đặt Java 8 của bạn vào JRE home. Các trường khác trong hộp thoại sẽ tự động được điền. Nhấp vào Finish.

        ◦ Trong Installed JREs, chọn Java 8 runtime và ấn Apply.

        ◦ Di chuyển tới Window→Preferences→Java→Compiler. Trong JDK Compliance, đặt Compiler Compliance level thành 1.8. Nhấn vào Apply and Close.
