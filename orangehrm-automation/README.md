# 🍊 OrangeHRM Automation Test Suite

> Bộ kiểm thử tự động hệ thống quản lý nhân sự OrangeHRM  
> Sử dụng **Selenium WebDriver + TestNG + Page Object Model (POM)**

---

## 📋 Tổng quan

| Thông tin | Chi tiết |
|-----------|----------|
| **Ứng dụng** | OrangeHRM Demo (opensource-demo.orangehrmlive.com) |
| **Ngôn ngữ** | Java 11+ |
| **Build Tool** | Maven |
| **Test Framework** | TestNG 7.9 |
| **Automation Tool** | Selenium WebDriver 4.18 |
| **Mô hình thiết kế** | Page Object Model (POM) |
| **Báo cáo** | ExtentReports 5 (HTML) |
| **Logging** | Log4j2 |

---

## 🏗️ Cấu trúc Project

```
orangehrm-automation/
├── pom.xml                                    # Maven dependencies
├── README.md
│
├── src/
│   ├── main/java/com/orangehrm/
│   │   ├── config/
│   │   │   └── ConfigReader.java              # Đọc config.properties
│   │   ├── pages/                             # PAGE OBJECT MODEL
│   │   │   ├── BasePage.java                  # Lớp cha - method dùng chung
│   │   │   ├── LoginPage.java                 # Trang đăng nhập
│   │   │   ├── DashboardPage.java             # Trang Dashboard
│   │   │   ├── EmployeeListPage.java          # DS nhân viên (PIM)
│   │   │   ├── AddEmployeePage.java           # Form thêm/sửa nhân viên
│   │   │   ├── LeaveListPage.java             # DS nghỉ phép
│   │   │   └── AdminPage.java                 # Quản lý User hệ thống
│   │   └── utils/
│   │       ├── DriverManager.java             # Quản lý WebDriver
│   │       ├── ExtentReportManager.java       # Quản lý báo cáo HTML
│   │       └── ScreenshotUtils.java           # Chụp screenshot
│   │
│   └── test/
│       ├── java/com/orangehrm/
│       │   ├── listeners/
│       │   │   └── TestListener.java          # TestNG Listener
│       │   └── tests/
│       │       ├── BaseTest.java              # Lớp cha cho Test
│       │       ├── LoginTest.java             # ✅ 8 test cases Login
│       │       ├── DashboardTest.java         # ✅ 4 test cases Dashboard
│       │       ├── EmployeeTest.java          # ✅ 7 test cases PIM
│       │       ├── AdminTest.java             # ✅ 5 test cases Admin
│       │       └── LeaveTest.java             # ✅ 4 test cases Leave
│       └── resources/
│           ├── config.properties              # Cấu hình môi trường
│           ├── testng.xml                     # Cấu hình test suite
│           └── log4j2.xml                     # Cấu hình logging
│
├── reports/                                   # Báo cáo HTML (tự sinh)
├── screenshots/                               # Screenshot khi FAIL (tự sinh)
└── logs/                                      # Log file (tự sinh)
```

---

## 📦 Yêu cầu môi trường

- **Java JDK** 11 trở lên
- **Maven** 3.8+
- **Google Chrome** (phiên bản mới nhất)
- Kết nối Internet (truy cập OrangeHRM Demo)

---

## ⚡ Cài đặt & Chạy

### 1. Clone / tải project

```bash
git clone <repository-url>
cd orangehrm-automation
```

### 2. Cài dependencies

```bash
mvn clean install -DskipTests
```

### 3. Chạy toàn bộ test suite

```bash
mvn test
```

### 4. Chạy theo nhóm (group)

```bash
# Chỉ Smoke Tests (chạy nhanh ~5 phút)
mvn test -Dgroups=smoke

# Regression Tests đầy đủ
mvn test -Dgroups=regression

# Chỉ test Login
mvn test -Dtest=LoginTest

# Chỉ test Employee
mvn test -Dtest=EmployeeTest
```

### 5. Chạy Headless (không hiện browser)

Sửa file `src/test/resources/config.properties`:
```properties
headless=true
```

---

## 🧪 Danh sách Test Cases

