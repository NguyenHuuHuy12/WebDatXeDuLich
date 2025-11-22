document.getElementById("signUp").addEventListener("click", () => {
    document.getElementById("container").classList.add("right-panel-active");
});

document.getElementById("signIn").addEventListener("click", () => {
    document.getElementById("container").classList.remove("right-panel-active");
});
