<%-- 
    Document   : index
    Created on : 19/02/2026, 9:10:33 a. m.
    Author     : Propietario
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="./Assets/Styles_principal.css">
</head>

<body>

    <!-- Seccion de header -->
    <header class="main-header">
        <h1 class="logotipo">ECONOMIC</h1>
    </header>
    <!-- Seccion de contenido -->
    <main class="main-menu">

        <section class="formulario-inicio">
            <div class="formulario__titulo">
                <h2>Iniciar Sesíon</h2>
                <hr class="linea">
            </div>
            <form action="LoginControlador" method="POST" class="formulario-inicio__form">

                <input type="email" name="txtCorreo" class="formulario__input" placeholder="Correo electronico" required>

                <input type="password" name="txtContrasena" class="formulario__input" placeholder="Contraseña" required>

                <a href="Public/User/recuperacion_contraseña.jsp" class="formulario-inicio__link">Olvide mi contraseña</a>

                <nav class="formulario__navegacion">
                    <button class="boton" type="submit">Confirmar</button>
                    <a href="Public/User/registro_usuario.jsp" class="formulario__link">¿Aun no tienes una cuenta?</a>
                </nav>

                <% if(request.getParameter("res") != null) { %>
                    <p style="color: red; text-align: center;">Correo o contraseña incorrectos.</p>
                <% } %>
            </form>
        </section>
    </main>

    <!-- Seccion de pie de pagina -->
    <footer class="main-footer">
        <div class="pie-pagina">
            <nav class="pie-pagina__navegacion">
                <a href="https://github.com/alejandropf3" class="pie-pagina__link-icono"><i
                        class="bi bi-github"></i></a>
                <a href="" class="pie-pagina__link-icono"><i class="bi bi-instagram"></i></a>
                <a href="" class="pie-pagina__link-icono"><i class="bi bi-facebook"></i></a>
            </nav>

            <h2 class="pie-pagina__logo">ECONOMIC</h2>

            <div class="pie-pagina__contacto">
                <div class="pie-pagina__contacto-informacion">
                    <i class="bi bi-envelope-fill"></i>
                    <p>alejandropefa31@gmail.com</p>
                </div>
                <div class="pie-pagina__contacto-informacion">
                    <i class="bi bi-telephone-fill"></i>
                    <p>+57 3102317070</p>
                </div>
            </div>
        </div>
    </footer>


</body>

</html>
