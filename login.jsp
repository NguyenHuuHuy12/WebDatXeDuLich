<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập / Đăng ký - Auto Cars</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
<div class="wrapper">

    <header>
        <a href="${pageContext.request.contextPath}/" class="logo">Auto Cars</a>
        <nav>
            <c:if test="${not empty sessionScope.user && sessionScope.user.role == 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/admin/dashboard">Admin</a>
            </c:if>
            <a href="${pageContext.request.contextPath}/">Trang chủ</a>
            <a href="${pageContext.request.contextPath}/cars">Loại xe</a>
            <a href="${pageContext.request.contextPath}/booking">Đặt lịch đi</a>
            <a href="${pageContext.request.contextPath}/combo">Combo du lịch</a>
            <a href="${pageContext.request.contextPath}/history">Lịch sử đặt xe</a>
            <a href="${pageContext.request.contextPath}/contact">Đánh giá & phản hồi</a>
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login" class="active">Đăng nhập/Đăng ký</a>
                </c:otherwise>
            </c:choose>
        </nav>
    </header>

    <main>
        <div class="auth-box">

            <!-- Hiển thị thông báo -->
            <c:if test="${not empty error}">
                <div class="alert alert-error">${error}</div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert alert-success">${success}</div>
            </c:if>

            <div class="tabs">
                <button id="btnLogin" class="active">Đăng nhập</button>
                <button id="btnRegister">Đăng ký</button>
            </div>

            <!-- Form Đăng nhập -->
            <form id="loginForm" class="form show" method="POST" action="${pageContext.request.contextPath}/login">
                <h2>Đăng nhập</h2>
                <input type="text" name="username" placeholder="Tên đăng nhập" required
                       value="${param.username}">
                <input type="password" name="password" placeholder="Mật khẩu" required>

                <button type="submit" class="btn primary">Đăng nhập</button>

                <p class="switch">Chưa có tài khoản?
                    <span id="switchToRegister">Đăng ký ngay</span>
                </p>
            </form>

            <!-- Form Đăng ký -->
            <form id="registerForm" class="form" method="POST" action="${pageContext.request.contextPath}/register">
                <h2>Đăng ký</h2>
                <input type="text" name="username" placeholder="Tên đăng nhập" required
                       value="${param.username}">
                <input type="text" name="fullName" placeholder="Họ và tên" required
                       value="${param.fullName}">
                <input type="text" name="phone" placeholder="Số điện thoại"
                       value="${param.phone}">
                <input type="email" name="email" placeholder="Email" required
                       value="${param.email}">
                <input type="password" name="password" placeholder="Mật khẩu" required>
                <input type="password" name="confirmPassword" placeholder="Nhập lại mật khẩu" required>

                <button type="submit" class="btn primary">Tạo tài khoản</button>

                <p class="switch">Đã có tài khoản?
                    <span id="switchToLogin">Đăng nhập ngay</span>
                </p>
            </form>

            <!-- Form Quên mật khẩu -->
            <form id="forgotForm" class="form" method="POST" action="${pageContext.request.contextPath}/forgot-password">
                <h2>Quên mật khẩu</h2>
                <input type="email" name="email" placeholder="Nhập email đã đăng ký" required>

                <button type="submit" class="btn primary">Gửi mã khôi phục</button>

                <p class="switch">Nhớ mật khẩu rồi?
                    <span id="backToLogin1">Quay lại đăng nhập</span>
                </p>
            </form>

            <!-- Form Đặt lại mật khẩu -->
            <form id="resetForm" class="form" method="POST" action="${pageContext.request.contextPath}/reset-password">
                <h2>Đặt lại mật khẩu</h2>
                <input type="text" name="code" placeholder="Mã xác nhận" required>
                <input type="password" name="newPassword" placeholder="Mật khẩu mới" required>
                <input type="password" name="confirmPassword" placeholder="Nhập lại mật khẩu" required>

                <button type="submit" class="btn primary">Cập nhật mật khẩu</button>

                <p class="switch">Quay lại trang đăng nhập?
                    <span id="backToLogin2">Đăng nhập</span>
                </p>
            </form>

        </div>
    </main>

    <footer>
        <div class="left">© 2025 Auto Cars. Mọi quyền được bảo lưu.</div>
        <div class="right">
            <a href="#">Chính sách bảo mật</a> |
            <a href="#">Điều khoản sử dụng</a> |
            <a href="#">Liên hệ</a>
        </div>
    </footer>

</div>

<script>
    // Toggle giữa các form
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');
    const forgotForm = document.getElementById('forgotForm');
    const resetForm = document.getElementById('resetForm');

    const btnLogin = document.getElementById('btnLogin');
    const btnRegister = document.getElementById('btnRegister');

    function showForm(formToShow) {
        [loginForm, registerForm, forgotForm, resetForm].forEach(form => {
            form.classList.remove('show');
        });
        formToShow.classList.add('show');
    }

    btnLogin.onclick = () => {
        btnLogin.classList.add('active');
        btnRegister.classList.remove('active');
        showForm(loginForm);
    };

    btnRegister.onclick = () => {
        btnRegister.classList.add('active');
        btnLogin.classList.remove('active');
        showForm(registerForm);
    };

    document.getElementById('switchToRegister').onclick = () => {
        btnRegister.click();
    };

    document.getElementById('switchToLogin').onclick = () => {
        btnLogin.click();
    };

    document.getElementById('backToLogin1').onclick = () => {
        btnLogin.click();
    };

    document.getElementById('backToLogin2').onclick = () => {
        btnLogin.click();
    };
</script>

</body>
</html>