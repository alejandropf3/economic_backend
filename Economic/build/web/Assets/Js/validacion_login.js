// Validación para formulario de Login
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
    
    // Validación de email
    if (regla.email && elemento.value.trim() !== "") {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(elemento.value.trim())) {
        return {
          esValido: false,
          mensaje: regla.emailMensaje || "El formato del correo electrónico no es válido",
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
  } 
  return {
    esValido: true
  };
};

const formulario = document.querySelector('.formulario-inicio__form');
const mensaje_error = document.querySelector('.mensaje_error') || document.createElement('div');

// Crear contenedor de errores si no existe
if (!document.querySelector('.mensaje_error')) {
    mensaje_error.className = 'mensaje_error';
    mensaje_error.style.display = 'none';
    formulario?.parentElement?.insertBefore(mensaje_error, formulario);
}

const reglas = {
    txtCorreo: { 
        required: true, 
        email: true,
        mensaje: "El campo correo electrónico es obligatorio",
        emailMensaje: "El formato del correo electrónico no es válido (ej: usuario@dominio.com)"
    },
    txtContrasena: { 
        required: true, 
        min: 8,
        max: 15,
        mensaje: "El campo contraseña es obligatorio",
        minMensaje: "La contraseña debe tener como mínimo 8 caracteres",
        maxMensaje: "La contraseña no debe exceder 15 caracteres"
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
    // Mostrar mensajes de error del servidor
    const urlParams = new URLSearchParams(window.location.search);
    const res = urlParams.get('res');
    const mensajeError = document.querySelector('.mensaje_error');

    if (mensajeError && res) {
        let mensaje = '';

        switch(res) {
            case 'user_not_found':
                mensaje = 'El usuario no existe en el sistema';
                break;
            case 'invalid_password':
                mensaje = 'La contraseña es incorrecta';
                break;
            case 'error':
                mensaje = 'Error al iniciar sesión. Verifique sus credenciales';
                break;
            default:
                mensaje = 'Error desconocido';
        }

        mensajeError.textContent = mensaje;
        mensajeError.style.display = 'block';

        // Marcar campos correspondientes como error
        if (res === 'user_not_found' || res === 'invalid_password') {
            const emailInput = document.querySelector('input[name="txtCorreo"]');
            const passInput = document.querySelector('input[name="txtContrasena"]');

            if (emailInput) emailInput.classList.add('error');
            if (passInput) passInput.classList.add('error');
        }
    }
    
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
