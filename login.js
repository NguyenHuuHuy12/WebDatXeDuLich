const btnLogin = document.getElementById("btnLogin");
const btnRegister = document.getElementById("btnRegister");

const loginForm = document.getElementById("loginForm");
const registerForm = document.getElementById("registerForm");

const switchToRegister = document.getElementById("switchToRegister");
const switchToLogin = document.getElementById("switchToLogin");

// Hàm đổi tab
function showLogin() {
    btnLogin.classList.add("active");
    btnRegister.classList.remove("active");
    loginForm.classList.add("show");
    registerForm.classList.remove("show");

}

function showRegister() {
    btnRegister.classList.add("active");
    btnLogin.classList.remove("active");
    registerForm.classList.add("show");
    loginForm.classList.remove("show");
}

btnLogin.onclick = showLogin;
btnRegister.onclick = showRegister;

switchToRegister.onclick = showRegister;
switchToLogin.onclick = showLogin;
