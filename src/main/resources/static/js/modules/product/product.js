import { fetchConAuth } from '../../app.js';

export async function initProductModule() {
    const userRole = localStorage.getItem('role');
    const form = document.getElementById('productForm');
    
    // Restricción visual si es VENDEDOR
    if (userRole !== 'ADMIN_ALMACEN' && form) {
        const cardBody = form.closest('.card-body');
        if (cardBody) {
            cardBody.innerHTML = `
                <div class="alert alert-warning mb-0" role="alert">
                    <i class="bi bi-shield-lock me-2"></i>Solo los usuarios con rol <strong>ADMIN_ALMACEN</strong> pueden registrar o modificar productos.
                </div>
            `;
        }
    } else if (form) {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            const product = {
                name: document.getElementById('p-name').value,
                price: parseFloat(document.getElementById('p-price').value),
                stock: parseInt(document.getElementById('p-stock').value),
                description: document.getElementById('p-desc')?.value || ''
            };

            try {
                await fetchConAuth('/api/products', {
                    method: 'POST',
                    body: JSON.stringify(product)
                });
                form.reset();
                loadProducts();
            } catch (err) {
                alert("Error al guardar producto: " + err.message);
            }
        });
    }

    loadProducts();
}

async function loadProducts() {
    try {
        const products = await fetchConAuth('/api/products');
        const tbody = document.getElementById('productTableBody');
        if (!tbody) return;

        if (products.length === 0) {
            tbody.innerHTML = `<tr><td colspan="5" class="text-center text-muted">No hay productos registrados</td></tr>`;
            return;
        }

        tbody.innerHTML = products.map(p => `
            <tr>
                <td>#${p.id}</td>
                <td class="fw-bold">${p.name}</td>
                <td>${p.description || 'N/A'}</td>
                <td>$${Number(p.price).toFixed(2)}</td>
                <td><span class="badge ${p.stock > 0 ? 'bg-primary' : 'bg-danger'}">${p.stock} unids</span></td>
            </tr>
        `).join('');
    } catch (err) {
        console.error("Error al cargar productos:", err);
    }
}