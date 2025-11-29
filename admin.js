document.addEventListener("DOMContentLoaded", () => {
    const modal = document.getElementById("modal");
    const modalContent = document.getElementById("modalContent");

    function openModal(html) {
        modalContent.innerHTML = html;
        modal.style.display = "block";
    }
    window.closeModal = () => modal.style.display = "none";

    // Load dữ liệu từ localStorage
    let drivers = JSON.parse(localStorage.getItem("drivers") || "[]");
    let cars = JSON.parse(localStorage.getItem("cars") || "[]");
    let promos = JSON.parse(localStorage.getItem("promos") || "[]");
    let feedback = JSON.parse(localStorage.getItem("feedback") || "[]");

    // Sidebar chuyển trang
    document.querySelectorAll(".sidebar li").forEach(li => {
        li.addEventListener("click", () => {
            document.querySelectorAll(".sidebar li").forEach(i => i.classList.remove("active"));
            li.classList.add("active");

            let page = li.getAttribute("data-page");
            document.querySelectorAll(".page").forEach(p => p.classList.remove("active"));
            document.getElementById(page).classList.add("active");
        });
    });

    // -------------------
    // HIỂN THỊ DỮ LIỆU
    // -------------------

    function renderDrivers() {
        let list = document.getElementById("driverList");
        list.innerHTML = "";
        drivers.forEach((d, i) => {
            list.innerHTML += `
                <div class="card">
                    <b>${d.name}</b><br>
                    SĐT: ${d.phone}<br>
                    <button onclick="editDriver(${i})">Sửa</button>
                    <button onclick="deleteDriver(${i})">Xóa</button>
                </div>`;
        });
    }

    function renderCars() {
        let list = document.getElementById("carList");
        list.innerHTML = "";
        cars.forEach((c, i) => {
            list.innerHTML += `
                <div class="card">
                    <b>${c.name}</b><br>
                    Số chỗ: ${c.seat}<br>
                    <button onclick="editCar(${i})">Sửa</button>
                    <button onclick="deleteCar(${i})">Xóa</button>
                </div>`;
        });
    }

    function renderPromos() {
        let list = document.getElementById("promoList");
        list.innerHTML = "";
        promos.forEach((p, i) => {
            list.innerHTML += `
                <div class="card">
                    <b>${p.code}</b><br>
                    Giảm: ${p.discount}%<br>
                    <button onclick="editPromo(${i})">Sửa</button>
                    <button onclick="deletePromo(${i})">Xóa</button>
                </div>`;
        });
    }

    function renderFeedback() {
        let list = document.getElementById("feedbackList");
        list.innerHTML = "";
        feedback.forEach(f => {
            list.innerHTML += `
                <div class="card">
                    <b>${f.name}</b><br>
                    ${f.message}
                </div>`;
        });
    }

    // -------------------
    // CRUD TÀI XẾ
    // -------------------

    document.getElementById("addDriverBtn").onclick = () => {
        openModal(`
            <h3>Thêm tài xế</h3>
            <input id="driverName" placeholder="Tên tài xế">
            <input id="driverPhone" placeholder="Số điện thoại">
            <button onclick="saveDriver()">Lưu</button>
            <button onclick="closeModal()">Hủy</button>
        `);
    };

    window.saveDriver = () => {
        drivers.push({
            name: driverName.value,
            phone: driverPhone.value
        });
        localStorage.setItem("drivers", JSON.stringify(drivers));
        renderDrivers();
        closeModal();
    };

    window.editDriver = index => {
        let d = drivers[index];
        openModal(`
            <h3>Sửa tài xế</h3>
            <input id="driverName" value="${d.name}">
            <input id="driverPhone" value="${d.phone}">
            <button onclick="updateDriver(${index})">Cập nhật</button>
        `);
    };

    window.updateDriver = index => {
        drivers[index] = {
            name: driverName.value,
            phone: driverPhone.value
        };
        localStorage.setItem("drivers", JSON.stringify(drivers));
        renderDrivers();
        closeModal();
    };

    window.deleteDriver = index => {
        if (confirm("Xóa tài xế này?")) {
            drivers.splice(index, 1);
            localStorage.setItem("drivers", JSON.stringify(drivers));
            renderDrivers();
        }
    };

    // -------------------
    // CRUD LOẠI XE
    // -------------------

    document.getElementById("addCarBtn").onclick = () => {
        openModal(`
            <h3>Thêm loại xe</h3>
            <input id="carName" placeholder="Tên xe">
            <input id="carSeat" placeholder="Số chỗ">
            <button onclick="saveCar()">Lưu</button>
        `);
    };

    window.saveCar = () => {
        cars.push({
            name: carName.value,
            seat: carSeat.value
        });
        localStorage.setItem("cars", JSON.stringify(cars));
        renderCars();
        closeModal();
    };

    window.editCar = index => {
        let c = cars[index];
        openModal(`
            <h3>Sửa loại xe</h3>
            <input id="carName" value="${c.name}">
            <input id="carSeat" value="${c.seat}">
            <button onclick="updateCar(${index})">Cập nhật</button>
        `);
    };

    window.updateCar = index => {
        cars[index] = {
            name: carName.value,
            seat: carSeat.value
        };
        localStorage.setItem("cars", JSON.stringify(cars));
        renderCars();
        closeModal();
    };

    window.deleteCar = index => {
        if (confirm("Xóa xe?")) {
            cars.splice(index, 1);
            localStorage.setItem("cars", JSON.stringify(cars));
            renderCars();
        }
    };

    // -------------------
    // CRUD PROMO
    // -------------------

    document.getElementById("addPromoBtn").onclick = () => {
        openModal(`
            <h3>Thêm voucher</h3>
            <input id="promoCode" placeholder="Mã giảm giá">
            <input id="promoDiscount" placeholder="Phần trăm giảm">
            <button onclick="savePromo()">Lưu</button>
        `);
    };

    window.savePromo = () => {
        promos.push({
            code: promoCode.value,
            discount: promoDiscount.value
        });
        localStorage.setItem("promos", JSON.stringify(promos));
        renderPromos();
        closeModal();
    };

    window.editPromo = index => {
        let p = promos[index];
        openModal(`
            <h3>Sửa voucher</h3>
            <input id="promoCode" value="${p.code}">
            <input id="promoDiscount" value="${p.discount}">
            <button onclick="updatePromo(${index})">Cập nhật</button>
        `);
    };

    window.updatePromo = index => {
        promos[index] = {
            code: promoCode.value,
            discount: promoDiscount.value
        };
        localStorage.setItem("promos", JSON.stringify(promos));
        renderPromos();
        closeModal();
    };

    window.deletePromo = index => {
        if (confirm("Xóa voucher?")) {
            promos.splice(index, 1);
            localStorage.setItem("promos", JSON.stringify(promos));
            renderPromos();
        }
    };

    // -------------------
    // PHẢN HỒI KHÁCH HÀNG
    // -------------------
    renderFeedback();

    // -------------------
    // DASHBOARD
    // -------------------
    function updateDashboard() {
        document.getElementById("totalUsers").textContent = "10+";
        document.getElementById("totalDrivers").textContent = drivers.length;
        document.getElementById("totalCars").textContent = cars.length;
        document.getElementById("totalPromo").textContent = promos.length;
    }

    // Render toàn bộ
    renderDrivers();
    renderCars();
    renderPromos();
    updateDashboard();
});
