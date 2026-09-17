export async function initOrderModule() {
    populateProductSelect();
    loadOrders();

    const form = document.getElementById('orderForm');
    if (form) {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            const select = document.getElementById('o-product-select');
            const selectedOption = select.options[select.selectedIndex];
            
            const order = {
                customerName: document.getElementById('o-customer').value,
                items: [{
                    productId: parseInt(select.value),
                    productName: selectedOption.dataset.name,
                    quantity: parseInt(document.getElementById('o-quantity').value),
                    unitPrice: parseFloat(selectedOption.dataset.price)
                }]
            };

            await fetch('/api/orders', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(order)
            });
            form.reset();
            loadOrders();
        });
    }
}

async function populateProductSelect() {
    const res = await fetch('/api/products');
    const products = await res.json();
    const select = document.getElementById('o-product-select');
    if (!select) return;
    select.innerHTML = products.map(p => 
        `<option value="${p.id}" data-name="${p.name}" data-price="${p.price}">${p.name} - $${p.price}</option>`
    ).join('');
}

async function loadOrders() {
    const res = await fetch('/api/orders');
    const orders = await res.json();
    const tbody = document.getElementById('orderTableBody');
    if (!tbody) return;
    tbody.innerHTML = orders.map(o => `
        <tr>
            <td>#${o.id}</td>
            <td class="fw-bold">${o.customerName}</td>
            <td>${new Date(o.orderDate).toLocaleDateString()}</td>
            <td><span class="badge bg-warning text-dark">${o.status}</span></td>
            <td class="fw-bold text-success">$${o.totalAmount.toFixed(2)}</td>
        </tr>
    `).join('');
}