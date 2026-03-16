// ─────────────────────────────────────────────────────────────────────────────
// historial_transacciones.js
// Maneja modal de edición, eliminación y mensajes del servidor
// ─────────────────────────────────────────────────────────────────────────────
 
// ── Abrir modal editar ────────────────────────────────────────────────────────
const abrirModalEditar = (id, idCategoria, valor, descripcion, fecha) => {
    document.getElementById("edit-id").value          = id;
    document.getElementById("edit-valor").value       = valor;
    document.getElementById("edit-descripcion").value = descripcion !== "—" ? descripcion : "";
    document.getElementById("edit-fecha").value       = fecha;
 
    // Seleccionar la categoría correcta
    const selectCategoria = document.getElementById("edit-categoria");
    for (let i = 0; i < selectCategoria.options.length; i++) {
        if (selectCategoria.options[i].value == idCategoria) {
            selectCategoria.selectedIndex = i;
            break;
        }
    }
 
    // Limpiar errores previos
    document.querySelectorAll("#modal-editar .formulario__span-error").forEach(span => {
        span.textContent = "";
        span.style.display = "none";
    });
 
    // Mostrar modal usando el mecanismo CSS :target
    window.location.hash = "modal-editar";
};
 
// ── Cerrar modal editar ───────────────────────────────────────────────────────
const cerrarModalEditar = () => {
    history.pushState("", document.title, window.location.pathname + window.location.search);
};
 
// ── Confirmar eliminación ─────────────────────────────────────────────────────
const confirmarEliminar = (id) => {
    if (!confirm("¿Estás seguro de que deseas eliminar esta transacción? Esta acción no se puede deshacer.")) return;
    document.getElementById("eliminar-id").value = id;
    document.getElementById("form-eliminar").submit();
};
 
// ── Validación del formulario de edición ──────────────────────────────────────
document.addEventListener("DOMContentLoaded", () => {
 
    // Mensajes del servidor
    const urlParams = new URLSearchParams(window.location.search);
    const res = urlParams.get("res");
 
    if (res) {
        const mensajes = {
            ok_editar:    { texto: "Transacción editada correctamente.",  tipo: "ok"    },
            ok_eliminar:  { texto: "Transacción eliminada correctamente.", tipo: "ok"   },
            valor_vacio:  { texto: "El valor es obligatorio.",             tipo: "error" },
            valor_invalido: { texto: "El valor debe ser mayor a 0.",       tipo: "error" },
            categoria_vacia: { texto: "Selecciona una categoría.",         tipo: "error" },
            fecha_vacia:  { texto: "La fecha es obligatoria.",             tipo: "error" },
            fecha_invalida: { texto: "El formato de fecha no es válido.",  tipo: "error" },
            error:        { texto: "Ocurrió un error. Intenta de nuevo.",  tipo: "error" }
        };
 
        const info = mensajes[res];
        if (info) {
            // Crear toast de notificación
            const toast = document.createElement("div");
            toast.textContent = info.texto;
            toast.style.cssText = `
                position: fixed; top: 90px; right: 20px; z-index: 99999;
                padding: 14px 20px; border-radius: 10px; font-size: 14px;
                font-weight: 600; box-shadow: 0 8px 24px rgba(0,0,0,0.15);
                transition: opacity 0.4s ease; opacity: 1;
                background: ${info.tipo === "ok" ? "#28a745" : "#dc3545"};
                color: white;
            `;
            document.body.appendChild(toast);
            setTimeout(() => { toast.style.opacity = "0"; }, 3000);
            setTimeout(() => { toast.remove(); }, 3400);
        }
    }
 
    // Validación antes de enviar el formulario de edición
    const formEditar = document.querySelector(".ventana-modificar-categoria__formulario-editar");
    if (formEditar) {
        formEditar.addEventListener("submit", (e) => {
 
            // Limpiar errores
            formEditar.querySelectorAll(".formulario__span-error").forEach(s => {
                s.textContent = ""; s.style.display = "none";
            });
            formEditar.querySelectorAll(".contenido__input").forEach(i => {
                i.style.borderColor = "";
            });
 
            let esValido = true;
 
            const valor = document.getElementById("edit-valor").value.trim();
            const fecha = document.getElementById("edit-fecha").value;
            const hoy   = new Date().toISOString().split("T")[0];
 
            if (!valor || parseFloat(valor) <= 0) {
                mostrarErrorEdit("error-edit-valor", "edit-valor", "El valor debe ser mayor a 0.");
                esValido = false;
            }
            if (!fecha) {
                mostrarErrorEdit("error-edit-fecha", "edit-fecha", "La fecha es obligatoria.");
                esValido = false;
            } else if (fecha > hoy) {
                mostrarErrorEdit("error-edit-fecha", "edit-fecha", "La fecha no puede ser mayor a hoy.");
                esValido = false;
            }
 
            if (!esValido) e.preventDefault();
        });
    }
});
 
const mostrarErrorEdit = (spanId, inputId, mensaje) => {
    const span  = document.getElementById(spanId);
    const input = document.getElementById(inputId);
    if (span)  { span.textContent = mensaje; span.style.display = "block"; }
    if (input) input.style.borderColor = "#c0392b";
};