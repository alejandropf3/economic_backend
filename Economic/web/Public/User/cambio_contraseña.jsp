<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cambio de contraseña</title>
    <link rel="stylesheet" href="../../Assets/Styles_principal.css">
    <script src="../../Assets/Js/validacion_cambio_contrasena.js" defer></script>
</head>

<body>

    <%-- Protección: solo accesible si el token fue verificado ─────────────── --%>
    <%
        Boolean tokenVerificado = (Boolean) session.getAttribute("tokenVerificado");
        if (tokenVerificado == null || !tokenVerificado) {
            response.sendRedirect(request.getContextPath()
                    + "/Public/User/recuperacion_contrase%C3%B1a.jsp?res=acceso_denegado");
            return;
        }

        // Manejo de errores del servidor
        String res = request.getParameter("res");
        String mensajeServidor = "";
        if (res != null) {
            switch (res) {
                case "contrasena_vacia":
                    mensajeServidor = "Debes ingresar la nueva contraseña.";
                    break;
                case "confirmacion_vacia":
                    mensajeServidor = "Debes confirmar la nueva contraseña.";
                    break;
                case "contrasena_muy_corta":
                    mensajeServidor = "La contraseña debe tener mínimo 8 caracteres.";
                    break;
                case "contrasena_muy_larga":
                    mensajeServidor = "La contraseña no debe exceder 15 caracteres.";
                    break;
                case "contrasenas_no_coinciden":
                    mensajeServidor = "Las contraseñas no coinciden.";
                    break;
                case "error_general":
                    mensajeServidor = "Ocurrió un error al actualizar. Intenta de nuevo.";
                    break;
                default:
                    mensajeServidor = "Ocurrió un error inesperado.";
                    break;
            }
        }
    %>

    <!-- Campo de header -->
    <header class="main-header">
        <h1 class="logotipo">ECONOMIC</h1>
    </header>

    <!-- Campo de main -->
    <main class="main-menu">
        <section class="formulario-cambio-contrasena">

            <div class="formulario__titulo">
                <h2>Cambio de contraseña</h2>
                <hr class="linea">
            </div>

            <%-- Error del servidor --%>
            <% if (!mensajeServidor.isEmpty()) { %>
                <span class="mensaje_error" style="display:block;"><%= mensajeServidor %></span>
            <% } %>

            <%-- action → servlet, method POST --%>
            <form action="<%= request.getContextPath() %>/CambioContrasenaControlador"
                  method="post"
                  class="formulario-cambio-contrasena__form">

                <input type="password" name="nuevaContrasena"
                       class="formulario__input" placeholder="Nueva Contraseña">

                <input type="password" name="confirmarContrasena"
                       class="formulario__input" placeholder="Confirma la contraseña">

                <%-- Span vacío para errores del JS cliente --%>
                <span class="mensaje_error"></span>

                <button class="boton" type="submit">Guardar</button>

            </form>

        </section>
    </main>

</body>

</html>
