const track = document.getElementById("track");
const cards = Array.from(track.children);
const cardWidth = cards[0].offsetWidth + 20; // 380 + margin
let index = 0;

// Hàm di chuyển slider
function moveSlide(dir) {
    if (dir === 1) {
        // sang phải
        track.style.transition = "transform 0.6s ease-in-out";
        track.style.transform = `translateX(-${cardWidth}px)`;

        setTimeout(() => {
            track.appendChild(track.firstElementChild);
            track.style.transition = "none";
            track.style.transform = "translateX(0)";
        }, 600);
    } else {
        // sang trái
        track.insertBefore(track.lastElementChild, track.firstElementChild);
        track.style.transition = "none";
        track.style.transform = `translateX(-${cardWidth}px)`;

        setTimeout(() => {
            track.style.transition = "transform 0.6s ease-in-out";
            track.style.transform = "translateX(0)";
        }, 10);
    }
}

// Tự động chạy
setInterval(() => moveSlide(1), 3000);
// Khởi tạo bản đồ Việt Nam
var map = L.map('map').setView([16.0471, 108.2068], 6); // Trung tâm: Đà Nẵng

// Nền bản đồ từ OpenStreetMap
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 18,
    attribution: '&copy; OpenStreetMap contributors'
}).addTo(map);

// Biến lưu điểm bắt đầu và kết thúc
let startMarker, endMarker, routeControl;

// Thêm sự kiện click chọn điểm
map.on('click', function (e) {
    if (!startMarker) {
        startMarker = L.marker(e.latlng).addTo(map).bindPopup("Điểm đi").openPopup();
    } else if (!endMarker) {
        endMarker = L.marker(e.latlng).addTo(map).bindPopup("Điểm đến").openPopup();

        // Hiển thị tuyến đường
        routeControl = L.Routing.control({
            waypoints: [
                startMarker.getLatLng(),
                endMarker.getLatLng()
            ],
            routeWhileDragging: true
        }).addTo(map);
    } else {
        map.removeLayer(startMarker);
        map.removeLayer(endMarker);
        if (routeControl) map.removeControl(routeControl);
        startMarker = endMarker = routeControl = null;
    }
});

// Xử lý form đăng ký
document.getElementById("trip-form").addEventListener("submit", function (e) {
    e.preventDefault();
    alert("Đăng ký thành công! Hệ thống sẽ gửi thông tin đến email của bạn.");
});
