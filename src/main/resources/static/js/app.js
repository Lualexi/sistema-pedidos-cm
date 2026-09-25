import { initProductModule } from './modules/product/product.js';
import { initOrderModule } from './modules/order/order.js';

if (!localStorage.getItem('token')) {
    window.location.href = '/pages/login.html';
}

async function navigateTo(page, moduleInitializer) {
    try {
        const res = await fetch(`./pages/${page}.html`);
        if (!res.ok) throw new Error("No se pudo cargar la vista");
        const html = await res.text();
        document.getElementById('view-container').innerHTML = html;
        if (moduleInitializer) moduleInitializer();
    } catch (err) {
        console.error("Error al navegar:", err);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    // Mostrar Rol en la UI
    const roleBadge = document.getElementById('user-role-badge');
    const currentRole = localStorage.getItem('role') || 'USUARIO';
    if (roleBadge) roleBadge.textContent = currentRole;

    // Logout
    const logoutBtn = document.getElementById('btn-logout');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', () => {
            localStorage.clear();
            window.location.href = '/pages/login.html';
        });
    }

    // Navegación principal
    navigateTo('products', initProductModule);

    document.getElementById('nav-products').addEventListener('click', (e) => {
        e.preventDefault();
        updateActiveNav(e.currentTarget);
        navigateTo('products', initProductModule);
    });

    document.getElementById('nav-orders').addEventListener('click', (e) => {
        e.preventDefault();
        updateActiveNav(e.currentTarget);
        navigateTo('orders', initOrderModule);
    });
});

function updateActiveNav(target) {
    document.querySelectorAll('#sidebar .nav-link').forEach(el => el.classList.remove('active'));
    target.classList.add('active');
}

export async function fetchConAuth(url, options = {}) {
    const token = localStorage.getItem("token");
    
    const headers = {
        'Content-Type': 'application/json',
        ...options.headers
    };

    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch(url, { ...options, headers });

    if (response.status === 401) {
        localStorage.clear();
        window.location.href = '/pages/login.html';
        throw new Error('Sesión expirada');
    }

    if (!response.ok) {
        const errorData = await response.json().catch(() => ({ message: 'Error en la solicitud' }));
        throw new Error(errorData.message || `Error HTTP: ${response.status}`);
    }

    if (response.status === 204) return null;

    return response.json();
}