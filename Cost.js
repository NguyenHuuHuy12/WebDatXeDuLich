const searchInput = document.getElementById("searchInput");
const table = document.getElementById("priceTable");
const rows = table.getElementsByTagName("tr");

searchInput.addEventListener("keyup", () => {
    const filter = searchInput.value.toLowerCase();
    for (let i = 1; i < rows.length; i++) {
        const firstCell = rows[i].getElementsByTagName("td")[0];
        if (firstCell) {
            const textValue = firstCell.textContent || firstCell.innerText;
            rows[i].style.display = textValue.toLowerCase().includes(filter) ? "" : "none";
        }
    }
});
