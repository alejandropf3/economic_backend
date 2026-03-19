// ─────────────────────────────────────────────────────────────────────────────
// categorias_dinamicas.js — versión corregida
// ─────────────────────────────────────────────────────────────────────────────
 
document.addEventListener("DOMContentLoaded", () => {
 
    const contenedor = document.querySelector(".caja-categorias__contenido");
    if (!contenedor) {
        console.error("categorias_dinamicas: no se encontró .caja-categorias__contenido");
        return;
    }
 
    // ── 1. Verificar que el array existe ──────────────────────────────────────
    if (typeof categoriasData === "undefined" || !Array.isArray(categoriasData)) {
        console.error("categorias_dinamicas: categoriasData no está definido");
        return;
    }
 
    console.log("Categorías recibidas →", categoriasData);
 
    contenedor.innerHTML = "";
 
    if (categoriasData.length === 0) {
        contenedor.innerHTML = `<p class="caja-categorias__vacia">No tienes categorías creadas aún.</p>`;
        return;
    }
 
    // ── 2. Renderizar cada categoría ──────────────────────────────────────────
    categoriasData.forEach(cat => {
        const tipoClase = cat.tipoTransaccion === "Ingreso" ? "ingreso" : "egreso";
        const tipoTexto = cat.tipoTransaccion === "Ingreso" ? "Ingreso +" : "Egreso -";
 
        const item = document.createElement("div");
        item.classList.add("contenido__item", tipoClase);
 
        item.innerHTML = `
            <h2 class="item__titulo">${cat.nombreCategoria}</h2>
            <p class="item__parrafo">${tipoTexto}</p>
            <nav class="item__navegacion">
                <a href="#"
                   class="boton--eliminar"
                   data-id="${cat.idCategoria}">
                   Eliminar
                </a>
            </nav>
        `;
 
        contenedor.appendChild(item);
    });
 
    // ── 4. Delegación de eventos — Eliminar ───────────────────────────────────
    contenedor.addEventListener("click", (e) => {
        const btnEliminar = e.target.closest(".boton--eliminar");
        if (!btnEliminar) return;
 
        e.preventDefault();
        if (!confirm("¿Estás seguro de que deseas eliminar esta categoría?")) return;
 
        const contextPath = document.querySelector("meta[name='context-path']").content;
 
        const form = document.createElement("form");
        form.method = "POST";
        form.action = contextPath + "/CategoriaControlador";
 
        const campoAccion = document.createElement("input");
        campoAccion.type  = "hidden";
        campoAccion.name  = "accion";
        campoAccion.value = "eliminar";
 
        const campoId = document.createElement("input");
        campoId.type  = "hidden";
        campoId.name  = "txtIdCategoria";
        campoId.value = btnEliminar.dataset.id;
 
        form.appendChild(campoAccion);
        form.appendChild(campoId);
        document.body.appendChild(form);
        form.submit();
    });
});