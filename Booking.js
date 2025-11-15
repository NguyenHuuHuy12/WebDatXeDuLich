document.getElementById('checkPrice').onclick = () => {
    const car = document.getElementById('carType').value;
    const price = car === 'sedan' ? 300000 : car === 'suv' ? 500000 : car === 'minivan' ? 700000 : 0;
    document.getElementById('priceInfo').textContent = price ? `Giá dự kiến: ${price} VND` : 'Vui lòng chọn loại xe';
}

document.getElementById('payBtn').onclick = () => {
    const method = document.getElementById('paymentMethod').value;
    document.getElementById('paymentResult').textContent = `Thanh toán thành công bằng ${method}`;
}
