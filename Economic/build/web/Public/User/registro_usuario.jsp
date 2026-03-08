<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro de usuario</title>
    <link rel="stylesheet" href="../../Assets/Styles_principal.css">
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

            <form action="/Economic/UsuarioControlador" method="POST" class="formulario-registro__form">
                
                <input type="text" name="txtNombre" class="formulario__input" placeholder="Nombre" required>

                <input type="email" name="txtEmail" class="formulario__input" placeholder="Correo electronico">

                <input type="password" name="txtContrasena" class="formulario__input" placeholder="Contraseña" required>

                <input type="password" name="txtConfirmar" class="formulario__input" placeholder="Confirma la contraseña" required>

                <nav class="formulario__navegacion">
                    <button class="boton" type="submit">Confirmar</button>
                    <a href="../../index.jsp" class="formulario__link">¿Ya tienes cuenta?</a>
                </nav>

            </form>

        </section>
    </main>
                <script src="../../Assets/JS/validaciones.js"></script>
</body>
</html>