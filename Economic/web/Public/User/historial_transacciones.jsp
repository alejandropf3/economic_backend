<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="modelo.Usuario"%>
<%-- Recuperar usuario de sesión --%>
<%
    Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
    String nombreUsuario = usuarioSesion != null ? usuarioSesion.getNombre() : "";
    String correoUsuario = usuarioSesion != null ? usuarioSesion.getCorreo() : "";
%>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Historial de transacciones</title>
    <link rel="stylesheet" href="../../Assets/Styles_principal.css">
</head>

<body>

    <!-- Checkbox para controlar el sidebar -->
    <input type="checkbox" id="sidebar-toggle" class="sidebar__toggle">

    <!-- Overlay del sidebar -->
    <label for="sidebar-toggle" class="sidebar__overlay"></label>

    <!-- Sidebar para mÃ³vil -->
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
                <a href="historial_transacciones.jsp" class="sidebar__link sidebar__link--activo">
                    <i class="bi bi-clock-history"></i>
                    Historial de transacciones
                </a>
                <a href="historial_resumenes.jsp" class="sidebar__link">
                    <i class="bi bi-file-text-fill"></i>
                    Historial de resumenes
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
                        <p class="sidebar__usuario-nombre"><%= nombreUsuario %></p>
                        <p class="sidebar__usuario-email"><%= correoUsuario %></p>
                    </div>
                </div>
                <a href="../../index.html" class="sidebar__salir">
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

            <!-- BotÃ³n hamburguesa para mÃ³vil -->
            <label for="sidebar-toggle" class="hamburguesa">
                <span class="hamburguesa__linea"></span>
                <span class="hamburguesa__linea"></span>
                <span class="hamburguesa__linea"></span>
            </label>

            <nav class="encabezado__navegacion">
                <a href="menu_principal.jsp" class="encabezado__link">Inicio</a>
                <a href="historial_transacciones.jsp" class="encabezado__link">Historial de transacciones</a>
                <a href="historial_resumenes.jsp" class="encabezado__link">Historial de resumenes</a>
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
                            <p class="sidebar__usuario-nombre"><%= nombreUsuario %></p>
                        </div>

                        <div class="ventana-salida__informacion">
                            <p>Email</p>
                            <p class="sidebar__usuario-email"><%= correoUsuario %></p>
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

    <!-- Seccion del main -->
    <main class="main-menu">
        <section class="main-menu__layout-general">

            <div class="layout-menu__informacion">
                <h2 class="layout-menu__titulo">Historial de Transacciones</h2>
                <div class="contenedor-resumen">
                    <div class="resumen-item">
                        <span class="resumen-label">Total Ingresos:</span>
                        <span class="resumen-valor resumen-ingreso">+$0.00</span>
                    </div>
                    <div class="resumen-item">
                        <span class="resumen-label">Total Egresos:</span>
                        <span class="resumen-valor resumen-egreso">-$0.00</span>
                    </div>
                    <div class="resumen-item">
                        <span class="resumen-label">Balance:</span>
                        <span class="resumen-valor resumen-balance">$0.00</span>
                    </div>
                </div>
            </div>

            <div class="layout-general__contenedor-transacciones">
                <div class="contenedor-filtros">
                    <div class="filtro-item">
                        <label for="filtro-tipo" class="filtro-label">Tipo:</label>
                        <select id="filtro-tipo" class="filtro-select">
                            <option value="todos">Todos</option>
                            <option value="ingreso">Ingresos</option>
                            <option value="egreso">Egresos</option>
                        </select>
                    </div>
                    <div class="filtro-item">
                        <label for="filtro-categoria" class="filtro-label">Categori­a:</label>
                        <select id="filtro-categoria" class="filtro-select">
                            <option value="todas">Todas</option>
                            <option value="comida">Comida</option>
                            <option value="transporte">Transporte</option>
                            <option value="entretenimiento">Entretenimiento</option>
                        </select>
                    </div>
                    <div class="filtro-item">
                        <label for="filtro-fecha" class="filtro-label">Fecha:</label>
                        <input type="date" id="filtro-fecha" class="filtro-input">
                    </div>
                    <button class="boton--filtrar">Filtrar</button>
                </div>

                <div class="contenedor-tabla-wrapper">
                    <table class="contenedor-transacciones__tabla">

                        <thead class="tabla__encabezado">
                            <tr>
                                <th class="sortable" data-column="tipo">
                                    Tipo <i class="bi bi-arrow-down-up"></i>
                                </th>
                                <th class="sortable" data-column="valor">
                                    Valor <i class="bi bi-arrow-down-up"></i>
                                </th>
                                <th class="sortable" data-column="categoria">
                                    Categori­a <i class="bi bi-arrow-down-up"></i>
                                </th>
                                <th>Descripción</th>
                                <th class="sortable" data-column="fecha">
                                    Fecha <i class="bi bi-arrow-down-up"></i>
                                </th>
                                <th>Acciones</th>
                            </tr>
                        </thead>

                        <tbody class="tabla__cuerpo">
                            <tr class="tabla__cuerpo-ingreso">
                                <td>Ingreso +</td>
                                <td>$1,250.00</td>
                                <td><span class="categoria-badge">Salario</span></td>
                                <td class="descripcion">Pago mensual de salario</td>
                                <td class="fecha">2024-01-15</td>
                                <td class="acciones">
                                    <button class="accion-btn editar-btn" title="Editar">
                                        <i class="bi bi-pencil"></i>
                                    </button>
                                    <button class="accion-btn eliminar-btn" title="Eliminar">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </td>
                            </tr>

                            <tr class="tabla__cuerpo-egreso">
                                <td>Egreso -</td>
                                <td>$450.00</td>
                                <td><span class="categoria-badge">Supermercado</span></td>
                                <td class="descripcion">Compras semanales</td>
                                <td class="fecha">2024-01-14</td>
                                <td class="acciones">
                                    <button class="accion-btn editar-btn" title="Editar">
                                        <i class="bi bi-pencil"></i>
                                    </button>
                                    <button class="accion-btn eliminar-btn" title="Eliminar">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </td>
                            </tr>

                            <tr class="tabla__cuerpo-ingreso">
                                <td>Ingreso +</td>
                                <td>$300.00</td>
                                <td><span class="categoria-badge">Freelance</span></td>
                                <td class="descripcion">Proyecto web</td>
                                <td class="fecha">2024-01-13</td>
                                <td class="acciones">
                                    <button class="accion-btn editar-btn" title="Editar">
                                        <i class="bi bi-pencil"></i>
                                    </button>
                                    <button class="accion-btn eliminar-btn" title="Eliminar">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </td>
                            </tr>

                            <tr class="tabla__cuerpo-egreso">
                                <td>Egreso -</td>
                                <td>$120.00</td>
                                <td><span class="categoria-badge">Transporte</span></td>
                                <td class="descripcion">Gasolina y mantenimiento</td>
                                <td class="fecha">2024-01-12</td>
                                <td class="acciones">
                                    <button class="accion-btn editar-btn" title="Editar">
                                        <i class="bi bi-pencil"></i>
                                    </button>
                                    <button class="accion-btn eliminar-btn" title="Eliminar">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </td>
                            </tr>

                            <tr class="tabla__cuerpo-egreso">
                                <td>Egreso -</td>
                                <td>$85.00</td>
                                <td><span class="categoria-badge">Entretenimiento</span></td>
                                <td class="descripcion">Cine y cena</td>
                                <td class="fecha">2024-01-11</td>
                                <td class="acciones">
                                    <button class="accion-btn editar-btn" title="Editar">
                                        <i class="bi bi-pencil"></i>
                                    </button>
                                    <button class="accion-btn eliminar-btn" title="Eliminar">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </td>
                            </tr>

                            <tr class="tabla__cuerpo-ingreso">
                                <td>Ingreso +</td>
                                <td>$200.00</td>
                                <td><span class="categoria-badge">Venta</span></td>
                                <td class="descripcion">Venta de artí­culos usados</td>
                                <td class="fecha">2024-01-10</td>
                                <td class="acciones">
                                    <button class="accion-btn editar-btn" title="Editar">
                                        <i class="bi bi-pencil"></i>
                                    </button>
                                    <button class="accion-btn eliminar-btn" title="Eliminar">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </td>
                            </tr>

                            <tr class="tabla__cuerpo-egreso">
                                <td>Egreso -</td>
                                <td>$60.00</td>
                                <td><span class="categoria-badge">Servicios</span></td>
                                <td class="descripcion">Internet y teléfono</td>
                                <td class="fecha">2024-01-09</td>
                                <td class="acciones">
                                    <button class="accion-btn editar-btn" title="Editar">
                                        <i class="bi bi-pencil"></i>
                                    </button>
                                    <button class="accion-btn eliminar-btn" title="Eliminar">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </td>
                            </tr>
                        </tbody>

                    </table>
                </div>

                <div class="contenedor-paginacion">
                    <button class="paginacion-btn" disabled>
                        <i class="bi bi-chevron-left"></i>
                        Anterior
                    </button>
                    <span class="paginacion-info">Página 1 de 1</span>
                    <button class="paginacion-btn">
                        Siguiente
                        <i class="bi bi-chevron-right"></i>
                    </button>
                </div>

            </div>

        </section>
    </main>

</body>

</html>