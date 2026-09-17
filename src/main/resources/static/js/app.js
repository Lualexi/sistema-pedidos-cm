import { initProductModule } from './modules/product/product.js';
import { initOrderModule } from './modules/order/order.js';

async function navigateTo(page, moduleInitializer) {
    const res = await fetch(`./pages/${page}.html`);
    const html = await res.text();
    document.getElementById('view-container').innerHTML = html;
    if (moduleInitializer) moduleInitializer();
}

document.addEventListener('DOMContentLoaded', () => {
    // Vista por defecto
    navigateTo('products', initProductModule);

    document.getElementById('nav-products').addEventListener('click', (e) => {
        e.preventDefault();
        updateActiveNav(e.target);
        navigateTo('products', initProductModule);
    });

    document.getElementById('nav-orders').addEventListener('click', (e) => {
        e.preventDefault();
        updateActiveNav(e.target);
        navigateTo('orders', initOrderModule);
    });
});

function updateActiveNav(target) {
    document.querySelectorAll('#sidebar .nav-link').forEach(el => el.classList.remove('active'));
    target.classList.add('active');
}