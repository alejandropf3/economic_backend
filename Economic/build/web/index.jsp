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
    <script src="Assets/Js/validacion_login.js" defer></script>
    
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

                <input type="email" name="txtCorreo" class="formulario__input" placeholder="Correo electronico">

                <input type="password" name="txtContrasena" class="formulario__input" placeholder="Contraseña">

                <span class="mensaje_error"></span>

                <a href="Public/User/recuperacion_contraseña.jsp" class="formulario-inicio__link">Olvide mi contraseña</a>

                <nav class="formulario__navegacion">
                    <button class="boton" type="submit">Confirmar</button>
                    <a href="Public/User/registro_usuario.jsp" class="formulario__link">¿Aun no tienes una cuenta?</a>
                </nav>

            </form>
        </section>
    </main>

    <!-- Seccion de pie de pagina -->
    <footer class="main-footer">
        
    </footer>


</body>

</html>
