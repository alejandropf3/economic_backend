// Validación para formulario de Código de Verificación
const validar = (formulario, reglas) => {
  for (const nombreCampo in reglas) {
    const elemento = formulario.elements[nombreCampo];
    const regla = reglas[nombreCampo];
    
    if (!elemento) continue;
    
    // Validación de campo requerido
    if (regla.required && elemento.value.trim() === "") {
      return {
        esValido: false,
        mensaje: regla.mensaje || `El campo ${nombreCampo} es obligatorio`,
        campo: nombreCampo
      };
    }
    
    // Validación de longitud exacta
    if (regla.exactLength && elemento.value.length !== regla.exactLength) {
      return {
        esValido: false,
        mensaje: regla.exactLengthMensaje || `El campo debe tener exactamente ${regla.exactLength} caracteres`,
        campo: nombreCampo
      };
    }
    
    // Validación de longitud mínima
    if (regla.min && elemento.value.length < regla.min) {
      return {
        esValido: false,
        mensaje: regla.minMensaje || `El campo debe tener como mínimo ${regla.min} caracteres`,
        campo: nombreCampo
      };
    }
    
    // Validación de longitud máxima
    if (regla.max && elemento.value.length > regla.max) {
      return {
        esValido: false,
        mensaje: regla.maxMensaje || `El campo no debe exceder ${regla.max} caracteres`,
        campo: nombreCampo
      };
    }
    
    // Validación de solo números
    if (regla.numeric && elemento.value.trim() !== "") {
      const numericRegex = /^[0-9]+$/;
      if (!numericRegex.test(elemento.value.trim())) {
        return {
          esValido: false,
          mensaje: regla.numericMensaje || "El campo solo debe contener números",
          campo: nombreCampo
        };
      }
    }
    
    // Validación de patrón personalizado
    if (regla.pattern && elemento.value.trim() !== "") {
      const pattern = new RegExp(regla.pattern);
      if (!pattern.test(elemento.value)) {
        return {
          esValido: false,
          mensaje: regla.patternMensaje || "El formato del campo no es válido",
          campo: nombreCampo
        };
      }
    }
  }
  
  return {
    esValido: true
  };
};

const formulario = document.querySelector('.formulario-codigo__form');
const mensaje_error = document.querySelector('.mensaje_error') || document.createElement('div');

// Crear contenedor de errores si no existe
if (!document.querySelector('.mensaje_error')) {
    mensaje_error.className = 'mensaje_error';
    mensaje_error.style.display = 'none';
    formulario?.parentElement?.insertBefore(mensaje_error, formulario);
}

const reglas = {
    codigo: { 
        required: true, 
        exactLength: 6,
        numeric: true,
        mensaje: "El campo código es obligatorio",
        exactLengthMensaje: "El código debe tener exactamente 6 caracteres",
        numericMensaje: "El código solo debe contener números"
    }
};

const mostrar_errores = (error, campo = null) => {
    // Limpiar errores anteriores
    document.querySelectorAll('.error').forEach(el => el.classList.remove('error'));
    
    if (campo) {
        const elemento = document.querySelector(`input[name="${campo}"]`);
        if (elemento) elemento.classList.add("error");
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
    
    // Limpiar errores al escribir
    formulario.querySelectorAll('input').forEach(input => {
        input.addEventListener('input', limpiar_errores);
    });
    
    formulario.addEventListener('submit', function (e) {
        e.preventDefault();
        
        limpiar_errores();
        
        // Validamos todos los campos
        const respuesta = validar(formulario, reglas);
        
        if (!respuesta.esValido) {
            mostrar_errores(respuesta.mensaje, respuesta.campo);
            return;
        }
        
        // Si todo es válido, enviamos el formulario
        formulario.submit();
    });
});
