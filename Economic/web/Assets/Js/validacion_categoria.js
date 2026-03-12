// ─────────────────────────────────────────────────────────────────────────────
// validacion_categoria.js — versión corregida
// ─────────────────────────────────────────────────────────────────────────────
 
const MIN_CARACTERES = 4;
const MAX_CARACTERES = 30;
 
// ── Utilidades ────────────────────────────────────────────────────────────────
 
const mostrarError = (spanId, inputEl, mensaje) => {
    const span = document.getElementById(spanId);
    if (span) {
        span.textContent = mensaje;
        span.style.display = "block";
    }
    if (inputEl) inputEl.classList.add("input--error");
};
 
const limpiarErrores = (formulario) => {
    formulario.querySelectorAll(".input--error").forEach(el => el.classList.remove("input--error"));
    formulario.querySelectorAll(".formulario__span-error").forEach(span => {
        span.textContent = "";
        span.style.display = "none";
    });
};
 
const validarCampos = (nombre, tipo, inputNombre, inputTipo, spanNombreId, spanTipoId) => {
    let esValido = true;
 
    if (!nombre || nombre.trim() === "") {
        mostrarError(spanNombreId, inputNombre, "El nombre de la categoría es obligatorio.");
        esValido = false;
    } else if (nombre.trim().length < MIN_CARACTERES) {
        mostrarError(spanNombreId, inputNombre, `El nombre debe tener al menos ${MIN_CARACTERES} caracteres.`);
        esValido = false;
    } else if (nombre.trim().length > MAX_CARACTERES) {
        mostrarError(spanNombreId, inputNombre, `El nombre no puede superar ${MAX_CARACTERES} caracteres.`);
        esValido = false;
    }
 
    if (!tipo || tipo.trim() === "") {
        mostrarError(spanTipoId, inputTipo, "Debes seleccionar un tipo.");
        esValido = false;
    }
 
    return esValido;
};
 
// ── Inicialización ────────────────────────────────────────────────────────────
 
document.addEventListener("DOMContentLoaded", () => {
 
    // ── Mensajes de error desde el servidor (?res=...) ────────────────────────
    const urlParams = new URLSearchParams(window.location.search);
    const res  = urlParams.get("res");
    const form = urlParams.get("form");
 
    if (res && form) {
        let mensaje = "";
        switch (res) {
            case "vacio":            mensaje = "Todos los campos son obligatorios."; break;
            case "nombre_muy_corto": mensaje = `El nombre debe tener al menos ${MIN_CARACTERES} caracteres.`; break;
            case "nombre_muy_largo": mensaje = `El nombre no puede superar ${MAX_CARACTERES} caracteres.`; break;
            case "tipo_invalido":    mensaje = "El tipo seleccionado no es válido."; break;
            case "nombre_duplicado": mensaje = "Ya existe una categoría con ese nombre."; break;
            case "error":            mensaje = "Ocurrió un error. Intenta de nuevo."; break;
            case "error_eliminar":   mensaje = "No se pudo eliminar la categoría."; break;
        }
 
        if (form === "crear") {
            const span = document.getElementById("error-crear-nombre");
            const input = document.querySelector(".ventana-crear-categoria__formulario-crear [name='txtNombreCategoria']");
            if (span)  { span.textContent = mensaje; span.style.display = "block"; }
            if (input) input.classList.add("input--error");
            // Abrir modal automáticamente
            window.location.hash = "ventana-crear-categoria-confirmar";
        }
 
        if (form === "editar") {
            const span = document.getElementById("error-editar-nombre");
            const input = document.querySelector(".ventana-modificar-categoria__formulario-editar [name='txtNombreCategoria']");
            if (span)  { span.textContent = mensaje; span.style.display = "block"; }
            if (input) input.classList.add("input--error");
            window.location.hash = "ventana-modificar-categoria-confirmar";
        }
    }
 
    // ── Validación formulario CREAR ───────────────────────────────────────────
    const formCrear = document.querySelector(".ventana-crear-categoria__formulario-crear");
    if (formCrear) {
        formCrear.addEventListener("submit", (e) => {
            limpiarErrores(formCrear);
 
            const inputNombre = formCrear.querySelector("[name='txtNombreCategoria']");
            const inputTipo   = formCrear.querySelector("[name='txtTipoCategoria']");
 
            const esValido = validarCampos(
                inputNombre ? inputNombre.value : "",
                inputTipo   ? inputTipo.value   : "",
                inputNombre, inputTipo,
                "error-crear-nombre", "error-crear-tipo"
            );
 
            if (!esValido) {
                e.preventDefault();
                e.stopPropagation();
            }
        });
    }
 
    // ── Validación formulario EDITAR ──────────────────────────────────────────
    const formEditar = document.querySelector(".ventana-modificar-categoria__formulario-editar");
    if (formEditar) {
        formEditar.addEventListener("submit", (e) => {
            limpiarErrores(formEditar);
 
            const inputNombre = formEditar.querySelector("[name='txtNombreCategoria']");
            const inputTipo   = formEditar.querySelector("[name='txtTipoCategoria']");
 
            const esValido = validarCampos(
                inputNombre ? inputNombre.value : "",
                inputTipo   ? inputTipo.value   : "",
                inputNombre, inputTipo,
                "error-editar-nombre", "error-editar-tipo"
            );
 
            if (!esValido) {
                e.preventDefault();
                e.stopPropagation();
            }
        });
    }
});