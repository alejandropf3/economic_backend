<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="modelo.Transaccion"%>
<%@page import="modelo.Categoria"%>
<%@page import="modelo.Usuario"%>
<%
    if (request.getAttribute("transacciones") == null) {
        response.sendRedirect(request.getContextPath() + "/HistorialControlador");
        return;
    }
    Usuario usuarioSesion     = (Usuario) session.getAttribute("usuario");
    String nombreUsuario      = usuarioSesion != null ? usuarioSesion.getNombre() : "";
    String correoUsuario      = usuarioSesion != null ? usuarioSesion.getCorreo()  : "";
    String urlImagenPerfil = usuarioSesion != null && usuarioSesion.getUrlImagen() != null
                             ? usuarioSesion.getUrlImagen() : null;

    List<Transaccion> transacciones = (List<Transaccion>) request.getAttribute("transacciones");
    List<Categoria>   categorias    = (List<Categoria>)   request.getAttribute("categorias");
    BigDecimal totalIngresos  = (BigDecimal) request.getAttribute("totalIngresos");
    BigDecimal totalEgresos   = (BigDecimal) request.getAttribute("totalEgresos");
    BigDecimal balance        = (BigDecimal) request.getAttribute("balance");

    String filtroTipo        = (String) request.getAttribute("filtroTipo");
    String filtroCategoria   = (String) request.getAttribute("filtroCategoria");
    String filtroFechaDesde  = (String) request.getAttribute("filtroFechaDesde");
    String filtroFechaHasta  = (String) request.getAttribute("filtroFechaHasta");
