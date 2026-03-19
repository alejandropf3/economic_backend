// ─────────────────────────────────────────────────────────────────────────────
// validacion_transaccion.js
// ─────────────────────────────────────────────────────────────────────────────
 
// ── Función de validación original (sin cambios) ──────────────────────────────
const validar = (formulario, reglas) => {
  for (const nombreCampo in reglas) {
    const elemento = formulario.elements[nombreCampo];
    const regla = reglas[nombreCampo];
 
    if (!elemento) continue;
 
    if (regla.required && elemento.value.trim() === "") {
      return { esValido: false, mensaje: regla.mensaje || `El campo ${nombreCampo} es obligatorio`, campo: nombreCampo };
    }
 
    if (regla.numeric && elemento.value.trim() !== "") {
      const numericRegex = /^[0-9]+(\.[0-9]+)?$/;
      if (!numericRegex.test(elemento.value.trim())) {
        return { esValido: false, mensaje: regla.numericMensaje || "El valor debe ser un número válido", campo: nombreCampo };
      }
    }
 
    if (regla.positive && elemento.value.trim() !== "") {
      const valor = parseFloat(elemento.value);
      if (isNaN(valor) || valor <= 0) {
        return { esValido: false, mensaje: regla.positiveMensaje || "El valor debe ser mayor a cero", campo: nombreCampo };
      }
    }
 
    if (regla.min && elemento.value.length < regla.min) {
      return { esValido: false, mensaje: regla.minMensaje || `El campo debe tener como mínimo ${regla.min} caracteres`, campo: nombreCampo };
    }
 
    if (regla.max && elemento.value.length > regla.max) {
      return { esValido: false, mensaje: regla.maxMensaje || `El campo no debe exceder ${regla.max} caracteres`, campo: nombreCampo };
    }
 
    if (regla.pattern && elemento.value.trim() !== "") {
      const pattern = new RegExp(regla.pattern);
      if (!pattern.test(elemento.value)) {
        return { esValido: false, mensaje: regla.patternMensaje || "El formato del campo no es válido", campo: nombreCampo };
      }
    }
  }
  return { esValido: true };
};
 
// ── Referencias al DOM ────────────────────────────────────────────────────────
const formulario   = document.querySelector('.layout-general__contenedor-registro');
const mensaje_error = document.querySelector('.mensaje_error') || document.createElement('div');
 
if (!document.querySelector('.mensaje_error')) {
    mensaje_error.className = 'mensaje_error';
    mensaje_error.style.display = 'none';
    const contenedorForm = document.querySelector('.contenedor-registro__formulario');
    if (contenedorForm) {
        contenedorForm.parentElement.insertBefore(mensaje_error, contenedorForm);
    }
}
 
// ── Reglas actualizadas — tipo y fecha ya no son requeridos manualmente ───────
const reglas = {
    valor: {
        required: true,
        numeric: true,
        positive: true,
        mensaje: "Por favor ingresa el valor de la transaccion",
        numericMensaje: "El valor debe ser un número válido (ej: 100.50)",
        positiveMensaje: "El valor debe ser mayor a cero"
    },
    categoria: {
        required: true,
        mensaje: "Por favor selecciona una categoría"
    },
    descripcion: {
        required: false,
        max: 200,
        maxMensaje: "La descripción no debe exceder 200 caracteres"
    }
};
 
// ── Mostrar / limpiar errores (original sin cambios) ──────────────────────────
const mostrar_errores = (error, campo = null) => {
    document.querySelectorAll('.error').forEach(el => el.classList.remove('error'));
    document.querySelectorAll('.campo-error').forEach(el => el.style.display = 'none');
 
    if (campo) {
        const elemento = document.querySelector(`[name="${campo}"]`);
        if (elemento) elemento.classList.add("error");
 
        const contenedorCampo = elemento ? elemento.closest('.campo-formulario') : null;
        if (contenedorCampo) {
            const spanError = contenedorCampo.querySelector('.campo-error');
            if (spanError) { spanError.textContent = error; spanError.style.display = 'block'; }
        }
    }
};
 
const limpiar_errores = () => {
    document.querySelectorAll('.error').forEach(el => el.classList.remove('error'));
    document.querySelectorAll('.campo-error').forEach(el => el.style.display = 'none');
    if (mensaje_error) mensaje_error.style.display = "none";
};
 
// ─────────────────────────────────────────────────────────────────────────────
// NUEVO: Fecha automática, badge de tipo, ventana de confirmación
// ─────────────────────────────────────────────────────────────────────────────
 
