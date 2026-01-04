<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Lịch sử đặt xe - Auto Cars</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/History.css">
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
        <a href="${pageContext.request.contextPath}/booking">Đặt lịch đi</a>
        <a href="${pageContext.request.contextPath}/combo">Combo du lịch</a>
        <a href="${pageContext.request.contextPath}/history" class="active">Lịch sử đặt xe</a>
        <a href="${pageContext.request.contextPath}/contact">Đánh giá & phản hồi</a>
        <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
    </nav>
</header>

<main>
    <div class="container">
        <h1>Lịch sử đặt xe</h1>

        <div class="card">
            <div class="list" id="list">
                <c:choose>
                    <c:when test="${not empty bookings}">
                        <c:forEach var="booking" items="${bookings}">
                            <div class="item">
                                <div class="date">
                                    <fmt:formatDate value="${booking.departureDate}" pattern="dd/MM/yyyy"/>
                                </div>
                                <div class="route">
                                    ${booking.fromLocation} → ${booking.toLocation}
                                    <div class="muted">Mã: ${booking.bookingCode}</div>
                                </div>
                                <div class="right">
                                    <span class="badge
                                        <c:choose>
                                            <c:when test="${booking.bookingStatus == 'PENDING'}">pending</c:when>
                                            <c:when test="${booking.bookingStatus == 'COMPLETED'}">done</c:when>
                                            <c:when test="${booking.bookingStatus == 'CANCELLED'}">cancel</c:when>
                                            <c:otherwise>pending</c:otherwise>
                                        </c:choose>">
                                        <c:choose>
                                            <c:when test="${booking.bookingStatus == 'PENDING'}">Đang chờ</c:when>
                                            <c:when test="${booking.bookingStatus == 'CONFIRMED'}">Đã xác nhận</c:when>
                                            <c:when test="${booking.bookingStatus == 'COMPLETED'}">Hoàn thành</c:when>
                                            <c:when test="${booking.bookingStatus == 'CANCELLED'}">Đã hủy</c:when>
                                            <c:otherwise>${booking.bookingStatus}</c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>
                                <div class="price">
                                    <fmt:formatNumber value="${booking.finalPrice}" type="number"/> VNĐ
                                </div>
                                <div class="actions">
                                    <button class="btn" onclick="viewDetail(${booking.bookingId})">
                                        Chi tiết
                                    </button>
                                    <c:if test="${booking.bookingStatus == 'PENDING' || booking.bookingStatus == 'CONFIRMED'}">
                                        <button class="btn btn-cancel" onclick="cancelBooking(${booking.bookingId})">
                                            Hủy
                                        </button>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="empty">
                            <p>Bạn chưa có chuyến đi nào</p>
                            <a href="${pageContext.request.contextPath}/booking" class="btn">Đặt xe ngay</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</main>

<!-- Modal chi tiết -->
<div class="modal" id="modal" style="display: none;">
    <div class="box">
        <h3>Chi tiết đặt xe</h3>
        <div class="modal-content" id="modalContent">
            <!-- Nội dung sẽ được load bằng AJAX hoặc thông qua servlet -->
        </div>
        <div class="modal-actions">
            <button class="btn" onclick="closeModal()">Đóng</button>
        </div>
    </div>
</div>

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
    function viewDetail(bookingId) {
        // TODO: Load chi tiết booking bằng AJAX
        const modal = document.getElementById('modal');
        const modalContent = document.getElementById('modalContent');

        modalContent.innerHTML = '<p>Đang tải...</p>';
        modal.style.display = 'flex';

        // Fetch booking details
        fetch('${pageContext.request.contextPath}/api/booking/' + bookingId)
            .then(response => response.json())
            .then(data => {
                modalContent.innerHTML = `
                    <div class="row"><div class="muted">Mã đặt:</div><div>\${data.bookingCode}</div></div>
                    <div class="row"><div class="muted">Ngày đi:</div><div>\${data.departureDate}</div></div>
                    <div class="row"><div class="muted">Điểm đi:</div><div>\${data.fromLocation}</div></div>
                    <div class="row"><div class="muted">Điểm đến:</div><div>\${data.toLocation}</div></div>
                    <div class="row"><div class="muted">Số ghế:</div><div>\${data.numberOfSeats}</div></div>
                    <div class="row"><div class="muted">Tổng tiền:</div><div>\${data.finalPrice.toLocaleString('vi-VN')} VNĐ</div></div>
                `;
            })
            .catch(error => {
                modalContent.innerHTML = '<p style="color: red;">Không thể tải thông tin</p>';
            });
    }

    function closeModal() {
        document.getElementById('modal').style.display = 'none';
    }

    function cancelBooking(bookingId) {
        if (!confirm('Bạn có chắc chắn muốn hủy chuyến đi này?')) {
            return;
        }

        fetch('${pageContext.request.contextPath}/api/booking/cancel/' + bookingId, {
            method: 'POST'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('Đã hủy chuyến đi thành công');
                location.reload();
            } else {
                alert('Không thể hủy: ' + data.message);
            }
        })
        .catch(error => {
            alert('Có lỗi xảy ra. Vui lòng thử lại');
        });
    }
</script>

<style>
.item {
    display: grid;
    grid-template-columns: 100px 1fr 150px 120px auto;
    gap: 15px;
    align-items: center;
    padding: 15px;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    margin-bottom: 12px;
    background: white;
}

.item:hover {
    background: #f9f9f9;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.date {
    font-weight: 600;
    color: #003366;
}

.route {
    font-size: 16px;
}

.muted {
    color: #666;
    font-size: 13px;
    margin-top: 4px;
}

.badge {
    display: inline-block;
    padding: 6px 12px;
    border-radius: 20px;
    font-size: 13px;
    font-weight: 600;
}

.badge.pending {
    background: #fff7ed;
    color: #c2410c;
}

.badge.done {
    background: #ecfdf5;
    color: #065f46;
}

.badge.cancel {
    background: #fef2f2;
    color: #991b1b;
}

.price {
    font-weight: 700;
    color: #ff6600;
    font-size: 16px;
}

.actions {
    display: flex;
    gap: 8px;
}

.btn-cancel {
    background: #dc2626;
}

.btn-cancel:hover {
    background: #b91c1c;
}

.empty {
    text-align: center;
    padding: 60px 20px;
}

.empty p {
    color: #666;
    font-size: 18px;
    margin-bottom: 20px;
}

.modal {
    position: fixed;
    inset: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    background: rgba(0,0,0,0.5);
    z-index: 1000;
}

.modal .box {
    background: white;
    padding: 30px;
    border-radius: 12px;
    max-width: 500px;
    width: 90%;
}

.modal .row {
    display: flex;
    justify-content: space-between;
    padding: 10px 0;
    border-bottom: 1px solid #eee;
}

.modal-actions {
    margin-top: 20px;
    text-align: right;
}
</style>

</body>
</html>