%>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="context-path" content="${pageContext.request.contextPath}">
    <title>Historial de transacciones</title>
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
                <a href="${pageContext.request.contextPath}/HistorialControlador" class="sidebar__link sidebar__link--activo">
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
                        <% if (urlImagenPerfil != null) { %>
                            <img src="<%= urlImagenPerfil %>"
                                 alt="Foto de perfil"
                                 style="width:100%; height:100%; border-radius:50%; object-fit:cover;">
                        <% } else { %>
                            <i class="bi bi-person-fill"></i>
                        <% } %>
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
                <a href="${pageContext.request.contextPath}/HistorialControlador" class="encabezado__link">Historial de transacciones</a>
                <a href="${pageContext.request.contextPath}/Public/User/historial_resumenes.jsp" class="encabezado__link">Historial de resúmenes</a>
                <a href="${pageContext.request.contextPath}/CategoriaControlador" class="encabezado__link">Opciones</a>
                <a href="#ventana-salida-confirmar" class="encabezado__icono">
                    <i class="bi bi-person-fill"></i>
                </a>
                <div id="ventana-salida-confirmar" class="ventana-salida">
                    <div class="ventana-salida__contenido">
                        <div class="ventana-salida__icono">
                            <div class="encabezado__icono">
                                <% if (urlImagenPerfil != null) { %>
                                    <img src="<%= urlImagenPerfil %>"
                                         alt="Foto de perfil"
                                         style="width:100%; height:100%; border-radius:50%; object-fit:cover;">
                                <% } else { %>
                                    <i class="bi bi-person-fill"></i>
                                <% } %>
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
                <h2 class="layout-menu__titulo">Historial de Transacciones</h2>
                <div class="contenedor-resumen">
                    <div class="resumen-item">
                        <span class="resumen-label">Total Ingresos</span>
                        <span class="resumen-valor resumen-ingreso">+$<%= String.format("%,.2f", totalIngresos) %></span>
                    </div>
                    <div class="resumen-item">
                        <span class="resumen-label">Total Egresos</span>
                        <span class="resumen-valor resumen-egreso">-$<%= String.format("%,.2f", totalEgresos) %></span>
                    </div>
                    <div class="resumen-item">
                        <span class="resumen-label">Balance</span>
                        <span class="resumen-valor resumen-balance
                            <%= balance.compareTo(BigDecimal.ZERO) >= 0 ? "resumen-ingreso" : "resumen-egreso" %>">
                            $<%= String.format("%,.2f", balance) %>
                        </span>
                    </div>
                </div>
            </div>

            <div class="layout-general__contenedor-transacciones">

                <%-- ══ FILTROS ══ --%>
                <form action="${pageContext.request.contextPath}/HistorialControlador"
                      method="get"
                      class="contenedor-filtros">

                    <div class="filtro-item">
                        <label class="filtro-label">Tipo</label>
                        <select name="tipo" class="filtro-select">
                            <option value="todos"   <%= "todos".equals(filtroTipo)   ? "selected" : "" %>>Todos</option>
                            <option value="ingreso" <%= "ingreso".equals(filtroTipo) ? "selected" : "" %>>Ingresos</option>
                            <option value="egreso"  <%= "egreso".equals(filtroTipo)  ? "selected" : "" %>>Egresos</option>
                        </select>
                    </div>

                    <%--<div class="filtro-item">
                        <label class="filtro-label">Categoría</label>
                        <select name="categoria" class="filtro-select">
                            <option value="todas" <%= "todas".equals(filtroCategoria) || filtroCategoria.isEmpty() ? "selected" : "" %>>Todas</option>
                            <% for (Categoria c : categorias) { %>
                            <option value="<%= c.getIdCategoria() %>"
                                    <%= String.valueOf(c.getIdCategoria()).equals(filtroCategoria) ? "selected" : "" %>>
                                <%= c.getNombreCategoria() %>
                            </option>
                            <% } %>
                        </select>
                    </div> --%>

                    <div class="filtro-item">
                        <label class="filtro-label">Desde</label>
                        <input type="date" name="fechaDesde" class="filtro-input"
                               value="<%= filtroFechaDesde %>">
                    </div>

                    <div class="filtro-item">
                        <label class="filtro-label">Hasta</label>
                        <input type="date" name="fechaHasta" class="filtro-input"
                               value="<%= filtroFechaHasta %>">
                    </div>

                    <button type="submit" class="boton--filtrar">
                        <i class="bi bi-funnel"></i> Filtrar
                    </button>

                    <a href="${pageContext.request.contextPath}/HistorialControlador"
                       class="boton--filtrar" style="text-decoration:none; background: linear-gradient(135deg,#6c757d,#5a6268);">
                        Limpiar
                    </a>

                </form>

                <%-- ══ TABLA ══ --%>
                <div class="contenedor-tabla-wrapper">
                    <table class="contenedor-transacciones__tabla">
                        <thead class="tabla__encabezado">
                            <tr>
                                <th>Tipo</th>
                                <th>Valor</th>
                                <th>Categoría</th>
                                <th>Descripción</th>
                                <th>Fecha</th>
                            </tr>
                        </thead>
                        <tbody class="tabla__cuerpo">
                            <% if (transacciones.isEmpty()) { %>
                            <tr>
                                <td colspan="6" style="text-align:center; padding:30px; color:#666;">
                                    No hay transacciones registradas.
                                </td>
                            </tr>
                            <% } else {
                               for (Transaccion t : transacciones) {
                                   boolean esIngreso = "Ingreso".equals(t.getTipoTransaccion());
                                   String clasesFila = esIngreso ? "tabla__cuerpo-ingreso" : "tabla__cuerpo-egreso";
                                   String tipoTexto  = esIngreso ? "Ingreso +" : "Egreso -";
                                   String valorFormateado = "$" + String.format("%,.2f", t.getValorTransaccion());
                                   String descripcion = t.getDescripcion() != null ? t.getDescripcion() : "—";
                            %>
                            <tr class="<%= clasesFila %>">
                                <td><%= tipoTexto %></td>
                                <td><%= valorFormateado %></td>
                                <td><span class="categoria-badge"><%= t.getNombreCategoria() %></span></td>
                                <td class="descripcion"><%= descripcion %></td>
                                <td class="fecha"><%= t.getFechaRealizacion() %></td>
                                
                            </tr>
                            <% } } %>
                        </tbody>
                    </table>
                </div>

            </div>

        </section>
    </main>

</body>
</html>
