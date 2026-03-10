// Validación para formulario de Registro de Transacción
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
    
    // Validación de valor numérico
    if (regla.numeric && elemento.value.trim() !== "") {
      const numericRegex = /^[0-9]+(\.[0-9]+)?$/;
      if (!numericRegex.test(elemento.value.trim())) {
        return {
          esValido: false,
          mensaje: regla.numericMensaje || "El valor debe ser un número válido",
          campo: nombreCampo
        };
      }
    }
    
    // Validación de valor positivo
    if (regla.positive && elemento.value.trim() !== "") {
      const valor = parseFloat(elemento.value);
      if (isNaN(valor) || valor <= 0) {
        return {
          esValido: false,
          mensaje: regla.positiveMensaje || "El valor debe ser mayor a cero",
          campo: nombreCampo
        };
      }
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

const formulario = document.querySelector('.layout-general__contenedor-registro');
const mensaje_error = document.querySelector('.mensaje_error') || document.createElement('div');

// Crear contenedor de errores si no existe
if (!document.querySelector('.mensaje_error')) {
    mensaje_error.className = 'mensaje_error';
    mensaje_error.style.display = 'none';
    const contenedorForm = document.querySelector('.contenedor-registro__formulario');
    if (contenedorForm) {
        contenedorForm.parentElement.insertBefore(mensaje_error, contenedorForm);
    }
}

const reglas = {
    valor: { 
        required: true, 
        numeric: true,
        positive: true,
        mensaje: "El campo valor de la transacción es obligatorio",
        numericMensaje: "El valor debe ser un número válido (ej: 100.50)",
        positiveMensaje: "El valor debe ser mayor a cero"
    },
    categoria: { 
        required: true, 
        mensaje: "Debe seleccionar una categoría",
        patternMensaje: "Seleccione una categoría válida"
    },
    tipo: { 
        required: true, 
        min: 2,
        max: 50,
        mensaje: "El campo tipo es obligatorio",
        minMensaje: "El tipo debe tener como mínimo 2 caracteres",
        maxMensaje: "El tipo no debe exceder 50 caracteres"
    },
    descripcion: { 
        required: true, 
        min: 10,
        max: 200,
        mensaje: "El campo descripción es obligatorio",
        minMensaje: "La descripción debe tener como mínimo 10 caracteres",
        maxMensaje: "La descripción no debe exceder 200 caracteres"
    }
};

const mostrar_errores = (error, campo = null) => {
    // Limpiar errores anteriores
    document.querySelectorAll('.error').forEach(el => el.classList.remove('error'));
    document.querySelectorAll('.campo-error').forEach(el => el.style.display = 'none');
    
    if (campo) {
        // Marcar el campo con error
        const elemento = document.querySelector(`[name="${campo}"]`);
        if (elemento) elemento.classList.add("error");
        
        // Mostrar mensaje específico debajo del campo
        const contenedorCampo = elemento.closest('.campo-formulario');
        if (contenedorCampo) {
            const spanError = contenedorCampo.querySelector('.campo-error');
            if (spanError) {
                spanError.textContent = error;
                spanError.style.display = 'block';
            }
        }
    }
};

const limpiar_errores = () => {
    document.querySelectorAll('.error').forEach(el => el.classList.remove('error'));
    document.querySelectorAll('.campo-error').forEach(el => el.style.display = 'none');
    if (mensaje_error) mensaje_error.style.display = "none";
};

// Esperamos a que el DOM esté listo
document.addEventListener('DOMContentLoaded', function () {
    if (!formulario) return;
    
    // Limpiar errores al escribir
    formulario.querySelectorAll('input, select, textarea').forEach(element => {
        element.addEventListener('input', limpiar_errores);
        element.addEventListener('change', limpiar_errores);
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
