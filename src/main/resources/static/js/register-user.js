document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("registerUserForm");
  const feedback = document.getElementById("feedback");

  if (!form) return;

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    feedback.className = "";
    feedback.textContent = "Registrando usuario...";

    const payload = {
      fullName: document.getElementById("fullName").value.trim() || null,
      email: document.getElementById("email").value.trim() || null,
      username: document.getElementById("username").value.trim(),
      password: document.getElementById("password").value,
      role: document.getElementById("role").value,
      active: true
    };

    try {
      const token = localStorage.getItem("token");
      const headers = { "Content-Type": "application/json" };
      if (token) {
        headers["Authorization"] = `Bearer ${token}`;
      }

      const response = await fetch("/api/auth/register", {
        method: "POST",
        headers,
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || `Error HTTP ${response.status}`);
      }

      const created = await response.json();
      feedback.className = "msg-ok";
      feedback.textContent = `Usuario "${created.username}" registrado con rol ${created.role}.`;
      form.reset();
    } catch (err) {
      feedback.className = "msg-err";
      feedback.textContent = `No se pudo registrar: ${err.message}`;
    }
  });
});