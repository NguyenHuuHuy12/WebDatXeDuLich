document.addEventListener("DOMContentLoaded", () => {
    const carGrid = document.getElementById("carGrid");
    const addCarBtn = document.getElementById("addCarBtn");
    const modal = document.getElementById("carModal");
    const closeModalBtn = document.getElementById("closeModalBtn");
    const saveCarBtn = document.getElementById("saveCarBtn");
    const modalTitle = document.getElementById("modalTitle");

    const carNameInput = document.getElementById("carName");
    const carImageInput = document.getElementById("carImage");

    let editIndex = null;

    // Danh sách xe mẫu
    let cars = [
        { name: "Xe 4 chỗ", image: "https://hyundaibinhthuan.vn/wp-content/uploads/Hyundai-Elantra-6-1.jpg" },
        { name: "Xe 7 chỗ", image: "https://otohonda.com.vn/wp-content/uploads/Danh-gia-Honda-BR-V-Xe-7-cho-gia-re-cua-Honda.jpg" },
        { name: "Xe 16 chỗ", image: "https://chothuexevip.vn/uploads/2021/03/Xe-Hyundai-Solati-16-cho-02.jpg" }
    ];

    function renderCars() {
        carGrid.innerHTML = "";
        cars.forEach((car, index) => {
            const card = document.createElement("div");
            card.className = "car-card";
            card.innerHTML = `
        <img src="${car.image}" alt="${car.name}">
        <div class="overlay"><p>${car.name}</p></div>
        <div class="actions">
          <button onclick="editCar(${index})">✏️</button>
          <button onclick="deleteCar(${index})">🗑️</button>
        </div>
      `;
            carGrid.appendChild(card);
        });
    }

    // Mở modal thêm mới
    addCarBtn.onclick = () => {
        modal.style.display = "flex";
        modalTitle.textContent = "Thêm loại xe";
        carNameInput.value = "";
        carImageInput.value = "";
        editIndex = null;
    };

    // Lưu dữ liệu
    saveCarBtn.onclick = () => {
        const name = carNameInput.value.trim();
        const image = carImageInput.value.trim();
        if (!name || !image) {
            alert("Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        if (editIndex !== null) {
            cars[editIndex] = { name, image };
        } else {
            cars.push({ name, image });
        }
        modal.style.display = "none";
        renderCars();
    };

    // Đóng modal
    closeModalBtn.onclick = () => {
        modal.style.display = "none";
    };

    // Cho phép edit, delete toàn cục
    window.editCar = (index) => {
        modal.style.display = "flex";
        modalTitle.textContent = "Sửa loại xe";
        carNameInput.value = cars[index].name;
        carImageInput.value = cars[index].image;
        editIndex = index;
    };

    window.deleteCar = (index) => {
        if (confirm("Bạn có chắc muốn xóa loại xe này?")) {
            cars.splice(index, 1);
            renderCars();
        }
    };

    renderCars();
});
