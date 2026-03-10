<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Realizar registro</title>
    <link rel="stylesheet" href="../../Assets/Styles_principal.css">
    <script src="../../Assets/Js/validacion_transaccion.js" defer></script>
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

        <section class="main-menu__layout-general">

            <div class="layout-menu__informacion">
                <h2 class="layout-menu__titulo">Registro de transacciones</h2>
            </div>

            <form action="#ventana-confirmacion-activar" method="get" class="layout-general__contenedor-registro">

                <div class="contenedor-registro__formulario">

                    <div class="campo-formulario">
                        <input type="number" name="valor" class="formulario__input--registro" placeholder="Valor de la transaccion">
                        <span class="mensaje_error campo-error"></span>
                    </div>
                    <div class="formulario__seccion">
                        <div class="campo-formulario">
                            <select name="categoria" id="categoria" class="seccion__caja-eleccion">
                                <option value="" disabled selected>Categoria</option>
                                <option value="Octubre">Octubre</option>
                            </select>
                            <span class="mensaje_error campo-error"></span>
                        </div>
                        <div class="campo-formulario">
                            <input type="text" name="tipo" class="seccion__input--registro" placeholder="Tipo">
                            <span class="mensaje_error campo-error"></span>
                        </div>
                    </div>
                    <div class="campo-formulario">
                        <input type="text" name="fecha" class="formulario__input--registro" placeholder="Fecha de creacion">
                        <span class="mensaje_error campo-error"></span>
                    </div>
                    <div class="campo-formulario">
                        <textarea name="descripcion" id="descripcion" class="formulario__caja-texto" placeholder="Descripcion"></textarea>
                        <span class="mensaje_error campo-error"></span>
                    </div>
                </div>

                <div class="formulario__informacion">

                    <div class="formulario__informacion-titulo">
                        <h2>¿Como registrar una transacción?</h2>
                        <hr class="linea">
                    </div>

                    <ol type="1" start="1" class="formulario__informacion-lista">
                        <li>Ingresa el valor de la transacción.</li>
                        <li>Selecciona la categoria de la transacción.</li>
                        <li>En caso de no tener una categoria puedes crear una en las opciones.</li>
                        <li>Al seleccionar la categoria, el tipo de transacción sera asignado.</li>
                        <li>La fecha de creacion se asigna de forma automatica.</li>
                        <li>Ingresa una breve descripcion de la transacción.</li>
                    </ol>

                    <nav class="formulario__informacion-opciones">
                        <a href="menu_principal.jsp" class="boton--salir">Cancelar</a>
                        <button type="submit" class="boton">Guardar</button>
                    </nav>
                </div>
            </form>

            <div id="ventana-confirmacion-activar" class="ventana-confirmacion">
                <div class="ventana-confirmacion__contenido">
                    <h2 class="ventana-confirmacion__titulo">Registro guardado con exito</h2>
                    <a href="menu_principal.jsp" class="boton">Continuar</a>
                </div>
            </div>

        </section>

    </main>

</body>

</html>