<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recuperacion de contraseña</title>
    <link rel="stylesheet" href="../../Assets/Styles_principal.css">
    <script src="../../Assets/Js/validacion_recuperacion.js" defer></script>
</head>

<body>

    <!-- Seccion del header -->
    <header class="main-header">
        <h1 class="logotipo">ECONOMIC</h1>
    </header>

    <!-- Seccion del main -->
    <main class="main-menu">

        <section class="formulario-recuperacion">

            <div class="formulario__titulo">
                <h2>Recuperacion de contraseña</h2>
                <hr class="linea">
            </div>

            <%-- Manejo de errores provenientes del servidor --%>
            <%
                String res = request.getParameter("res");
                String mensajeServidor = "";
                if (res != null) {
                    switch (res) {
                        case "correo_no_encontrado":
                            mensajeServidor = "No existe una cuenta registrada con ese correo electrónico.";
                            break;
                        case "correo_vacio":
                            mensajeServidor = "El campo correo electrónico es obligatorio.";
                            break;
                        default:
                            mensajeServidor = "Ocurrió un error inesperado. Intenta de nuevo.";
                            break;
                    }
                }
            %>

            <%-- Mostrar error del servidor solo si existe --%>
            <% if (!mensajeServidor.isEmpty()) { %>
                <span class="mensaje_error" style="display:block;">
                    <%= mensajeServidor %>
                </span>
            <% } %>

            <%-- action apunta al servlet; method POST para no exponer el correo en la URL --%>
            <form action="<%=request.getContextPath()%>/RecuperacionControlador" method="post" class="formulario-recuperacion__form">

                <input type="email" name="correo" class="formulario__input" placeholder="Correo electronico"
                       value="<%= res != null ? "" : "" %>">

                <%-- Span vacío para errores del JS del cliente --%>
                <span class="mensaje_error"></span>

                <nav class="formulario__navegacion--alineado">
                    <a href="<%=request.getContextPath()%>/index.jsp" class="boton--salir">Salir</a>
                    <button type="submit" class="boton">Confirmar</button>
                </nav>

            </form>

        </section>

    </main>

</body>

</html>
