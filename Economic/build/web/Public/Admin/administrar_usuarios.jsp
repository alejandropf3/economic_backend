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
                <a href="menu_principal.html" class="sidebar__link">
                    <i class="bi bi-house-fill"></i>
                    Inicio
                </a>
                <a href="historial_transacciones.html" class="sidebar__link">
                    <i class="bi bi-clock-history"></i>
                    Historial de transacciones
                </a>
                <a href="historial_resumenes.html" class="sidebar__link">
                    <i class="bi bi-file-text-fill"></i>
                    Historial de resúmenes
                </a>
                <a href="configuracion_usuario.html" class="sidebar__link">
                    <i class="bi bi-gear-fill"></i>
                    Opciones
                </a>
                <a href="administrar_usuarios.html" class="sidebar__link sidebar__link--activo">
                    <i class="bi bi-people-fill"></i>
                    Admin
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
                <a href="../index.html" class="sidebar__salir">
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
                <a href="../User/menu_principal.jsp" class="encabezado__link">Inicio</a>
                <a href="../User/historial_transacciones.jsp" class="encabezado__link">Historial de transacciones</a>
                <a href="../User/historial_resumenes.jsp" class="encabezado__link">Historial de resúmenes</a>
                <a href="../User/configuracion_usuario.jsp" class="encabezado__link">Opciones</a>

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

    <!-- Seccion main -->
    <main class="main-menu">
        <section class="main-menu__layout-general">
            <div class="layout-menu__informacion">
                <h2 class="layout-menu__titulo">Administrar usuarios</h2>
            </div>

            <table class="layout-general__tabla">
                <thead class="layout-general__tabla-encabezado">
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Correo electronico</th>
                        <th>Rol</th>
                        <th>Opciones</th>
                    </tr>
                </thead>

                <tbody class="layout-general__tabla-cuerpo">
                    <tr>
                        <td>1</td>
                        <td>Juan pedro</td>
                        <td>Jpedro2003@gmail.com</td>
                        <td>Usuario</td>
                        <td>
                            <a href="">Eliminar</a>
                            <a href="">Suspender</a>
                            <a href="">Activar</a>
                        </td>
                    </tr>

                    <tr>
                        <td>2</td>
                        <td>Maria andrea</td>
                        <td>mariandrea@gmail.com</td>
                        <td>Usuario</td>
                        <td>
                            <a href="">Eliminar</a>
                            <a href="">Suspender</a>
                            <a href="">Activar</a>
                        </td>
                    </tr>

                    <tr>
                        <td>3</td>
                        <td>Jose Daniel</td>
                        <td>Danielpro777@gmail.com</td>
                        <td>Usuario</td>
                        <td>
                            <a href="">Eliminar</a>
                            <a href="">Suspender</a>
                            <a href="">Activar</a>
                        </td>
                    </tr>

                    <tr>
                        <td>4</td>
                        <td>Lucas</td>
                        <td>Hechisero122@gmail.com</td>
                        <td>Usuario</td>
                        <td>
                            <a href="">Eliminar</a>
                            <a href="">Suspender</a>
                            <a href="">Activar</a>
                        </td>
                    </tr>

                    <tr>
                        <td>5</td>
                        <td>Dorian moreno</td>
                        <td>Alocondorian@gmail.com</td>
                        <td>Usuario</td>
                        <td>
                            <a href="">Eliminar</a>
                            <a href="">Suspender</a>
                            <a href="">Activar</a>
                        </td>
                    </tr>

                    <tr>
                        <td>6</td>
                        <td>Pepe alfonso</td>
                        <td>alfonso03@gmail.com</td>
                        <td>Usuario</td>
                        <td>
                            <a href="">Eliminar</a>
                            <a href="">Suspender</a>
                            <a href="">Activar</a>
                        </td>
                    </tr>
                </tbody>
            </table>


        </section>
    </main>

</body>

</html>