document.addEventListener('DOMContentLoaded', function () {
 
    // ── 1. Fecha automática al día de hoy ─────────────────────────────────────
    const inputFecha = document.getElementById("input-fecha");
    if (inputFecha && !inputFecha.value) {
        inputFecha.value = new Date().toISOString().split("T")[0];
    }
 
    // ── 2. Badge de tipo al seleccionar categoría ─────────────────────────────
    const selectCategoria = document.getElementById("select-categoria");
    const tipoBadge       = document.getElementById("tipo-badge");
 
    if (selectCategoria && tipoBadge) {
        selectCategoria.addEventListener("change", () => {
            const opcion = selectCategoria.options[selectCategoria.selectedIndex];
            const tipo   = opcion.dataset.tipo;
 
            if (tipo === "Ingreso") {
                tipoBadge.textContent = "+ Ingreso";
                tipoBadge.className   = "tipo-badge ingreso visible";
            } else if (tipo === "Egreso") {
                tipoBadge.textContent = "− Egreso";
                tipoBadge.className   = "tipo-badge egreso visible";
            } else {
                tipoBadge.className = "tipo-badge";
            }
        });
    }
 
    // ── 3. Limpiar errores al escribir (original) ─────────────────────────────
    if (formulario) {
        formulario.querySelectorAll('input, select, textarea').forEach(element => {
            element.addEventListener('input',  limpiar_errores);
            element.addEventListener('change', limpiar_errores);
        });
    }
 
    // ── 4. Mensajes de error del servidor (?res=...) ──────────────────────────
    const urlParams = new URLSearchParams(window.location.search);
    const res = urlParams.get("res");
 
    if (res && res !== "ok") {
        const mensajes = {
            valor_vacio:     { campo: "valor",     msg: "El valor es obligatorio." },
            valor_invalido:  { campo: "valor",     msg: "El valor debe ser un número mayor a 0." },
            categoria_vacia: { campo: "categoria", msg: "Debes seleccionar una categoría." },
            fecha_vacia:     { campo: "fecha",     msg: "La fecha es obligatoria." },
            fecha_invalida:  { campo: "fecha",     msg: "El formato de fecha no es válido." },
            error:           { campo: "valor",     msg: "Ocurrió un error. Intenta de nuevo." }
        };
        const info = mensajes[res];
        if (info) mostrar_errores(info.msg, info.campo);
    }
});
 
// ── Abrir ventana de confirmación (botón Guardar) ─────────────────────────────
const abrirConfirmacion = () => {
 
    if (!formulario) return;
    limpiar_errores();
 
    // Validar con la función original
    const respuesta = validar(formulario, reglas);
    if (!respuesta.esValido) {
        mostrar_errores(respuesta.mensaje, respuesta.campo);
        return;
    }
 
    // Validar fecha por separado (no está en las reglas de validar())
    const inputFecha = document.getElementById("input-fecha");
    if (!inputFecha || !inputFecha.value) {
        mostrar_errores("La fecha es obligatoria.", "fecha");
        return;
    }
    
    const fecha       = inputFecha.value;
    
    // ── Validar que la fecha no supere la fecha actual ────────────────────────
    const hoy = new Date().toISOString().split("T")[0];
    if (fecha > hoy) {
        mostrar_errores("La fecha no puede ser mayor a la fecha actual.", "fecha");
        return;
    }
 
    // ── Llenar resumen en el modal ────────────────────────────────────────────
    const inputValor  = formulario.elements["valor"];
    const selectCateg = document.getElementById("select-categoria");
    const inputDesc   = formulario.elements["descripcion"];
 
    const opcionCateg = selectCateg.options[selectCateg.selectedIndex];
    const tipo        = opcionCateg.dataset.tipo || "—";
    const nombreCateg = opcionCateg.text;
    const descripcion = inputDesc ? inputDesc.value.trim() : "";
    const valor       = inputValor.value;
 
    document.getElementById("resumen-valor").textContent     = "$" + parseFloat(valor).toLocaleString("es-CO");
    document.getElementById("resumen-categoria").textContent = nombreCateg;
 
    const resumenTipo = document.getElementById("resumen-tipo");
    resumenTipo.textContent = tipo === "Ingreso" ? "+ Ingreso" : "− Egreso";
    resumenTipo.className   = "confirmacion__fila-valor " + tipo.toLowerCase();
 
    const [y, m, d] = fecha.split("-");
    const meses = ["enero","febrero","marzo","abril","mayo","junio",
                   "julio","agosto","septiembre","octubre","noviembre","diciembre"];
    document.getElementById("resumen-fecha").textContent =
        `${parseInt(d)} de ${meses[parseInt(m) - 1]} de ${y}`;
 
    const filaDesc = document.getElementById("fila-descripcion");
    if (descripcion) {
        document.getElementById("resumen-descripcion").textContent = descripcion;
        filaDesc.style.display = "flex";
    } else {
        filaDesc.style.display = "none";
    }
 
    document.getElementById("modal-confirmacion").classList.add("activa");
};
 
// ── Cerrar modal y confirmar envío ────────────────────────────────────────────
const cerrarConfirmacion = () => {
    document.getElementById("modal-confirmacion").classList.remove("activa");
};
 
const confirmarGuardar = () => {
    document.getElementById("form-transaccion").submit();
};