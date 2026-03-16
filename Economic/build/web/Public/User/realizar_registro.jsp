<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="modelo.Categoria"%>
<%@page import="modelo.Usuario"%>
<%
    if (request.getAttribute("categorias") == null) {
        response.sendRedirect(request.getContextPath() + "/TransaccionControlador");
        return;
    }
    Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
    String nombreUsuario = usuarioSesion != null ? usuarioSesion.getNombre() : "";
    String correoUsuario = usuarioSesion != null ? usuarioSesion.getCorreo() : "";
    List<Categoria> categorias = (List<Categoria>) request.getAttribute("categorias");
%>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="context-path" content="${pageContext.request.contextPath}">
    <title>Realizar registro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Assets/Styles_principal.css">
</head>

<body>

    <input type="checkbox" id="sidebar-toggle" class="sidebar__toggle">
    <label for="sidebar-toggle" class="sidebar__overlay"></label>

    <aside class="sidebar">
        <div class="sidebar__contenido">
            <div class="sidebar__header">
                <h2 class="sidebar__logo">ECONOMIC</h2>
                <label for="sidebar-toggle" class="sidebar__cerrar">
                    <i class="bi bi-x-lg"></i>
                </label>
            </div>
            <nav class="sidebar__navegacion">
                <a href="${pageContext.request.contextPath}/Public/User/menu_principal.jsp" class="sidebar__link">
                    <i class="bi bi-house-fill"></i> Inicio
                </a>
                <a href="${pageContext.request.contextPath}/Public/User/historial_transacciones.jsp" class="sidebar__link">
                    <i class="bi bi-clock-history"></i> Historial de transacciones
                </a>
                <a href="${pageContext.request.contextPath}/Public/User/historial_resumenes.jsp" class="sidebar__link">
                    <i class="bi bi-file-text-fill"></i> Historial de resúmenes
                </a>
                <a href="${pageContext.request.contextPath}/CategoriaControlador" class="sidebar__link">
                    <i class="bi bi-gear-fill"></i> Opciones
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
                <a href="${pageContext.request.contextPath}/index.jsp" class="sidebar__salir">
                    <i class="bi bi-box-arrow-right"></i> Cerrar sesión
                </a>
            </div>
        </div>
    </aside>

    <header class="main-header">
        <div class="encabezado">
            <h1 class="encabezado__logo">ECONOMIC</h1>
            <label for="sidebar-toggle" class="hamburguesa">
                <span class="hamburguesa__linea"></span>
                <span class="hamburguesa__linea"></span>
                <span class="hamburguesa__linea"></span>
            </label>
            <nav class="encabezado__navegacion">
                <a href="${pageContext.request.contextPath}/Public/User/menu_principal.jsp" class="encabezado__link">Inicio</a>
                <a href="${pageContext.request.contextPath}/Public/User/historial_transacciones.jsp" class="encabezado__link">Historial de transacciones</a>
                <a href="${pageContext.request.contextPath}/Public/User/historial_resumenes.jsp" class="encabezado__link">Historial de resúmenes</a>
                <a href="${pageContext.request.contextPath}/CategoriaControlador" class="encabezado__link">Opciones</a>
                <a href="#ventana-salida-confirmar" class="encabezado__icono">
                    <i class="bi bi-person-fill"></i>
                </a>
                <div id="ventana-salida-confirmar" class="ventana-salida">
                    <div class="ventana-salida__contenido">
                        <div class="ventana-salida__icono">
                            <div class="encabezado__icono">
                                <i class="bi bi-person-fill"></i>
                            </div>
                            <p><%= nombreUsuario %></p>
                        </div>
                        <div class="ventana-salida__informacion">
                            <p>Email</p>
                            <p><%= correoUsuario %></p>
                        </div>
                        <a href="${pageContext.request.contextPath}/index.jsp" class="ventana-salida__link">
                            <i class="bi bi-box-arrow-right"></i>
                            <p>salir</p>
                        </a>
                    </div>
                </div>
            </nav>
        </div>
    </header>

    <main class="main-menu">
        <section class="main-menu__layout-general">

            <div class="layout-menu__informacion">
                <h2 class="layout-menu__titulo">Registro de transacciones</h2>
            </div>

            <form id="form-transaccion"
                  action="${pageContext.request.contextPath}/TransaccionControlador"
                  method="post"
                  class="layout-general__contenedor-registro">

                <div class="contenedor-registro__formulario">

                    <%-- Campo: Valor --%>
                    <div class="campo-formulario">
                        <input type="number"
                               name="valor"
                               id="input-valor"
                               class="formulario__input--registro"
                               placeholder="Valor de la transacción"
                               min="0.01"
                               step="0.01">
                        <span class="campo-error" id="error-valor"></span>
                    </div>

                    <%-- Campo: Categoría + badge de tipo --%>
                    <div class="formulario__seccion">
                        <div class="campo-formulario">
                            <select name="categoria"
                                    id="select-categoria"
                                    class="seccion__caja-eleccion">
                                <option value="" disabled selected>Categoría</option>
                                <% for (Categoria c : categorias) { %>
                                <option value="<%= c.getIdCategoria() %>"
                                        data-tipo="<%= c.getTipoTransaccion() %>">
                                    <%= c.getNombreCategoria() %>
                                </option>
                                <% } %>
                                <% if (categorias.isEmpty()) { %>
                                <option value="" disabled>No tienes categorías creadas</option>
                                <% } %>
                            </select>
                            <span class="campo-error" id="error-categoria"></span>
                        </div>
                        <span id="tipo-badge" class="tipo-badge"></span>
                    </div>

                    <%-- Campo: Fecha --%>
                    <div class="campo-formulario">
                        <input type="date"
                               name="fecha"
                               id="input-fecha"
                               class="formulario__input--registro">
                        <span class="campo-error" id="error-fecha"></span>
                    </div>

                    <%-- Campo: Descripción --%>
                    <div class="campo-formulario">
                        <textarea name="descripcion"
                                  id="input-descripcion"
                                  class="formulario__caja-texto"
                                  placeholder="Descripción (opcional)"></textarea>
                    </div>

                </div>

                <div class="formulario__informacion">
                    <div class="formulario__informacion-titulo">
                        <h2>¿Cómo registrar una transacción?</h2>
                        <hr class="linea">
                    </div>
                    <ol type="1" class="formulario__informacion-lista">
                        <li>Ingresa el valor de la transacción.</li>
                        <li>Selecciona la categoría — el tipo se asigna automáticamente.</li>
                        <li>Si no tienes categorías, créalas en Opciones.</li>
                        <li>La fecha se asigna al día de hoy automáticamente, pero puedes cambiarla.</li>
                        <li>Ingresa una descripción opcional.</li>
                    </ol>
                    <nav class="formulario__informacion-opciones">
                        <a href="${pageContext.request.contextPath}/Public/User/menu_principal.jsp"
                           class="boton--salir">Cancelar</a>
                        <button type="button" class="boton" onclick="abrirConfirmacion()">Guardar</button>
                    </nav>
                </div>

            </form>

            <%-- ══ VENTANA CONFIRMACIÓN ══ --%>
            <div id="modal-confirmacion" class="ventana-confirmacion-registro">
                <div class="confirmacion__contenido">
                    <div class="confirmacion__header">
                        <div class="confirmacion__header-icono">
                            <i class="bi bi-check-lg"></i>
                        </div>
                        <p>Confirmar transacción</p>
                    </div>
                    <div class="confirmacion__body">
                        <div class="confirmacion__fila">
                            <span class="confirmacion__fila-label">Valor</span>
                            <span class="confirmacion__fila-valor" id="resumen-valor">—</span>
                        </div>
                        <div class="confirmacion__fila">
                            <span class="confirmacion__fila-label">Categoría</span>
                            <span class="confirmacion__fila-valor" id="resumen-categoria">—</span>
                        </div>
                        <div class="confirmacion__fila">
                            <span class="confirmacion__fila-label">Tipo</span>
                            <span class="confirmacion__fila-valor" id="resumen-tipo">—</span>
                        </div>
                        <div class="confirmacion__fila">
                            <span class="confirmacion__fila-label">Fecha</span>
                            <span class="confirmacion__fila-valor" id="resumen-fecha">—</span>
                        </div>
                        <div class="confirmacion__fila" id="fila-descripcion" style="display:none;">
                            <span class="confirmacion__fila-label">Descripción</span>
                            <span class="confirmacion__fila-valor" id="resumen-descripcion">—</span>
                        </div>
                    </div>
                    <div class="confirmacion__footer">
                        <button type="button" class="boton--cancelar" onclick="cerrarConfirmacion()">Cancelar</button>
                        <button type="button" class="boton--guardar" onclick="confirmarGuardar()">Confirmar</button>
                    </div>
                </div>
            </div>

        </section>
    </main>

    <script src="${pageContext.request.contextPath}/Assets/Js/validacion_transaccion.js"></script>

</body>
</html>
