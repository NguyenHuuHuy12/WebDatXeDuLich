<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Loại xe - Auto Cars</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/car.css">
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
        <a href="${pageContext.request.contextPath}/cars" class="active">Loại xe</a>
        <a href="${pageContext.request.contextPath}/booking">Đặt lịch đi</a>
        <a href="${pageContext.request.contextPath}/combo">Combo du lịch</a>
        <a href="${pageContext.request.contextPath}/history">Lịch sử đặt xe</a>
        <a href="${pageContext.request.contextPath}/contact">Đánh giá & phản hồi</a>
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login">Đăng nhập/Đăng ký</a>
            </c:otherwise>
        </c:choose>
    </nav>
</header>

<main>
    <section class="intro">
        <h1>Các dòng xe phục vụ</h1>
        <p>Chúng tôi cung cấp đa dạng các loại xe phù hợp cho du lịch, công tác và sự kiện.</p>
    </section>

    <section class="filter-bar">
        <label for="seatFilter">Phân loại xe:</label>
        <select id="seatFilter" onchange="filterCars()">
            <option value="all" ${empty param.seats ? 'selected' : ''}>Tất cả</option>
            <option value="4" ${param.seats == '4' ? 'selected' : ''}>Xe 4 chỗ</option>
            <option value="7" ${param.seats == '7' ? 'selected' : ''}>Xe 7 chỗ</option>
            <option value="12" ${param.seats == '12' ? 'selected' : ''}>Xe 12 chỗ</option>
            <option value="16" ${param.seats == '16' ? 'selected' : ''}>Xe 16 chỗ</option>
        </select>
    </section>

    <section class="car-list">
        <c:choose>
            <c:when test="${not empty carTypes}">
                <c:forEach var="car" items="${carTypes}">
                    <div class="car-card" data-val="${car.seats}" onclick="toggleCard(this)">
                        <img src="${car.imageUrl}" alt="${car.name}">
                        <h3>${car.name}</h3>
                        <p>${car.description}</p>
                        <p><strong>${car.seats} chỗ ngồi</strong></p>
                        <p class="price">
                            <fmt:formatNumber value="${car.pricePerKm}" type="number"/> VNĐ/km
                        </p>
                        <div class="spec">
                            • Công suất: 170 HP<br>
                            • Nhiên liệu: Xăng<br>
                            • Số ghế: ${car.seats}<br>
                            • Truyền động: Tự động
                        </div>
                        <button class="btn-booking" onclick="bookCar(${car.carTypeId}); event.stopPropagation();">
                            Đặt xe ngay
                        </button>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p style="text-align: center; padding: 40px; color: #666;">
                    Hiện tại chưa có loại xe nào. Vui lòng quay lại sau!
                </p>
            </c:otherwise>
        </c:choose>
    </section>
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
    function filterCars() {
        const value = document.getElementById('seatFilter').value;
        if (value === 'all') {
            window.location.href = '${pageContext.request.contextPath}/cars';
        } else {
            window.location.href = '${pageContext.request.contextPath}/cars?seats=' + value;
        }
    }

    function toggleCard(card) {
        const allCards = document.querySelectorAll('.car-card');
        allCards.forEach(c => {
            if (c !== card) {
                c.classList.remove('expanded');
            }
        });
        card.classList.toggle('expanded');
    }

    function bookCar(carTypeId) {
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                window.location.href = '${pageContext.request.contextPath}/booking?carTypeId=' + carTypeId;
            </c:when>
            <c:otherwise>
                if (confirm('Bạn cần đăng nhập để đặt xe. Chuyển đến trang đăng nhập?')) {
                    window.location.href = '${pageContext.request.contextPath}/login';
                }
            </c:otherwise>
        </c:choose>
    }
</script>

<style>
.car-card {
    cursor: pointer;
    transition: all 0.3s;
}

.car-card.expanded {
    transform: scale(1.05);
    box-shadow: 0 6px 20px rgba(0,0,0,0.2);
    z-index: 10;
}

.car-card .spec {
    display: none;
    margin-top: 10px;
    padding: 10px;
    background: #f5f5f5;
    border-radius: 5px;
    font-size: 14px;
    text-align: left;
}

.car-card.expanded .spec {
    display: block;
}

.btn-booking {
    display: none;
    margin-top: 10px;
    background: #003366;
    color: white;
    border: none;
    padding: 10px 20px;
    border-radius: 5px;
    cursor: pointer;
    font-weight: 600;
}

.car-card.expanded .btn-booking {
    display: inline-block;
}

.btn-booking:hover {
    background: #002244;
}

.price {
    color: #ff6600;
    font-weight: 600;
    font-size: 16px;
}
</style>

</body>
</html>