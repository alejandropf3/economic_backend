const pass = document.querySelector('input[name="txtContrasena"]');
const confirm = document.querySelector('input[name="txtConfirmar"]');
const formulario = document.querySelector('.formulario-registro__form');
const mensaje_error = document.querySelector('.mensaje_error');

document.addEventListener('DOMContentLoaded', function () {

    const mostrar_errores = () => {
        pass.classList.add("error");
        confirm.classList.add("error");
        mensaje_error.textContent = "Las contraseñas no coinciden";
        mensaje_error.style.display = "block";
    };

    const limpiar_errores = () => {
        pass.classList.remove("error");
        confirm.classList.remove("error");
        mensaje_error.style.display = "none";
    };
    
    //Validamos que el campo de contraseña y confirmar contraseña coincidan
    if (formulario) {
        formulario.onsubmit = function (event) {
            const pass_value = pass.value;
            const confirm_value = confirm.value;

            if (pass_value !== confirm_value) {
                mostrar_errores();
                event.preventDefault(); // Bloquea el envío
                return false;
            } else {
                limpiar_errores();
            }
        };
    }
});