<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recuperacion de contraseña</title>
    <link rel="stylesheet" href="../../Assets/Styles_principal.css">
</head>

<body>

    <!-- Seccion del header -->
    <header class="main-header">
        <h1 class="logotipo">ECONOMIC</h1>
    </header>

    <!-- Seccion del main -->
    <Main class="main-menu">

        <section class="formulario-recuperacion">

            <div class="formulario__titulo"> <!-- Contenedor del titulo del formulario -->
                <h2>Recuperacion de contraseÃ±a</h2>
                <hr class="linea">
            </div>

            <form action="codigo_verificacion.jsp" method="get" class="formulario-recuperacion__form">

                <input type="email" class="formulario__input" placeholder="Correo electronico">

                <nav class="formulario__navegacion--alineado">
                    <a href="../../index.jsp" class="boton--salir">Salir</a>
                    <button type="submit" class="boton">Confirmar</button>
                </nav>

            </form>

        </section>

    </Main>

</body>

</html>