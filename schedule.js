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
// --- Khởi tạo map (giữ như cũ hoặc thay tâm và zoom) ---
var map = L.map('map', {zoomControl: true}).setView([16.0471, 108.2068], 6);

// Tile layer (OpenStreetMap) - bạn có thể thay provider nếu muốn
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 18,
    attribution: '&copy; OpenStreetMap contributors'
}).addTo(map);

// Style cho polygon hiển thị chủ quyền
const islandStyle = {
    color: '#c8102e',      // viền đỏ
    weight: 2,
    opacity: 0.9,
    fillColor: '#f7931e',  // nền cam nhạt
    fillOpacity: 0.35
};

// Hàm load GeoJSON và thêm label/popup
async function addClaimLayer(url, label) {
    try {
        const resp = await fetch(url);
        if (!resp.ok) throw new Error('Không tải được ' + url);
        const geojson = await resp.json();

        const layer = L.geoJSON(geojson, {
            style: islandStyle,
            onEachFeature: (feature, layer) => {
                // popup và tooltip rõ ràng
                const popupHtml = `<strong>${label}</strong><br>Thuộc chủ quyền: Việt Nam`;
                layer.bindPopup(popupHtml);
                layer.bindTooltip(label, {permanent: false, direction: 'center', className: 'island-tooltip'});
            }
        }).addTo(map);

        // zoom vừa đủ để thấy layer (nếu cần)
        // map.fitBounds(layer.getBounds(), {padding: [40,40]});
        return layer;
    } catch (err) {
        console.error(err);
    }
}

// Thêm hai layer (thay đường dẫn thành đường dẫn file bạn có)
addClaimLayer('./data/hoangsa.geojson', 'Hoàng Sa (Việt Nam)');
addClaimLayer('./data/truongsa.geojson', 'Trường Sa (Việt Nam)');

// Nếu muốn, thêm markers tên cụ thể (ví dụ đảo chính) hoặc label cố định
// Ví dụ thêm marker ở vị trí tham chiếu:
L.marker([16.6, 112.2]).addTo(map).bindPopup('<strong>Hoàng Sa (Việt Nam)</strong>');
L.marker([9.6, 115.4]).addTo(map).bindPopup('<strong>Trường Sa (Việt Nam)</strong>');

// Tùy chọn: đưa các layer lên trên tile để luôn hiển thị rõ
// (Leaflet tự xử lý, nhưng ta có thể set zIndex nếu dùng pane)

// Xử lý form đăng ký
document.getElementById("trip-form").addEventListener("submit", function (e) {
    e.preventDefault();
    alert("Đăng ký thành công! Hệ thống sẽ gửi thông tin đến email của bạn.");
});
