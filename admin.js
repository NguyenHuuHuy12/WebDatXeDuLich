// chuyển trang
document.querySelectorAll(".sidebar li").forEach(li => {
    li.onclick = () => {
        document.querySelector(".sidebar li.active").classList.remove("active");
        li.classList.add("active");

        document.querySelector(".page.active").classList.remove("active");
        document.getElementById(li.dataset.page).classList.add("active");
    };
});

// Dữ liệu mẫu
const db = {
    users: [
        {id:1, name:"Nguyễn Văn A", email:"a@gmail.com"},
        {id:2, name:"Trần Thị B", email:"b@gmail.com"}
    ],
    drivers: [
        {id:1, name:"Tài xế Minh", phone:"090123456"},
        {id:2, name:"Tài xế Phong", phone:"090987654"}
    ],
    cars: [
        {id:1, name:"Xe 7 chỗ"},
        {id:2, name:"Xe 16 chỗ"},
        {id:3, name:"Limousine"}
    ],
    trips: [
        {id:101, route:"Sài Gòn → Đà Lạt", price:250000, status:"Hoàn thành"},
        {id:102, route:"Hà Nội → Ninh Bình", price:150000, status:"Đang chờ"}
    ],
    promo: [
        {id:1, code:"SALE50", percent:50}
    ],
    feedback: [
        {id:1, name:"Khách X", msg:"Dịch vụ tốt!"}
    ]
};

// Helper render list
function render(listId, arr, fields) {
    const box = document.getElementById(listId);
    box.innerHTML = "";
    arr.forEach(o => {
        const div = document.createElement("div");
        div.className = "item";
        div.innerHTML = `
            <div>${fields.map(f => o[f]).join(" - ")}</div>
            <button class="btn">Xóa</button>
        `;
        box.appendChild(div);
    });
}

// Render danh sách
render("userList", db.users, ["name", "email"]);
render("driverList", db.drivers, ["name", "phone"]);
render("carList", db.cars, ["name"]);
render("tripList", db.trips, ["route", "price", "status"]);
render("promoList", db.promo, ["code", "percent"]);
render("feedbackList", db.feedback, ["name", "msg"]);

// Dashboard
document.getElementById("totalTrips").innerText = db.trips.length;
document.getElementById("activeCars").innerText = db.cars.length;
document.getElementById("totalRevenue").innerText = "1.250.000 VNĐ";
document.getElementById("topRoute").innerText = "Sài Gòn → Đà Lạt";
