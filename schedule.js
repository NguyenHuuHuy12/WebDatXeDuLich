let index = 0;
const slides = document.querySelectorAll('.slide');
const slider = document.getElementById('slider');
const visibleCount = 5;
const total = slides.length;

function moveSlide(direction) {
    index += direction;
    if (index > total - visibleCount) index = 0;
    if (index < 0) index = total - visibleCount;

    const offset = -index * (slides[0].offsetWidth + 20);
    slider.style.transform = `translateX(${offset}px)`;
}

// Tự động chạy
setInterval(() => moveSlide(1), 4000);
