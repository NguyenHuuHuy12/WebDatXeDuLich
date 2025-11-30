const loginForm = document.getElementById("loginForm");
const registerForm = document.getElementById("registerForm");
const forgotForm = document.getElementById("forgotForm");
const resetForm = document.getElementById("resetForm");

const btnLogin = document.getElementById("btnLogin");
const btnRegister = document.getElementById("btnRegister");
const switchToRegister = document.getElementById("switchToRegister");
const switchToLogin = document.getElementById("switchToLogin");

const backToLogin1 = document.getElementById("backToLogin1");
const backToLogin2 = document.getElementById("backToLogin2");

// Nút quên mật khẩu thêm trong login form
const forgotBtn = document.createElement("p");
forgotBtn.innerHTML = `<span id="forgotPassword" style="cursor:pointer;color:#0a4d8c;font-weight:600">Quên mật khẩu?</span>`;
loginForm.appendChild(forgotBtn);

const forgotPassword = document.getElementById("forgotPassword");

// Hàm mở form
function showForm(form) {
    [loginForm, registerForm, forgotForm, resetForm].forEach(f => f.classList.remove("show"));
    form.classList.add("show");
}

// Chuyển tab login/register
btnLogin.onclick = () => {
    btnLogin.classList.add("active");
    btnRegister.classList.remove("active");
    showForm(loginForm);
};

btnRegister.onclick = () => {
    btnRegister.classList.add("active");
    btnLogin.classList.remove("active");
    showForm(registerForm);
};

// Link đổi giữa login/register
switchToRegister.onclick = () => {
    btnRegister.click();
};
switchToLogin.onclick = () => {
    btnLogin.click();
};

// Quên mật khẩu
forgotPassword.onclick = () => {
    btnLogin.classList.remove("active");
    btnRegister.classList.remove("active");
    showForm(forgotForm);
};

// Trở lại đăng nhập từ quên mật khẩu
backToLogin1.onclick = () => showForm(loginForm);
backToLogin2.onclick = () => showForm(loginForm);

// Khi gửi mã, chuyển sang form nhập mã & reset password
forgotForm.addEventListener("submit", function(e){
    e.preventDefault();
    alert("Mã khôi phục đã được gửi vào email của bạn.");
    showForm(resetForm);
});

// Cập nhật mật khẩu mới
resetForm.addEventListener("submit", function(e){
    e.preventDefault();
    alert("Mật khẩu của bạn đã được cập nhật!");
    showForm(loginForm);
});
