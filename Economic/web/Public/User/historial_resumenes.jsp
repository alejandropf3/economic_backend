<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="modelo.Usuario"%>
<%@page import="modelo.ResumenSemanal"%>
<%@page import="modelo.ResumenMensual"%>
<%@page import="modelo.ResumenAnual"%>
<%@page import="modelo.ResumenDiario"%>
<%@page import="java.util.List"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.time.LocalDate"%>
<%
    if (request.getAttribute("vista") == null) {
        response.sendRedirect(request.getContextPath() + "/ResumenControlador");
        return;
    }

    Usuario usuarioSesion  = (Usuario) session.getAttribute("usuario");
    String nombreUsuario   = usuarioSesion != null ? usuarioSesion.getNombre() : "";
    String correoUsuario   = usuarioSesion != null ? usuarioSesion.getCorreo()  : "";
    String urlImagenPerfil = usuarioSesion != null && usuarioSesion.getUrlImagen() != null
                             ? usuarioSesion.getUrlImagen() : null;

    String vista             = (String)  request.getAttribute("vista");
    int    anio              = (Integer) request.getAttribute("anio");
    List<Integer> aniosDisp  = (List<Integer>) request.getAttribute("aniosDisponibles");

    ResumenSemanal  resumenSemanal  = (ResumenSemanal)  request.getAttribute("resumenSemanal");
    ResumenMensual  resumenMensual  = (ResumenMensual)  request.getAttribute("resumenMensual");
    ResumenAnual    resumenAnual    = (ResumenAnual)     request.getAttribute("resumenAnual");

    String fechaSeleccionada = (String)  request.getAttribute("fechaSeleccionada");
    Integer mesSeleccionado  = (Integer) request.getAttribute("mesSeleccionado");

    DateTimeFormatter fmtCorto = DateTimeFormatter.ofPattern("dd/MM");
    DateTimeFormatter fmtFull  = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    String[] nombresMeses = {"","Enero","Febrero","Marzo","Abril","Mayo","Junio",
                              "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
%>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="context-path" content="${pageContext.request.contextPath}">
    <title>Historial de resúmenes</title>
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
                <a href="${pageContext.request.contextPath}/MenuControlador" class="sidebar__link">
                    <i class="bi bi-house-fill"></i> Inicio
                </a>
                <a href="${pageContext.request.contextPath}/HistorialControlador" class="sidebar__link">
                    <i class="bi bi-clock-history"></i> Historial de transacciones
                </a>
                <a href="${pageContext.request.contextPath}/ResumenControlador" class="sidebar__link sidebar__link--activo">
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
                            <img src="<%= urlImagenPerfil %>" alt="Foto de perfil"
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
                <a href="${pageContext.request.contextPath}/MenuControlador" class="encabezado__link">Inicio</a>
                <a href="${pageContext.request.contextPath}/HistorialControlador" class="encabezado__link">Historial de transacciones</a>
                <a href="${pageContext.request.contextPath}/ResumenControlador" class="encabezado__link">Historial de resúmenes</a>
                <a href="${pageContext.request.contextPath}/CategoriaControlador" class="encabezado__link">Opciones</a>
                <a href="#ventana-salida-confirmar" class="encabezado__icono">
                    <i class="bi bi-person-fill"></i>
                </a>
                <div id="ventana-salida-confirmar" class="ventana-salida">
                    <div class="ventana-salida__contenido">
                        <div class="ventana-salida__icono">
                            <div class="encabezado__icono">
                                <% if (urlImagenPerfil != null) { %>
                                    <img src="<%= urlImagenPerfil %>" alt="Foto de perfil"
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
        <section class="main-menu__layout-historial-resumenes">

            <div class="layout-menu__informacion">
                <h2 class="layout-menu__titulo">Historial de Resúmenes</h2>
            </div>

            <%-- ══ CONTENEDOR IZQUIERDA — navegación y selector ══ --%>
            <div class="layout-menu__contenedor-historial">

                <%-- Selector de tipo de vista --%>
                <div class="contenedor-historial__encabezado">
                    <p class="contenedor-historial__encabezado-texto">Ver por:</p>
                    <a href="${pageContext.request.contextPath}/ResumenControlador?vista=semanal"
                       class="boton--filtrar <%= "semanal".equals(vista) ? "" : "" %>"
                       style="<%= "semanal".equals(vista) ? "background:linear-gradient(135deg,var(--color_principal),#003977);" : "" %>">
                        Semanal
                    </a>
                    <a href="${pageContext.request.contextPath}/ResumenControlador?vista=mensual&anio=<%= anio %>"
                       class="boton--filtrar"
                       style="<%= "mensual".equals(vista) ? "background:linear-gradient(135deg,var(--color_principal),#003977);" : "" %>">
                        Mensual
                    </a>
                    <a href="${pageContext.request.contextPath}/ResumenControlador?vista=anual&anio=<%= anio %>"
                       class="boton--filtrar"
                       style="<%= "anual".equals(vista) ? "background:linear-gradient(135deg,var(--color_principal),#003977);" : "" %>">
                        Anual
                    </a>
                </div>

                <%-- Selector de fecha/mes/año según la vista --%>
                <form action="${pageContext.request.contextPath}/ResumenControlador" method="get"
                      class="contenedor-historial__selector">

                    <input type="hidden" name="vista" value="<%= vista %>">

                    <% if ("semanal".equals(vista)) { %>
                        <p class="contenedor-historial__encabezado-texto">Seleccionar fecha:</p>
                        <input type="date" name="fecha"
                               value="<%= fechaSeleccionada != null ? fechaSeleccionada : LocalDate.now().toString() %>"
                               class="contenedor-historial__selector-date">

                    <% } else if ("mensual".equals(vista)) { %>
                        <p class="contenedor-historial__encabezado-texto">Mes:</p>
                        <select name="mes" class="contenedor-historial__selector-seleccion">
                            <% for (int m = 1; m <= 12; m++) { %>
                            <option value="<%= m %>" <%= (mesSeleccionado != null && mesSeleccionado == m) ? "selected" : "" %>>
                                <%= nombresMeses[m] %>
                            </option>
                            <% } %>
                        </select>
                        <select name="anio" class="contenedor-historial__selector-seleccion">
                            <% for (Integer a : aniosDisp) { %>
                            <option value="<%= a %>" <%= (a == anio) ? "selected" : "" %>><%= a %></option>
                            <% } %>
                        </select>

                    <% } else { %>
                        <p class="contenedor-historial__encabezado-texto">Año:</p>
                        <select name="anio" class="contenedor-historial__selector-seleccion">
                            <% for (Integer a : aniosDisp) { %>
                            <option value="<%= a %>" <%= (a == anio) ? "selected" : "" %>><%= a %></option>
                            <% } %>
                        </select>
                    <% } %>

                    <button type="submit" class="boton--filtrar">
                        <i class="bi bi-search"></i> Buscar
                    </button>
                </form>

                <%-- Lista de resúmenes según la vista --%>
                <div class="contenedor-historial__alinear">

                    <% if ("semanal".equals(vista) && resumenSemanal != null) { %>
                        <div class="caja-resumen">
                            <h2 class="caja-resumen__titulo">Resumen semanal</h2>
                            <p class="caja-resumen__fecha">
                                <%= resumenSemanal.getFechaInicio().format(fmtCorto) %> /
                                <%= resumenSemanal.getFechaFin().format(fmtCorto) %>
                            </p>
                            <p style="font-size:13px; color:var(--color_ingresos); font-weight:600;">
                                +$<%= String.format("%,.2f", resumenSemanal.getTotalIngresos()) %>
                            </p>
                            <p style="font-size:13px; color:var(--color_egresos); font-weight:600;">
                                -$<%= String.format("%,.2f", resumenSemanal.getTotalEgresos()) %>
                            </p>
                        </div>

                    <% } else if ("mensual".equals(vista) && resumenMensual != null) { %>
                        <% for (ResumenSemanal sem : resumenMensual.getSemanas()) { %>
                        <div class="caja-resumen">
                            <h2 class="caja-resumen__titulo">Semana</h2>
                            <p class="caja-resumen__fecha">
                                <%= sem.getFechaInicio().format(fmtCorto) %> /
                                <%= sem.getFechaFin().format(fmtCorto) %>
                            </p>
                            <p style="font-size:13px; color:var(--color_ingresos); font-weight:600;">
                                +$<%= String.format("%,.2f", sem.getTotalIngresos()) %>
                            </p>
                            <p style="font-size:13px; color:var(--color_egresos); font-weight:600;">
                                -$<%= String.format("%,.2f", sem.getTotalEgresos()) %>
                            </p>
                        </div>
                        <% } %>

                    <% } else if ("anual".equals(vista) && resumenAnual != null) { %>
                        <% for (ResumenMensual mes2 : resumenAnual.getMeses()) { %>
                        <div class="caja-resumen">
                            <h2 class="caja-resumen__titulo"><%= mes2.getNombreMes() %></h2>
                            <p class="caja-resumen__fecha"><%= mes2.getAnio() %></p>
                            <p style="font-size:13px; color:var(--color_ingresos); font-weight:600;">
                                +$<%= String.format("%,.2f", mes2.getTotalIngresos()) %>
                            </p>
                            <p style="font-size:13px; color:var(--color_egresos); font-weight:600;">
                                -$<%= String.format("%,.2f", mes2.getTotalEgresos()) %>
                            </p>
                        </div>
                        <% } %>
                    <% } %>

                </div>
            </div>

            <%-- ══ CONTENEDOR DERECHA — tabla de detalle ══ --%>
            <div class="layout-menu__contenedor-vista-previa">

                <% if ("semanal".equals(vista) && resumenSemanal != null) { %>

                    <h2 class="contenedor-vista-previa__titulo">Resumen semanal</h2>
                    <div class="contenedor-vista-previa__encabezado">
                        <p class="contenedor-vista-previa__encabezado-texto">
                            Días: <%= resumenSemanal.getFechaInicio().format(fmtCorto) %> /
                                  <%= resumenSemanal.getFechaFin().format(fmtCorto) %>
                        </p>
                        <p class="contenedor-vista-previa__encabezado-texto">
                            Mes: <%= nombresMeses[resumenSemanal.getFechaInicio().getMonthValue()] %>
                        </p>
                        <p class="contenedor-vista-previa__encabezado-texto">
                            Año: <%= resumenSemanal.getFechaInicio().getYear() %>
                        </p>
                    </div>

                    <table class="contenido__transacciones-table">
                        <thead>
                            <tr>
                                <th>Fecha</th>
                                <th>Ingresos</th>
                                <th>Egresos</th>
                                <th>Balance del día</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (resumenSemanal.getDiasResumen().isEmpty()) { %>
                            <tr>
                                <td colspan="4" style="text-align:center; padding:20px; color:#666;">
                                    Sin transacciones esta semana.
                                </td>
                            </tr>
                            <% } else {
                               for (ResumenDiario dia : resumenSemanal.getDiasResumen()) {
                                   boolean positivo = dia.getBalanceDia().compareTo(BigDecimal.ZERO) >= 0;
                            %>
                            <tr>
                                <td><%= dia.getFecha().format(fmtFull) %></td>
                                <td class="transacciones-table__ingreso">
                                    +$<%= String.format("%,.2f", dia.getTotalIngresos()) %>
                                </td>
                                <td class="transacciones-table__egreso">
                                    -$<%= String.format("%,.2f", dia.getTotalEgresos()) %>
                                </td>
                                <td class="<%= positivo ? "transacciones-table__ingreso" : "transacciones-table__egreso" %>">
                                    $<%= String.format("%,.2f", dia.getBalanceDia().abs()) %>
                                </td>
                            </tr>
                            <% } } %>
                        </tbody>
                    </table>

                    <div class="contenido-total-semanal">
                        <h2 class="contenido-total-semanal__titulo">
                            Balance semanal =
                            $<%= String.format("%,.2f", resumenSemanal.getBalance().abs()) %>
                            <%= resumenSemanal.getBalance().compareTo(BigDecimal.ZERO) >= 0 ? "(positivo)" : "(negativo)" %>
                        </h2>
                    </div>

                <% } else if ("mensual".equals(vista) && resumenMensual != null) { %>

                    <h2 class="contenedor-vista-previa__titulo">
                        <%= resumenMensual.getNombreMes() %> <%= resumenMensual.getAnio() %>
                    </h2>
                    <div class="contenedor-vista-previa__encabezado">
                        <p class="contenedor-vista-previa__encabezado-texto">Mes: <%= resumenMensual.getNombreMes() %></p>
                        <p class="contenedor-vista-previa__encabezado-texto">Año: <%= resumenMensual.getAnio() %></p>
                    </div>

                    <table class="contenido__transacciones-table">
                        <thead>
                            <tr>
                                <th>Semana</th>
                                <th>Ingresos</th>
                                <th>Egresos</th>
                                <th>Balance</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (ResumenSemanal sem : resumenMensual.getSemanas()) {
                                boolean pos = sem.getBalance().compareTo(BigDecimal.ZERO) >= 0; %>
                            <tr>
                                <td>
                                    <%= sem.getFechaInicio().format(fmtCorto) %> /
                                    <%= sem.getFechaFin().format(fmtCorto) %>
                                </td>
                                <td class="transacciones-table__ingreso">
                                    +$<%= String.format("%,.2f", sem.getTotalIngresos()) %>
                                </td>
                                <td class="transacciones-table__egreso">
                                    -$<%= String.format("%,.2f", sem.getTotalEgresos()) %>
                                </td>
                                <td class="<%= pos ? "transacciones-table__ingreso" : "transacciones-table__egreso" %>">
                                    $<%= String.format("%,.2f", sem.getBalance().abs()) %>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>

                    <div class="contenido-total-semanal">
                        <h2 class="contenido-total-semanal__titulo">
                            Balance mensual =
                            $<%= String.format("%,.2f", resumenMensual.getBalance().abs()) %>
                            <%= resumenMensual.getBalance().compareTo(BigDecimal.ZERO) >= 0 ? "(positivo)" : "(negativo)" %>
                        </h2>
                    </div>

                <% } else if ("anual".equals(vista) && resumenAnual != null) { %>

                    <h2 class="contenedor-vista-previa__titulo">Año <%= resumenAnual.getAnio() %></h2>
                    <div class="contenedor-vista-previa__encabezado">
                        <p class="contenedor-vista-previa__encabezado-texto">Año: <%= resumenAnual.getAnio() %></p>
                    </div>

                    <table class="contenido__transacciones-table">
                        <thead>
                            <tr>
                                <th>Mes</th>
                                <th>Ingresos</th>
                                <th>Egresos</th>
                                <th>Balance</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (ResumenMensual mes2 : resumenAnual.getMeses()) {
                                boolean pos = mes2.getBalance().compareTo(BigDecimal.ZERO) >= 0; %>
                            <tr>
                                <td><%= mes2.getNombreMes() %></td>
                                <td class="transacciones-table__ingreso">
                                    +$<%= String.format("%,.2f", mes2.getTotalIngresos()) %>
                                </td>
                                <td class="transacciones-table__egreso">
                                    -$<%= String.format("%,.2f", mes2.getTotalEgresos()) %>
                                </td>
                                <td class="<%= pos ? "transacciones-table__ingreso" : "transacciones-table__egreso" %>">
                                    $<%= String.format("%,.2f", mes2.getBalance().abs()) %>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>

                    <div class="contenido-total-semanal">
                        <h2 class="contenido-total-semanal__titulo">
                            Balance anual =
                            $<%= String.format("%,.2f", resumenAnual.getBalance().abs()) %>
                            <%= resumenAnual.getBalance().compareTo(BigDecimal.ZERO) >= 0 ? "(positivo)" : "(negativo)" %>
                        </h2>
                    </div>

                <% } %>

            </div>

        </section>
    </main>

</body>
</html>
