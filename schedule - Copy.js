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
