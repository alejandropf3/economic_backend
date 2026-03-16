<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="modelo.Usuario"%>
<%@page import="modelo.Transaccion"%>
<%@page import="java.math.BigDecimal"%>
<%
    if (request.getAttribute("ultimoIngreso") == null && request.getAttribute("ultimoEgreso") == null
        && request.getAttribute("balance") == null) {
        response.sendRedirect(request.getContextPath() + "/MenuControlador");
        return;
    }

    Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
    String nombreUsuario  = usuarioSesion != null ? usuarioSesion.getNombre() : "";
    String correoUsuario  = usuarioSesion != null ? usuarioSesion.getCorreo()  : "";

    Transaccion ultimoIngreso = (Transaccion) request.getAttribute("ultimoIngreso");
    Transaccion ultimoEgreso  = (Transaccion) request.getAttribute("ultimoEgreso");
    BigDecimal  balance       = (BigDecimal)  request.getAttribute("balance");
    if (balance == null) balance = BigDecimal.ZERO;

    // ── Datos último ingreso ──────────────────────────────────────────────────
    String ingresoValor    = ultimoIngreso != null ? "$" + String.format("%,.2f", ultimoIngreso.getValorTransaccion()) : "$0.00";
    String ingresoCateg    = ultimoIngreso != null ? ultimoIngreso.getNombreCategoria() : "Sin categoría";
    String ingresoFecha    = ultimoIngreso != null ? ultimoIngreso.getFechaRealizacion().toString() : "—";

    // ── Datos último egreso ───────────────────────────────────────────────────
    String egresoValor     = ultimoEgreso != null ? "$" + String.format("%,.2f", ultimoEgreso.getValorTransaccion()) : "$0.00";
    String egresoCateg     = ultimoEgreso != null ? ultimoEgreso.getNombreCategoria() : "Sin categoría";
    String egresoFecha     = ultimoEgreso != null ? ultimoEgreso.getFechaRealizacion().toString() : "—";

    // ── Balance con signo ─────────────────────────────────────────────────────
    String balanceTexto    = "$" + String.format("%,.2f", balance.abs());
    String balanceClase    = balance.compareTo(BigDecimal.ZERO) >= 0 ? "balance--positivo" : "balance--negativo";
    String balanceSigNo    = balance.compareTo(BigDecimal.ZERO) >= 0 ? "+" : "-";
%>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="context-path" content="${pageContext.request.contextPath}">
    <title>Menú principal</title>
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
                <a href="${pageContext.request.contextPath}/MenuControlador" class="sidebar__link sidebar__link--activo">
                    <i class="bi bi-house-fill"></i> Inicio
                </a>
                <a href="${pageContext.request.contextPath}/HistorialControlador" class="sidebar__link">
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
                <a href="${pageContext.request.contextPath}/MenuControlador" class="encabezado__link">Inicio</a>
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
        <section class="main-menu__layout-menu">

            <div class="layout-menu__informacion">
                <h2 class="layout-menu__titulo">Dinero actual</h2>
                <p class="layout-menu__texto <%= balanceClase %>">
                    <%= balanceSigNo %><%= balanceTexto %>
                </p>
            </div>

            <a href="${pageContext.request.contextPath}/TransaccionControlador"
               class="boton--realizar-registro">Realizar registro</a>

            <section class="layout-menu__contenedor">

                <%-- ── Últimas transacciones ── --%>
                <div class="contenedor__historial">
                    <h2 class="historial__titulo">Últimas transacciones</h2>

                    <%-- Último ingreso --%>
                    <% if (ultimoIngreso != null) { %>
                    <div class="historial__ingresos">
                        <p class="historial__ingresos-texto">Ingreso +</p>
                        <p class="historial__ingresos-texto"><%= ingresoValor %></p>
                        <p class="historial__ingresos-texto"><%= ingresoCateg %></p>
                        <p class="historial__ingresos-texto"><%= ingresoFecha %></p>
                    </div>
                    <% } else { %>
                    <div class="historial__ingresos">
                        <p class="historial__ingresos-texto">Sin ingresos registrados aún</p>
                    </div>
                    <% } %>

                    <%-- Último egreso --%>
                    <% if (ultimoEgreso != null) { %>
                    <div class="historial__egresos">
                        <p class="historial__egresos-texto">Egreso -</p>
                        <p class="historial__egresos-texto"><%= egresoValor %></p>
                        <p class="historial__egresos-texto"><%= egresoCateg %></p>
                        <p class="historial__egresos-texto"><%= egresoFecha %></p>
                    </div>
                    <% } else { %>
                    <div class="historial__egresos">
                        <p class="historial__egresos-texto">Sin egresos registrados aún</p>
                    </div>
                    <% } %>

                    <a href="${pageContext.request.contextPath}/HistorialControlador"
                       class="boton--ver-mas">Ver más</a>
                </div>

                <%-- ── Historial de resúmenes ── --%>
                <div class="contenedor__resumenes">
                    <h2 class="resumenes__titulo">Historial de resúmenes</h2>
                    <div class="resumenes__elementos">
                        <div class="caja-resumen">
                            <h2 class="caja-resumen__titulo">Resumen semanal</h2>
                            <p class="caja-resumen__fecha">Dias 20/26</p>
                            <a href="" class="caja-resumen__link">Ver tabla</a>
                        </div>
                    </div>
                    <a href="${pageContext.request.contextPath}/Public/User/historial_resumenes.jsp"
                       class="boton--ver-mas">Ver más</a>
                </div>

            </section>
        </section>
    </main>

</body>
</html>
