<%@page contentType="text/html" pageEncoding="UTF-8"%>
﻿<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cambio de contraseña</title>
    <link rel="stylesheet" href="../../Assets/Styles_principal.css">
    <script src="../../Assets/Js/validacion_cambio_contrasena.js" defer></script>
</head>

<body>

    <!-- Campo de header -->
    <header class="main-header">
        <h1 class="logotipo">ECONOMIC</h1>
    </header>

    <!-- Campo de main -->
    <main class="main-menu">
        <section class="formulario-cambio-contrasena">

            <div class="formulario__titulo"> <!-- Contenedor del titulo del formulario -->
                <h2>Cambio de contraseña</h2>
                <hr class="linea">
            </div>

            <form action="menu_principal.jsp" method="get" class="formulario-cambio-contrasena__form">

                <input type="password" name="nuevaContrasena" class="formulario__input" placeholder="Nueva Contraseña">

                <input type="password" name="confirmarContrasena" class="formulario__input" placeholder="Confirma la contraseña">

                <span class="mensaje_error"></span>

                <button class="boton" type="submit">Guardar</button>

            </form>

        </section>
    </main>


</body>

</html>
