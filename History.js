// Dữ liệu mẫu. Thay bằng API thực tế khi tích hợp.


function statusText(s){if(s==='pending')return 'Đang chờ'; if(s==='done')return 'Hoàn thành'; return 'Đã huỷ'}
function formatCurrency(n){return n.toLocaleString('vi-VN')+' ₫'}
function escapeHtml(s){return String(s).replaceAll('&','&amp;').replaceAll('<','&lt;').replaceAll('>','&gt;')}


// filtering/search
const qEl = document.getElementById('q');
const suggestEl = document.getElementById('suggest');
qEl.addEventListener('input',onQuery);
qEl.addEventListener('focus',onQuery);
document.addEventListener('click',e=>{if(!e.target.closest('.search')) suggestEl.style.display='none'})


function onQuery(){
    const q = qEl.value.trim().toLowerCase();
// show suggestions
    const matched = suggestions.filter(s=>s.toLowerCase().includes(q)).slice(0,10);
    suggestEl.innerHTML = matched.map(s=>`<div data-val="${s}">${s}</div>`).join('');
    suggestEl.style.display = matched.length? 'block':'none';
    suggestEl.querySelectorAll('div').forEach(d=>d.addEventListener('click',()=>{qEl.value=d.dataset.val; suggestEl.style.display='none'; applyFilters()}));
    applyFilters();
}


// date + status
document.getElementById('statusFilter').addEventListener('change',applyFilters);
document.getElementById('from').addEventListener('change',applyFilters);
document.getElementById('to').addEventListener('change',applyFilters);
document.getElementById('clearFilters').addEventListener('click',()=>{document.getElementById('statusFilter').value='';document.getElementById('from').value='';document.getElementById('to').value='';qEl.value='';applyFilters()});


function applyFilters(){
    const q = qEl.value.trim().toLowerCase();
    const status = document.getElementById('statusFilter').value;
    const from = document.getElementById('from').value;
    const to = document.getElementById('to').value;
    let out = bookings.filter(b=>{
        if(status && b.status!==status) return false;
        if(from && new Date(b.date) < new Date(from+'T00:00:00')) return false;
        if(to && new Date(b.date) > new Date(to+'T23:59:59')) return false;
        if(!q) return true;
        const hay = `${b.id} ${b.from} ${b.to} ${b.driver}`.toLowerCase();
        return hay.includes(q);
    });
    render(out);
}


// modal logic
const modal = document.getElementById('modal');
function openModal(b){document.getElementById('md_route').textContent = b.from+' → '+b.to;document.getElementById('md_code').textContent=b.id;document.getElementById('md_date').textContent=fmtDate(b.date);document.getElementById('md_driver').textContent=b.driver;document.getElementById('md_price').textContent=formatCurrency(b.price);modal.style.display='flex'}
document.getElementById('closeModal').addEventListener('click',()=>modal.style.display='none');


// export CSV
document.getElementById('exportCsv').addEventListener('click',()=>{
    const rows = [['Mã đặt','Điểm đi','Điểm đến','Ngày','Tài xế','Phí','Trạng thái']];
    document.querySelectorAll('.list .item').forEach(it=>{
// parse visible items from DOM
    });
// export all filtered items instead
    const data = getFiltered();
    const csv = rows.concat(data.map(d=>[d.id,d.from,d.to,fmtDate(d.date),d.driver,d.price,statusText(d.status)])).map(r=>r.map(cell=>`"${String(cell).replace(/"/g,'""')}"`).join(',')).join('\n');
    const blob = new Blob([csv],{type:'text/csv;charset=utf-8;'});
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a'); a.href=url; a.download='booking-history.csv'; a.click(); URL.revokeObjectURL(url);
});


function getFiltered(){
    const q = qEl.value.trim().toLowerCase();
    const status = document.getElementById('statusFilter').value;
    const from = document.getElementById('from').value;
    const to = document.getElementById('to').value;
    return bookings.filter(b=>{
        if(status && b.status!==status) return false;
        if(from && new Date(b.date) < new Date(from+'T00:00:00')) return false;
        if(to && new Date(b.date) > new Date(to+'T23:59:59')) return false;
        if(!q) return true;
        const hay = `${b.id} ${b.from} ${b.to} ${b.driver}`.toLowerCase();
        return hay.includes(q);
    });
}


// initial
render(bookings);
function init(){/* placeholder if fetching from api */}
init();