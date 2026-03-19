<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String resReg    = (String) request.getAttribute("res");
    String valNombre = request.getAttribute("txtNombre") != null
                       ? (String) request.getAttribute("txtNombre") : "";
    String valEmail  = request.getAttribute("txtEmail")  != null
                       ? (String) request.getAttribute("txtEmail")  : "";

    String errorNombre = "";
    String errorEmail  = "";
    String msgError    = "";

    if (resReg != null) {
        switch (resReg) {
            case "vacio":
                errorNombre = "error";
                errorEmail  = "error";
                msgError    = "Todos los campos son obligatorios."; break;
            case "correo_duplicado":
                errorEmail  = "error";
                msgError    = "Este correo ya está registrado."; break;
            case "pass_error":
                msgError    = "Las contraseñas no coinciden."; break;
            case "cantidad_de_caracteres_error":
                msgError    = "La contraseña debe tener mínimo 8 caracteres."; break;
            case "error":
                msgError    = "Error al registrar. Intenta de nuevo."; break;
        }
    }

    boolean registroExitoso = "exitoso".equals(request.getAttribute("registroExitoso"));
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro de usuario</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Assets/Styles_principal.css">
</head>
<body>

    <header class="main-header">
        <h1 class="logotipo">ECONOMIC</h1>
    </header>

    <main class="main-menu">
        <section class="formulario-registro">
            <div class="formulario__titulo">
                <h2>Registro de usuario</h2>
                <hr class="linea">
            </div>

            <form action="${pageContext.request.contextPath}/UsuarioControlador"
                  method="POST"
                  class="formulario-registro__form">

                <input type="text"
                       name="txtNombre"
                       class="formulario__input <%= errorNombre %>"
                       placeholder="Nombre"
                       value="<%= valNombre %>">

                <input type="email"
                       name="txtEmail"
                       class="formulario__input <%= errorEmail %>"
                       placeholder="Correo electronico"
                       value="<%= valEmail %>">

                <input type="password"
                       name="txtContrasena"
                       class="formulario__input"
                       placeholder="Contraseña">

                <input type="password"
                       name="txtConfirmar"
                       class="formulario__input"
                       placeholder="Confirma la contraseña">

                <span class="mensaje_error"
                      style="display:<%= resReg != null ? "block" : "none" %>;">
                    <%= msgError %>
                </span>

                <nav class="formulario__navegacion">
                    <button class="boton" type="submit">Confirmar</button>
                    <a href="${pageContext.request.contextPath}/index.jsp"
                       class="formulario__link">¿Ya tienes cuenta?</a>
                </nav>
            </form>
        </section>
    </main>

    <%-- ══ VENTANA CONFIRMACIÓN DE REGISTRO EXITOSO ══ --%>
    <% if (registroExitoso) { %>
    <div class="ventana-exito__overlay">
        <div class="ventana-exito__card">
            <div class="ventana-exito__icono">
                <i class="bi bi-check-lg"></i>
            </div>
            <h2 class="ventana-exito__titulo">¡Registro exitoso!</h2>
            <p class="ventana-exito__mensaje">
                Tu cuenta ha sido creada correctamente.<br>
                Ya puedes iniciar sesión con tu correo y contraseña.
            </p>
            <a href="${pageContext.request.contextPath}/index.jsp"
               class="ventana-exito__boton">
                Iniciar sesión
            </a>
        </div>
    </div>
    <% } %>

    <script src="${pageContext.request.contextPath}/Assets/Js/script_formularios.js"></script>
</body>
</html>
