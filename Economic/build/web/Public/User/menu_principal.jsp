<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="../../Assets/Styles_principal.css">
</head>

<body>

    <!-- Checkbox para controlar el sidebar -->
    <input type="checkbox" id="sidebar-toggle" class="sidebar__toggle">

    <!-- Overlay del sidebar -->
    <label for="sidebar-toggle" class="sidebar__overlay"></label>

    <!-- Sidebar para móvil -->
    <aside class="sidebar">
        <div class="sidebar__contenido">
            <div class="sidebar__header">
                <h2 class="sidebar__logo">ECONOMIC</h2>
                <label for="sidebar-toggle" class="sidebar__cerrar">
                    <i class="bi bi-x-lg"></i>
                </label>
            </div>

            <nav class="sidebar__navegacion">
                <a href="menu_principal.jsp" class="sidebar__link sidebar__link--activo">
                    <i class="bi bi-house-fill"></i>
                    Inicio
                </a>
                <a href="historial_transacciones.jsp" class="sidebar__link">
                    <i class="bi bi-clock-history"></i>
                    Historial de transacciones
                </a>
                <a href="historial_resumenes.jsp" class="sidebar__link">
                    <i class="bi bi-file-text-fill"></i>
                    Historial de resúmenes
                </a>
                <a href="configuracion_usuario.jsp" class="sidebar__link">
                    <i class="bi bi-gear-fill"></i>
                    Opciones
                </a>
            </nav>

            <div class="sidebar__usuario">
                <div class="sidebar__usuario-info">
                    <div class="sidebar__usuario-icono">
                        <i class="bi bi-person-fill"></i>
                    </div>
                    <div class="sidebar__usuario-datos">
                        <p class="sidebar__usuario-nombre">Nombre de usuario</p>
                        <p class="sidebar__usuario-email">Cor****@gmail.com</p>
                    </div>
                </div>
                <a href="../../index.jsp" class="sidebar__salir">
                    <i class="bi bi-box-arrow-right"></i>
                    Cerrar sesión
                </a>
            </div>
        </div>
    </aside>

    <!-- Seccion de header -->
    <header class="main-header">
        <div class="encabezado">
            <h1 class="encabezado__logo">ECONOMIC</h1>

            <!-- Botón hamburguesa para móvil -->
            <label for="sidebar-toggle" class="hamburguesa">
                <span class="hamburguesa__linea"></span>
                <span class="hamburguesa__linea"></span>
                <span class="hamburguesa__linea"></span>
            </label>

            <nav class="encabezado__navegacion">
                <a href="menu_principal.jsp" class="encabezado__link">Inicio</a>
                <a href="historial_transacciones.jsp" class="encabezado__link">Historial de transacciones</a>
                <a href="historial_resumenes.jsp" class="encabezado__link">Historial de resúmenes</a>
                <a href="configuracion_usuario.jsp" class="encabezado__link">Opciones</a>

                <a href="#ventana-salida-confirmar" class="encabezado__icono">
                    <i class="bi bi-person-fill"></i>
                </a>

                <div id="ventana-salida-confirmar" class="ventana-salida">
                    <div class="ventana-salida__contenido">

                        <div class="ventana-salida__icono">
                            <div class="encabezado__icono">
                                <i class="bi bi-person-fill"></i>
                            </div>
                            <p>Nombre de usuario</p>
                        </div>

                        <div class="ventana-salida__informacion">
                            <p>Email</p>
                            <p>Cor****@gmail.com</p>
                        </div>

                        <a href="../../index.jsp" class="ventana-salida__link">
                            <i class="bi bi-box-arrow-right"></i>
                            <p>salir</p>
                        </a>

                    </div>
                </div>
            </nav>
        </div>
    </header>

    <!-- Seccion de main -->
    <main class="main-menu">

        <section class="main-menu__layout-menu">
            <div class="layout-menu__informacion">
                <h2 class="layout-menu__titulo"> Dinero actual</h2>
                <p class="layout-menu__texto">$0.00</p>
            </div>

            <a href="realizar_registro.jsp" class="boton--realizar-registro">Realizar registro</a>

            <section class="layout-menu__contenedor">

                <div class="contenedor__historial"> <!-- Seccion de historial de transacciones -->
                    <h2 class="historial__titulo">Ultimas transacciones</h2>
                    <div class="historial__ingresos">
                        <p class="historial__ingresos-texto">Ingreso+</p>
                        <p class="historial__ingresos-texto">$0.00</p>
                        <p class="historial__ingresos-texto">Categoria</p>
                        <p class="historial__ingresos-texto">Descripcion</p>
                    </div>

                    <div class="historial__egresos">
                        <p class="historial__egresos-texto">Egreso -</p>
                        <p class="historial__egresos-texto">$0.00</p>
                        <p class="historial__egresos-texto">Categoria</p>
                        <p class="historial__egresos-texto">Descripcion</p>
                    </div>
                    <a href="historial_transacciones.jsp" class="boton--ver-mas">Ver mas</a>
                </div>

                <div class="contenedor__resumenes"> <!-- Seccion de historial de resumenes -->

                    <h2 class="resumenes__titulo">Historial de resumenes</h2>

                    <div class="resumenes__elementos">

                        <div class="caja-resumen">
                            <h2 class="caja-resumen__titulo">Resumen semanal</h2>
                            <p class="caja-resumen__fecha">Dias 20/26</p>
                            <a href="" class="caja-resumen__link">Ver tabla</a>
                        </div>

                        <div class="caja-resumen">
                            <h2 class="caja-resumen__titulo">Resumen semanal</h2>
                            <p class="caja-resumen__fecha">Dias 20/26</p>
                            <a href="" class="caja-resumen__link">Ver tabla</a>
                        </div>

                        <div class="caja-resumen">
                            <h2 class="caja-resumen__titulo">Resumen semanal</h2>
                            <p class="caja-resumen__fecha">Dias 20/26</p>
                            <a href="" class="caja-resumen__link">Ver tabla</a>
                        </div>


                        <div class="caja-resumen">
                            <h2 class="caja-resumen__titulo">Resumen semanal</h2>
                            <p class="caja-resumen__fecha">Dias 20/26</p>
                            <a href="" class="caja-resumen__link">Ver tabla</a>
                        </div>

                        <div class="caja-resumen">
                            <h2 class="caja-resumen__titulo">Resumen semanal</h2>
                            <p class="caja-resumen__fecha">Dias 20/26</p>
                            <a href="" class="caja-resumen__link">Ver tabla</a>
                        </div>

                    </div>
                    <a href="historial_resumenes.jsp" class="boton--ver-mas">Ver mas</a>

                </div>

            </section>
        </section>
    </main>

</body>

</html>