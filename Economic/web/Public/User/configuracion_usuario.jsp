<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Configuracion</title>
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
                <a href="historial_resumenes.jsp" class="sidebar__link">
                    <i class="bi bi-file-text-fill"></i>
                    Historial de resúmenes
                </a>
                <a href="configuracion_usuario.jsp" class="sidebar__link sidebar__link--activo">
                    <i class="bi bi-gear-fill"></i>
                    Opciones
                </a>
                <a href="../Admin/administrar_usuarios.jsp" class="sidebar__link">
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
                <a href="../../index.jsp" class="sidebar__salir">
                    <i class="bi bi-box-arrow-right"></i>
                    Cerrar sesion
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
                <a href="../Admin/administrar_usuarios.jsp" class="encabezado__link">Admin</a>

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
                <h2 class="layout-menu__titulo">Configuracion de usuario</h2>
            </div>

            <div class="main-menu__contenedor-izquierda">
                <form action="" method="get" class="contenedor-izquierda__formulario">

                    <div class="formulario__campos-usuario">
                        <label for="nombre_usuario" class="formulario__campos-label">Nombre</label>
                        <input type="text" id="nombre_usuario" class="formulario__campos-input"
                            placeholder="Nombre de usuario">
                    </div>

                    <div class="formulario__campos-usuario">
                        <label for="contraseña_usuario" class="formulario__campos-label">Contraseña</label>
                        <input type="text" id="contraseña_usuario" class="formulario__campos-input"
                            placeholder="Contra123">
                    </div>

                    <div class="formulario__campos-usuario">
                        <label for="correo_usuario" class="formulario__campos-label">Correo electronico</label>
                        <input type="email" id="correo_usuario" class="formulario__campos-input"
                            placeholder="Cor****@gmail.com">
                    </div>

                    <div class="formulario__campos-usuario">
                        <label for="respado_correo_usuario" class="formulario__campos-label">Respaldo correo
                            electronico</label>
                        <input type="email" id="respado_correo_usuario" class="formulario__campos-input"
                            placeholder="Res****@gmail.com">
                    </div>

                    <div class="formulario__campos-usuario">
                        <label for="dinero_diponible" class="formulario__campos-label">Dinero disponible</label>
                        <input type="number" id="dinero_diponible" class="formulario__campos-input" placeholder="$0.00">
                    </div>

                </form>
            </div>

            <div class="main-menu__contenedor-derecha">

                <form action="" method="get" class="contenedor-derecha__formulario">
                    <div class="formulario__icono">
                        <svg xmlns="http://www.w3.org/2000/svg" width="180px" height="180px" fill="currentColor"
                            class="bi bi-person-fill" viewBox="0 0 16 16">
                            <path d="M3 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6" />
                        </svg>
                    </div>

                    <div class="formulario__campo-imagen">
                        <label for="foto_usuario" class="formulario__imagen-label">Cambiar foto</label>
                        <input type="file" class="formulario__imagen-input" id="foto_usuario">
                    </div>
                </form>

                <div class="contenedor-derecha__caja-categorias">
                    <h2 class="caja-categorias__titulo">Categorias</h2>
                    <div class="caja-categorias__contenido">

                        <div class="contenido__item ingreso">
                            <h2 class="item__titulo">Pago de salario</h2>
                            <p class="item__parrafo">Ingreso +</p>
                            <nav class="item__navegacion">
                                <a href="#ventana-modificar-categoria-confirmar" class="boton--editar">Editar</a>
                                <a href="" class="boton--eliminar">Eliminar</a>
                            </nav>
                        </div>

                        <div class="contenido__item egreso">
                            <h2 class="item__titulo">Pago de servicios</h2>
                            <p class="item__parrafo">Egreso -</p>
                            <nav class="item__navegacion">
                                <a href="#ventana-modificar-categoria-confirmar" class="boton--editar">Editar</a>
                                <a href="" class="boton--eliminar">Eliminar</a>
                            </nav>
                        </div>

                    </div>
                    <a href="#ventana-crear-categoria-confirmar" class="boton--crear-categoria">Crear categoria +</a>

                </div>

            </div>

            <div id="ventana-crear-categoria-confirmar" class="ventana-crear-categoria">
                <form action="configuracion_usuario.html" method="get"
                    class="ventana-crear-categoria__formulario-crear">
                    <h2 class="formulario-crear__titulo">Crear categoria</h2>

                    <div class="formulario-crear__contenido">
                        <input type="text" placeholder="Nombre categoria" class="contenido__input">
                        <select name="" id="" class="contenido__input">
                            <option value="" disabled selected>Tipo</option>
                            <option value="">Ingreso +</option>
                            <option value="">Egreso -</option>
                        </select>
                    </div>

                    <nav class="formulario-crear__navegacion">
                        <a href="#ventana-crear-categoria-cerrar" class="boton--cancelar">Cancelar</a>
                        <button type="submit" class="boton--guardar">Guardar</button>
                    </nav>
                </form>
            </div>

            <div id="ventana-modificar-categoria-confirmar" class="ventana-modificar-categoria">
                <form action="configuracion_usuario.html" method="get"
                    class="ventana-modificar-categoria__formulario-editar">
                    <h2 class="formulario-editar__titulo">Editar categoria</h2>

                    <div class="formulario-editar__contenido">
                        <input type="text" placeholder="Nuevo nombre categoria" class="contenido__input">
                        <select name="" id="" class="contenido__input">
                            <option value="" disabled selected>Nuevo tipo</option>
                            <option value="">Ingreso +</option>
                            <option value="">Egreso -</option>
                        </select>
                    </div>

                    <nav class="formulario-editar__navegacion">
                        <a href="#ventana-modificar-categoria-cerrar" class="boton--cancelar">Cancelar</a>
                        <button type="submit" class="boton--guardar">Guardar</button>
                    </nav>
                </form>
            </div>

        </section>
    </main>

</body>

</html>