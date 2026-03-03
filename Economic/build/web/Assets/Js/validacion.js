document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('.formulario-registro__form');
    
    if (form) {
        form.onsubmit = function (event) {
            // Buscamos los inputs específicamente por su nombre
            const pass = document.querySelector('input[name="txtContrasena"]').value;
            const confirm = document.querySelector('input[name="txtConfirmar"]').value;

            if (pass !== confirm) {
                alert("¡Las contraseñas no coinciden!");
                event.preventDefault(); // Bloquea el envío
                return false;
            }
        };
    }
});