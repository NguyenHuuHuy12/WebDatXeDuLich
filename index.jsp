<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Auto Cars - Dịch vụ đặt xe du lịch</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
</head>

<body>
<div class="wrapper">

<header>
    <a href="${pageContext.request.contextPath}/" class="logo">Auto Cars</a>
    <nav>
        <c:if test="${not empty sessionScope.user && sessionScope.user.role == 'ADMIN'}">
            <a href="${pageContext.request.contextPath}/admin/dashboard">Admin</a>
        </c:if>
        <a href="${pageContext.request.contextPath}/" class="active">Trang chủ</a>
        <a href="${pageContext.request.contextPath}/cars">Loại xe</a>
        <a href="${pageContext.request.contextPath}/booking">Đặt lịch đi</a>
        <a href="${pageContext.request.contextPath}/combo">Combo du lịch</a>
        <a href="${pageContext.request.contextPath}/history">Lịch sử đặt xe</a>
        <a href="${pageContext.request.contextPath}/contact">Đánh giá & phản hồi</a>
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <span style="color: #ffcc00;">Xin chào, ${sessionScope.user.fullName}</span>
                <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login">Đăng nhập/Đăng ký</a>
            </c:otherwise>
        </c:choose>
    </nav>
</header>

<main>
    <section class="hero">
        <h2>Đặt Xe Du Lịch Dễ Dàng Hơn Bao Giờ Hết</h2>
        <p>Chọn điểm đến, ngày đi và số ghế — hệ thống sẽ báo giá nhanh chóng cho bạn.</p>
    </section>

    <div class="form-container">
        <form id="formDatXe" onsubmit="xuLyForm(event)">
            <label for="diemDi">Điểm đi:</label>
            <input type="text" id="diemDi" placeholder="Ví dụ: Hà Nội" required>

            <label for="diemDen">Điểm đến:</label>
            <input type="text" id="diemDen" placeholder="Ví dụ: Đà Nẵng" required>

            <label for="ngayDi">Ngày đi:</label>
            <input type="date" id="ngayDi" required>

            <label for="ngayVe">Ngày về:</label>
            <input type="date" id="ngayVe">

            <label for="soGhe">Số ghế:</label>
            <select id="soGhe">
                <c:forEach begin="1" end="20" var="i">
                    <option value="${i}">${i}</option>
                </c:forEach>
            </select>

            <button type="submit">Xem Báo Giá</button>
            <button type="button" onclick="lamMoi()">Làm Mới</button>
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
    function xuLyForm(e) {
        e.preventDefault();
        const diemDi = document.getElementById('diemDi').value;
        const diemDen = document.getElementById('diemDen').value;
        const ngayDi = document.getElementById('ngayDi').value;
        const ngayVe = document.getElementById('ngayVe').value;
        const soGhe = document.getElementById('soGhe').value;

        // Tính giá đơn giản
        const giaCoSo = 200000;
        const gia = (soGhe * giaCoSo).toLocaleString('vi-VN');

        alert('Báo giá tạm tính:\nTừ: ' + diemDi + '\nĐến: ' + diemDen +
              '\nNgày đi: ' + ngayDi + '\nNgày về: ' + (ngayVe || 'Không') +
              '\nSố ghế: ' + soGhe + '\nTổng giá: ' + gia + ' VND\n\n' +
              'Vui lòng đăng nhập để đặt xe!');

        // Redirect to booking page
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                window.location.href = '${pageContext.request.contextPath}/booking';
            </c:when>
            <c:otherwise>
                if (confirm('Bạn cần đăng nhập để đặt xe. Chuyển đến trang đăng nhập?')) {
                    window.location.href = '${pageContext.request.contextPath}/login';
                }
            </c:otherwise>
        </c:choose>
    }

    function lamMoi() {
        document.getElementById('formDatXe').reset();
    }
</script>

</body>
</html>