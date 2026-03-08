<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Historial de resumenes</title>
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
                <a href="menu_principal.jsp" class="sidebar__link">
                    <i class="bi bi-house-fill"></i>
                    Inicio
                </a>
                <a href="historial_transacciones.jsp" class="sidebar__link">
                    <i class="bi bi-clock-history"></i>
                    Historial de transacciones
                </a>
                <a href="historial_resumenes.jsp" class="sidebar__link sidebar__link--activo">
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

    <!-- Seccion del header -->
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

                        <a href="../../index.html" class="ventana-salida__link">
                            <i class="bi bi-box-arrow-right"></i>
                            <p>salir</p>
                        </a>

                    </div>
                </div>
            </nav>
        </div>
    </header>

    <!-- Seccion del main -->
    <main class="main-menu">
        <section class="main-menu__layout-historial-resumenes">
            <div class="layout-menu__informacion">
                <h2 class="layout-menu__titulo">Historial de Resumenes</h2>
            </div>

            <div class="layout-menu__contenedor-historial">

                <div class="contenedor-historial__encabezado">
                    <div class="contenedor-historial__selector">
                        <p class="contenedor-historial__encabezado-texto">Seleccionar fecha:</p>
                        <input type="date" name="fecha_seleccion" id="fecha_seleccion" class="contenedor-historial__selector-date">
                    </div>
                </div>

                <div class="contenedor-historial__alinear"> <!-- Caja para alinear los elementos -->

                    <div class="caja-resumen">
                        <h2 class="caja-resumen__titulo">Resumen semanal</h2>
                        <p class="caja-resumen__fecha">Dias 20/26</p>
                        <a href="" class="caja-resumen__link">Ver tabla</a>
                    </div>

                </div>

            </div>

            <div class="layout-menu__contenedor-vista-previa"> <!-- Contenedor de vista previa -->

                <h2 class="contenedor-vista-previa__titulo">Resumen semanal</h2>
                <div class="contenedor-vista-previa__encabezado"> <!-- Contenedor de encabezado de vista previa-->
                    <p class="contenedor-vista-previa__encabezado-texto">Días: 20/26</p>
                    <p class="contenedor-vista-previa__encabezado-texto">Mes: Oct</p>
                    <p class="contenedor-vista-previa__encabezado-texto">Año: 2025</p>
                </div>

                <table class="contenido__transacciones-table">
                    <thead>
                        <tr>
                            <th>Fecha</th>
                            <th>Total de transaccion:</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>20/10/2025</td>
                            <td class="transacciones-table__ingreso">$100000</td>
                        </tr>
                        <tr>
                            <td>21/10/2025</td>
                            <td class="transacciones-table__ingreso">$200000</td>
                        </tr>
                        <tr>
                            <td>22/10/2025</td>
                            <td class="transacciones-table__egreso">$ -50000</td>
                        </tr>
                        <tr>
                            <td>23/10/2025</td>
                            <td class="transacciones-table__egreso">$ -40000</td>
                        </tr>
                        <tr>
                            <td>24/10/2025</td>
                            <td class="transacciones-table__egreso">$ -22000</td>
                        </tr>
                        <tr>
                            <td>25/10/2025</td>
                            <td class="transacciones-table__ingreso">$70000</td>
                        </tr>
                        <tr>
                            <td>26/10/2025</td>
                            <td class="transacciones-table__egreso">$ -33000</td>
                        </tr>
                    </tbody>
                </table>

                <div class="contenido-total-semanal">
                    <h2 class="contenido-total-semanal__titulo">Total semanal = $270000</h2>
                </div>

            </div>

        </section>
    </main>



</body>

</html>