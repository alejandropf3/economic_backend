<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Codigo de recuperacion</title>
    <link rel="stylesheet" href="../../Assets/Styles_principal.css">
    <script src="../../Assets/Js/validacion_codigo.js" defer></script>
</head>

<body>

    <%-- Protección: si no hay sesión de recuperación activa, redirigir ─────── --%>
    <%
        String correoSesion = (String) session.getAttribute("correoRecuperacion");
        if (correoSesion == null) {
            response.sendRedirect(request.getContextPath()
                    + "/Public/User/recuperacion_contrase%C3%B1a.jsp?res=sesion_expirada");
            return;
        }

        // Enmascarar el correo: mostrar "Co****@gmail.com"
        String correoVisible;
        int arrobaIndex = correoSesion.indexOf("@");
        if (arrobaIndex > 2) {
            correoVisible = correoSesion.substring(0, 2)
                          + "****"
                          + correoSesion.substring(arrobaIndex);
        } else {
            correoVisible = correoSesion.substring(0, 1) + "****" + correoSesion.substring(arrobaIndex);
        }

        // Manejo de errores del servidor
        String res = request.getParameter("res");
        String mensajeServidor = "";
        if (res != null) {
            switch (res) {
                case "codigo_invalido":
                    mensajeServidor = "El código es incorrecto o ha expirado. Intenta de nuevo.";
                    break;
                case "codigo_vacio":
                    mensajeServidor = "Debes ingresar el código de verificación.";
                    break;
                case "sesion_expirada":
                    mensajeServidor = "La sesión ha expirado. Vuelve a ingresar tu correo.";
                    break;
                default:
                    mensajeServidor = "Ocurrió un error inesperado. Intenta de nuevo.";
                    break;
            }
        }
    %>

    <!--Seccion del header-->
    <header class="main-header">
        <h1 class="logotipo">ECONOMIC</h1>
    </header>

    <!-- Seccion del main-->
    <main class="main-menu">

        <section class="formulario-codigo">

            <div class="formulario__titulo">
                <h2>Recuperacion de contraseña</h2>
                <hr class="linea">
            </div>

            <div class="formulario__mensaje">
                <p class="formulario__texto">Hemos enviado un codigo de verificacion para recuperar la contraseña a:</p>
                <%-- Correo enmascarado dinámico desde sesión --%>
                <p><%= correoVisible %></p>
                <hr class="linea--texto">
            </div>

            <%-- Error del servidor --%>
            <% if (!mensajeServidor.isEmpty()) { %>
                <span class="mensaje_error" style="display:block;"><%= mensajeServidor %></span>
            <% } %>

            <%-- action → servlet, method POST --%>
            <form action="<%= request.getContextPath() %>/CodigoVerificacionControlador" method="post" class="formulario-codigo__form">

                <label for="codigo" class="formulario__label">Ingresa el codigo</label>
                <input type="text" name="codigo" id="codigo" class="formulario__input"
                       maxlength="6" autocomplete="one-time-code">

                <%-- Span vacío para errores del JS cliente --%>
                <span class="mensaje_error"></span>

                <nav class="formulario__navegacion">
                    <button class="boton" type="submit">Confirmar</button>
                    <%-- "Generar nuevo código" reenvía al servidor para crear y enviar uno nuevo --%>
                    <a href="<%= request.getContextPath() %>/RecuperacionControlador?accion=reenviar&correo=<%= java.net.URLEncoder.encode(correoSesion, "UTF-8") %>"
                       class="formulario__link">Generar nuevo codigo</a>
                </nav>

            </form>

        </section>

    </main>

</body>

</html>
