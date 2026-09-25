import { fetchConAuth } from '../../app.js';

export async function initOrderModule() {
    populateProductSelect();
    loadOrders();

    const form = document.getElementById('orderForm');
    if (form) {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            const select = document.getElementById('o-product-select');
            if (!select.value) {
                alert("Seleccione un producto válido");
                return;
            }

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

            try {
                await fetchConAuth('/api/orders', {
                    method: 'POST',
                    body: JSON.stringify(order)
                });
                form.reset();
                loadOrders();
            } catch (err) {
                alert("No se pudo registrar el pedido: " + err.message);
            }
        });
    }
}

async function populateProductSelect() {
    try {
        const products = await fetchConAuth('/api/products');
        const select = document.getElementById('o-product-select');
        if (!select) return;

        if (products.length === 0) {
            select.innerHTML = `<option value="">Sin productos disponibles</option>`;
            return;
        }

        select.innerHTML = products.map(p => 
            `<option value="${p.id}" data-name="${p.name}" data-price="${p.price}">
                ${p.name} - $${p.price} (Stock: ${p.stock})
            </option>`
        ).join('');
    } catch (err) {
        console.error("Error al cargar el selector de productos:", err);
    }
}

async function loadOrders() {
    try {
        const orders = await fetchConAuth('/api/orders');
        const tbody = document.getElementById('orderTableBody');
        if (!tbody) return;

        if (orders.length === 0) {
            tbody.innerHTML = `<tr><td colspan="5" class="text-center text-muted">No hay pedidos registrados</td></tr>`;
            return;
        }

        tbody.innerHTML = orders.map(o => {
            const cliente = o.customerName || 'Cliente Genérico';
            const fecha = o.date || o.orderDate;
            const total = o.total || o.totalAmount || 0;
            const fechaFormateada = fecha ? new Date(fecha).toLocaleDateString() : 'N/A';

            return `
                <tr>
                    <td>#${o.id}</td>
                    <td class="fw-bold">${cliente}</td>
                    <td>${fechaFormateada}</td>
                    <td><span class="badge bg-warning text-dark">${o.status || 'PENDIENTE'}</span></td>
                    <td class="fw-bold text-success">$${Number(total).toFixed(2)}</td>
                </tr>
            `;
        }).join('');
    } catch (err) {
        console.error("Error al cargar la lista de pedidos:", err);
    }
}