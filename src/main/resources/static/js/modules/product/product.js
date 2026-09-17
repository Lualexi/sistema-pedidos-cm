export async function initProductModule() {
    loadProducts();
    const form = document.getElementById('productForm');
    if (form) {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            const product = {
                name: document.getElementById('p-name').value,
                description: document.getElementById('p-desc').value,
                price: parseFloat(document.getElementById('p-price').value),
                stock: parseInt(document.getElementById('p-stock').value)
            };
            await fetch('/api/products', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(product)
            });
            form.reset();
            loadProducts();
        });
    }
}

async function loadProducts() {
    const res = await fetch('/api/products');
    const products = await res.json();
    const tbody = document.getElementById('productTableBody');
    if (!tbody) return;
    tbody.innerHTML = products.map(p => `
        <tr>
            <td>#${p.id}</td>
            <td class="fw-bold">${p.name}</td>
            <td>${p.description}</td>
            <td>$${p.price.toFixed(2)}</td>
            <td><span class="badge bg-primary">${p.stock} unids</span></td>
        </tr>
    `).join('');
}