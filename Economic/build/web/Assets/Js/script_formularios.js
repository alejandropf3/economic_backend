// Función de validación (incluida directamente para evitar problemas con import)
const validar = (formulario, reglas) => {
  for (const nombreCampo in reglas) {
    const elemento = formulario.elements[nombreCampo];
    const regla = reglas[nombreCampo];
    
    if (!elemento) continue;
    
    // Validación de campo requerido
    if (regla.required && elemento.value.trim() === "") {
      return {
        esValido: false,
        mensaje: regla.mensaje,
        campo: nombreCampo
      };
    }
    
    // Validación de longitud mínima
    if (regla.min && elemento.value.length < regla.min) {
      return {
        esValido: false,
        mensaje: `El campo debe tener como mínimo ${regla.min} caracteres`,
        campo: nombreCampo
      };
    }
  }
  
  return {
    esValido: true
  };
};

const formulario = document.querySelector('.formulario-registro__form');
const mensaje_error = document.querySelector('.mensaje_error');

const reglas = {
    txtNombre: { required: true, mensaje: "El campo nombre es obligatorio" },
    txtEmail: { required: true, mensaje: "El campo email es obligatorio" },
    txtContrasena: { required: true, min: 8, mensaje: "El campo contraseña es obligatorio" },
    txtConfirmar: { required: true, min: 8, mensaje: "El campo confirmar contraseña es obligatorio" }
};

const mostrar_errores = (error, campo = null) => {
    // Limpiar errores anteriores
    document.querySelectorAll('.error').forEach(el => el.classList.remove('error'));
    
    if (campo) {
        const elemento = document.querySelector(`input[name="${campo}"]`);
        if (elemento) elemento.classList.add("error");
    } else {
        // Si no hay campo específico, marcar ambos campos de contraseña
        const pass = document.querySelector('input[name="txtContrasena"]');
        const confirm = document.querySelector('input[name="txtConfirmar"]');
        if (pass) pass.classList.add("error");
        if (confirm) confirm.classList.add("error");
    }
    
    mensaje_error.textContent = error;
    mensaje_error.style.display = "block";
};

const limpiar_errores = () => {
    document.querySelectorAll('.error').forEach(el => el.classList.remove('error'));
    mensaje_error.style.display = "none";
};

// Esperamos a que el DOM esté listo
document.addEventListener('DOMContentLoaded', function () {
    if (!formulario) return;
    
    formulario.addEventListener('submit', function (e) {
        e.preventDefault();
        
        limpiar_errores();
        
        // Validamos todos los campos
        const respuesta = validar(formulario, reglas);
        
        if (!respuesta.esValido) {
            mostrar_errores(respuesta.mensaje, respuesta.campo);
            return;
        }
        
        // Validación específica de que las contraseñas coincidan
        const pass = document.querySelector('input[name="txtContrasena"]');
        const confirm = document.querySelector('input[name="txtConfirmar"]');
        
        if (pass.value !== confirm.value) {
            mostrar_errores("Las contraseñas no coinciden");
            return;
        }
        
        // Si todo es válido, enviamos el formulario
        formulario.submit();
    });
});