### 🔐 Login Module (8 test cases)
| Test ID | Mô tả | Nhóm |
|---------|-------|------|
| TC_LOGIN_001 | Đăng nhập thành công với Admin | smoke, regression |
| TC_LOGIN_002 | Đăng nhập thất bại - sai mật khẩu | regression, negative |
| TC_LOGIN_003 | Đăng nhập thất bại - username sai | regression, negative |
| TC_LOGIN_004 | Đăng nhập với thông tin trống | regression, negative |
| TC_LOGIN_005 | Logo OrangeHRM hiển thị | smoke |
| TC_LOGIN_006 | Kiểm tra phân biệt hoa/thường | regression |
| TC_LOGIN_007 | Đăng xuất thành công | smoke, regression |
| TC_LOGIN_008 | Bảo mật - truy cập Dashboard không qua login | security |

### 🏠 Dashboard Module (4 test cases)
| Test ID | Mô tả | Nhóm |
|---------|-------|------|
| TC_DASH_001 | Dashboard hiển thị sau đăng nhập | smoke |
| TC_DASH_002 | Tên người dùng hiển thị đúng | smoke |
| TC_DASH_003 | Menu điều hướng đầy đủ | smoke, regression |
| TC_DASH_004 | Widgets hiển thị trên Dashboard | regression |

### 👥 Employee Module / PIM (7 test cases)
| Test ID | Mô tả | Nhóm |
|---------|-------|------|
| TC_EMP_001 | Xem danh sách nhân viên | smoke, regression |
| TC_EMP_002 | Thêm nhân viên mới thành công | regression, crud |
| TC_EMP_003 | Thêm nhân viên - thiếu Last Name | regression, negative |
| TC_EMP_004 | Tìm kiếm nhân viên theo tên | regression, search |
| TC_EMP_005 | Tìm kiếm nhân viên không tồn tại | regression, negative |
| TC_EMP_006 | Reset bộ lọc tìm kiếm | regression, search |
| TC_EMP_007 | Employee ID tự động tạo | regression |

### ⚙️ Admin Module (5 test cases)
| Test ID | Mô tả | Nhóm |
|---------|-------|------|
| TC_ADMIN_001 | Truy cập module Admin | smoke |
| TC_ADMIN_002 | Danh sách người dùng hệ thống | regression |
| TC_ADMIN_003 | Tìm kiếm user theo username | regression, search |
| TC_ADMIN_004 | Tìm kiếm user không tồn tại | regression, negative |
| TC_ADMIN_005 | Reset bộ lọc Admin | regression |

### 🌴 Leave Module (4 test cases)
| Test ID | Mô tả | Nhóm |
|---------|-------|------|
| TC_LEAVE_001 | Truy cập module Leave | smoke |
| TC_LEAVE_002 | Xem danh sách nghỉ phép | regression |
| TC_LEAVE_003 | Lọc nghỉ phép theo ngày | regression, search |
| TC_LEAVE_004 | Reset bộ lọc Leave | regression |

**Tổng cộng: 28 test cases**

---

## 📊 Xem Báo Cáo

Sau khi chạy test, mở file báo cáo HTML trong thư mục `reports/`:

```
reports/OrangeHRM_TestReport_YYYYMMDD_HHmmss.html
```

Báo cáo bao gồm:
- ✅ Kết quả từng test case (PASS/FAIL/SKIP)
- 📸 Screenshot tự động khi test thất bại
- ⏱️ Thời gian thực thi
- 📈 Biểu đồ thống kê tổng quan

---

## ⚙️ Cấu hình Nâng cao

### Thay đổi môi trường test
Sửa file `src/test/resources/config.properties`:

```properties
# Đổi sang môi trường staging
base.url=https://your-staging-orangehrm.com/web/index.php

# Thông tin đăng nhập
admin.username=YourAdmin
admin.password=YourPassword

# Timeout
implicit.wait=15
explicit.wait=30
```

---

## 🔧 Nguyên tắc POM được áp dụng

1. **Tách biệt hoàn toàn** logic test (`tests/`) và logic tương tác UI (`pages/`)
2. **BasePage** chứa tất cả method dùng chung (click, type, wait...)
3. **Page Object** không chứa assert - chỉ trả về Page Object hoặc dữ liệu
4. **BaseTest** quản lý setup/teardown vòng đời WebDriver
5. **TestListener** tự động chụp screenshot và ghi log
6. **ThreadLocal** đảm bảo tương thích chạy song song (parallel)

---

## 👨‍💻 Thông tin

- **Target URL**: https://opensource-demo.orangehrmlive.com
- **Tài khoản test**: Admin / admin123
- **Framework**: Selenium WebDriver 4.x + TestNG + POM
