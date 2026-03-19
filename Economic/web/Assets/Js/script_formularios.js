// Validación de formulario de registro
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
 
    // Validación de formato email
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
        mensaje: `El campo debe tener como mínimo ${regla.min} caracteres`,
        campo: nombreCampo
      };
    }
 
    // Validación de longitud máxima
    if (regla.max && elemento.value.length > regla.max) {
      return {
        esValido: false,
        mensaje: `El campo no debe exceder ${regla.max} caracteres`,
        campo: nombreCampo
      };
    }
  }
 
  return { esValido: true };
};
 
const formulario    = document.querySelector('.formulario-registro__form');
const mensaje_error = document.querySelector('.mensaje_error');
 
const reglas = {
    txtNombre: {
        required: true,
        mensaje: "El campo nombre es obligatorio (Ingresa tu nombre)"
    },
    txtEmail: {
        required: true,
        email: true,
        mensaje: "El campo email es obligatorio (Ingresa ru direccion de correo)",
        emailMensaje: "El formato del correo no es válido (ej: usuario@dominio.com)"
    },
    txtContrasena: {
        required: true,
        min: 8,
        max: 30,
        mensaje: "El campo contraseña es obligatorio (Ingresa una contraseña de 8 a 30 digitos)"
    },
    txtConfirmar: {
        required: true,
        min: 8,
        max: 30,
        mensaje: "Por favor vuelve a ingresar tu contraseña"
    }
};
 
const mostrar_errores = (error, campo = null) => {
    document.querySelectorAll('.error').forEach(el => el.classList.remove('error'));
 
    if (campo) {
        const elemento = document.querySelector(`input[name="${campo}"]`);
        if (elemento) elemento.classList.add("error");
    } else {
        const pass    = document.querySelector('input[name="txtContrasena"]');
        const confirm = document.querySelector('input[name="txtConfirmar"]');
        if (pass)    pass.classList.add("error");
        if (confirm) confirm.classList.add("error");
    }
 
    mensaje_error.textContent = error;
    mensaje_error.style.display = "block";
};
 
const limpiar_errores = () => {
    document.querySelectorAll('.error').forEach(el => el.classList.remove('error'));
    mensaje_error.style.display = "none";
};
 
document.addEventListener('DOMContentLoaded', function () {
    if (!formulario) return;
 
    // Mostrar errores del servidor
    const urlParams   = new URLSearchParams(window.location.search);
    const res         = urlParams.get('res');
    const mensajeError = document.querySelector('.mensaje_error');
 
    if (mensajeError && res) {
        let mensaje = '';
        switch (res) {
            case 'vacio':
                mensaje = 'Todos los campos son obligatorios, por favor ingresa tus datos';
                break;
            case 'pass_error':
                mensaje = 'Las contraseñas no coinciden';
                break;
            case 'cantidad_de_caracteres_error':
                mensaje = 'La contraseña debe tener mínimo 8 caracteres';
                break;
            case 'correo_duplicado':
                mensaje = 'El correo ya está registrado en el sistema';
                break;
            case 'error':
                mensaje = 'Ocurrió un error. Intenta de nuevo';
                break;
        }
 
        if (mensaje) {
            mensajeError.textContent = mensaje;
            mensajeError.style.display = 'block';
        }
 
        if (res === 'correo_duplicado') {
            const emailInput = document.querySelector('input[name="txtEmail"]');
            if (emailInput) emailInput.classList.add('error');
        }
 
        if (res === 'pass_error' || res === 'cantidad_de_caracteres_error') {
            const pass    = document.querySelector('input[name="txtContrasena"]');
            const confirm = document.querySelector('input[name="txtConfirmar"]');
            if (pass)    pass.classList.add('error');
            if (confirm) confirm.classList.add('error');
        }
    }
 
    // Limpiar errores al escribir
    formulario.querySelectorAll('input').forEach(input => {
        input.addEventListener('input', limpiar_errores);
    });
 
    formulario.addEventListener('submit', function (e) {
        e.preventDefault();
        limpiar_errores();
 
        // Validar todos los campos
        const respuesta = validar(formulario, reglas);
        if (!respuesta.esValido) {
            mostrar_errores(respuesta.mensaje, respuesta.campo);
            return;
        }
 
        // Validar que las contraseñas coincidan
        const pass    = document.querySelector('input[name="txtContrasena"]');
        const confirm = document.querySelector('input[name="txtConfirmar"]');
        if (pass.value !== confirm.value) {
            mostrar_errores("Las contraseñas no coinciden");
            return;
        }
 
        formulario.submit();
    });
});