<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Codigo de recuperacion</title>
    <link rel="stylesheet" href="../../Assets/Styles_principal.css">
</head>

<body>

    <!--Seccion del header-->
    <header class="main-header">
        <h1 class="logotipo">ECONOMIC</h1>
    </header>

    <!-- Seccion del main-->
    <main class="main-menu">

        <section class="formulario-codigo">

            <div class="formulario__titulo"> <!-- Contenedor del titulo del formulario -->
                <h2>Recuperacion de contraseña</h2>
                <hr class="linea">
            </div>

            <div class="formulario__mensaje"> <!-- Contenedor del texto del formulario -->
                <p class="formulario__texto">Hemos enviado un codigo de verificacion para recuperar la contraseña a:</p>
                <p>Cor****@gmail.com</p>
                <hr class="linea--texto">
            </div>

            <form action="cambio_contrase�a.jsp" method="get" class="formulario-codigo__form">
                <label for="" class="formulario__label">Ingresa el codigo</label>
                <input type="text" class="formulario__input">

                <nav class="formulario__navegacion">
                    <button class="boton" type="submit">Confirmar</button>
                    <a href="" class="formulario__link">Generar nuevo codigo</a>
                </nav>
            </form>


        </section>

    </main>


</body>

</html>