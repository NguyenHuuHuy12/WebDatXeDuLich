<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt xe - Auto Cars</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Booking.css">
</head>
<body>
<div class="wrapper">

<header>
    <a href="${pageContext.request.contextPath}/" class="logo">Auto Cars</a>
    <nav>
        <c:if test="${sessionScope.user.role == 'ADMIN'}">
            <a href="${pageContext.request.contextPath}/admin/dashboard">Admin</a>
        </c:if>
        <a href="${pageContext.request.contextPath}/">Trang chủ</a>
        <a href="${pageContext.request.contextPath}/cars">Loại xe</a>
        <a href="${pageContext.request.contextPath}/booking" class="active">Đặt lịch đi</a>
        <a href="${pageContext.request.contextPath}/combo">Combo du lịch</a>
        <a href="${pageContext.request.contextPath}/history">Lịch sử đặt xe</a>
        <a href="${pageContext.request.contextPath}/contact">Đánh giá & phản hồi</a>
        <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
    </nav>
</header>

<main>
    <div class="container">
        <h1>Thông tin chuyến đi</h1>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <div class="card">
            <form method="POST" action="${pageContext.request.contextPath}/booking">
                <h3>Thông tin cá nhân</h3>
                <div class="row">
                    <input type="text" name="fullName" placeholder="Họ tên"
                           value="${sessionScope.user.fullName}" readonly>
                    <input type="text" name="phone" placeholder="SDT"
                           value="${sessionScope.user.phone}" required>
                    <input type="email" name="email" placeholder="Email"
                           value="${sessionScope.user.email}" readonly>
                </div>

                <h3>Chi tiết chuyến đi</h3>
                <div class="row">
                    <input type="text" name="fromLocation" placeholder="Điểm đi" required>
                    <input type="text" name="toLocation" placeholder="Điểm đến" required>
                    <input type="date" name="departureDate" placeholder="Ngày đi" required>
                    <input type="date" name="returnDate" placeholder="Ngày về (tùy chọn)">

                    <select name="carTypeId" id="carType" required onchange="calculatePrice()">
                        <option value="">Chọn loại xe</option>
                        <c:forEach var="car" items="${carTypes}">
                            <option value="${car.carTypeId}"
                                    data-price="${car.pricePerKm}"
                                    data-seats="${car.seats}"
                                    ${param.carTypeId == car.carTypeId ? 'selected' : ''}>
                                ${car.name} (${car.seats} chỗ) -
                                <fmt:formatNumber value="${car.pricePerKm}" type="number"/> VNĐ/km
                            </option>
                        </c:forEach>
                    </select>

                    <select name="numberOfSeats" id="numberOfSeats" required onchange="calculatePrice()">
                        <option value="">Số ghế</option>
                        <c:forEach begin="1" end="20" var="i">
                            <option value="${i}">${i}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="row">
                    <button type="button" class="btn" onclick="calculatePrice()">Kiểm tra giá</button>
                </div>
                <div id="priceInfo"></div>

                <h3>Thanh toán</h3>
                <div class="row">
                    <select name="paymentMethod" required>
                        <option value="CASH">Tiền mặt</option>
                        <option value="VNPAY">VNPay</option>
                        <option value="MOMO">Momo</option>
                    </select>
                </div>

                <h3>Ghi chú</h3>
                <textarea name="notes" placeholder="Ghi chú thêm (tùy chọn)" rows="3"></textarea>

                <div class="row">
                    <button type="submit" class="btn">Đặt xe</button>
                </div>
            </form>
        </div>
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
    function calculatePrice() {
        const carSelect = document.getElementById('carType');
        const seatsInput = document.getElementById('numberOfSeats');
        const priceInfo = document.getElementById('priceInfo');

        if (!carSelect.value || !seatsInput.value) {
            priceInfo.innerHTML = '<p style="color: #ff6600;">Vui lòng chọn loại xe và số ghế</p>';
            return;
        }

        const selectedOption = carSelect.options[carSelect.selectedIndex];
        const pricePerKm = parseFloat(selectedOption.getAttribute('data-price'));
        const maxSeats = parseInt(selectedOption.getAttribute('data-seats'));
        const requestedSeats = parseInt(seatsInput.value);

        if (requestedSeats > maxSeats) {
            priceInfo.innerHTML = '<p style="color: red;">Số ghế vượt quá sức chứa của xe (tối đa ' + maxSeats + ' chỗ)</p>';
            return;
        }

        // Tính giá ước tính (100km)
        const estimatedDistance = 100;
        const basePrice = pricePerKm * estimatedDistance;
        const seatMultiplier = 1.0 + (requestedSeats - 1) * 0.1;
        const totalPrice = Math.round(basePrice * seatMultiplier);

        priceInfo.innerHTML =
            '<p style="color: #003366; font-weight: 600; font-size: 18px;">' +
            'Giá ước tính: ' + totalPrice.toLocaleString('vi-VN') + ' VNĐ<br>' +
            '<small style="color: #666; font-size: 14px;">' +
            '(Ước tính cho khoảng cách 100km, giá cuối cùng sẽ được xác nhận sau)' +
            '</small></p>';
    }
</script>

<style>
.alert {
    padding: 15px;
    border-radius: 8px;
    margin-bottom: 20px;
}

.alert-error {
    background: #fee;
    color: #c00;
    border: 1px solid #fcc;
}

.alert-success {
    background: #efe;
    color: #0a0;
    border: 1px solid #cfc;
}

textarea {
    width: 100%;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 6px;
    font-family: inherit;
}

#priceInfo {
    margin-top: 15px;
    padding: 15px;
    background: #f5f8ff;
    border-radius: 8px;
}
</style>

</body>
</html>