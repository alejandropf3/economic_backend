<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="modelo.Categoria"%>
<%@page import="modelo.Usuario"%>

<%-- PASO 1: Recuperar usuario y resImagen PRIMERO --%>
<%
    Usuario usuarioSesion  = (Usuario) session.getAttribute("usuario");
    String nombreUsuario   = usuarioSesion != null ? usuarioSesion.getNombre() : "";
    String correoUsuario   = usuarioSesion != null ? usuarioSesion.getCorreo()  : "";
    String urlImagenPerfil = usuarioSesion != null && usuarioSesion.getUrlImagen() != null
                             ? usuarioSesion.getUrlImagen() : null;
    String resImagen       = request.getParameter("resImagen");
%>

<%-- PASO 2: Redirigir si no viene del controlador --%>
<%
    if (request.getAttribute("categorias") == null) {
        response.sendRedirect(request.getContextPath() + "/CategoriaControlador");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="context-path" content="${pageContext.request.contextPath}">
    <title>Configuracion</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Assets/Styles_principal.css">
    <style>
        .formulario__span-error {
            color: #c0392b;
            font-size: 12px;
            font-weight: 500;
            margin-top: 4px;
            display: none;
        }
        .input--error {
            border-color: #c0392b !important;
            box-shadow: 0 0 0 3px rgba(192, 57, 43, 0.12) !important;
        }
    </style>
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
                <a href="${pageContext.request.contextPath}/MenuControlador" class="sidebar__link">
                    <i class="bi bi-house-fill"></i>
                    Inicio
                </a>
                <a href="${pageContext.request.contextPath}/HistorialControlador" class="sidebar__link">
                    <i class="bi bi-clock-history"></i>
                    Historial de transacciones
                </a>
                <a href="${pageContext.request.contextPath}/Public/User/historial_resumenes.jsp" class="sidebar__link">
                    <i class="bi bi-file-text-fill"></i>
                    Historial de resúmenes
                </a>
                <a href="${pageContext.request.contextPath}/CategoriaControlador" class="sidebar__link sidebar__link--activo">
                    <i class="bi bi-gear-fill"></i>
                    Opciones
                </a>
                <a href="${pageContext.request.contextPath}/Public/Admin/administrar_usuarios.jsp" class="sidebar__link">
                    <i class="bi bi-people-fill"></i>
                    Admin
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
                <a href="${pageContext.request.contextPath}/Public/Admin/administrar_usuarios.jsp" class="encabezado__link">Admin</a>

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
                               value="<%= nombreUsuario %>" placeholder="Nombre de usuario" readonly>
                    </div>
                    <div class="formulario__campos-usuario">
                        <label for="contraseña_usuario" class="formulario__campos-label">Contraseña</label>
                        <input type="password" id="contraseña_usuario" class="formulario__campos-input"
                               value="••••••••" placeholder="Contra123" readonly>
                    </div>
                    <div class="formulario__campos-usuario">
                        <label for="correo_usuario" class="formulario__campos-label">Correo electronico</label>
                        <input type="email" id="correo_usuario" class="formulario__campos-input"
                               value="<%= correoUsuario %>" placeholder="Cor****@gmail.com" readonly>
                    </div>
                    <div class="formulario__campos-usuario">
                        <label for="respado_correo_usuario" class="formulario__campos-label">Respaldo correo electronico</label>
                        <input type="email" id="respado_correo_usuario" class="formulario__campos-input"
                               placeholder="Res****@gmail.com" readonly>
                    </div>
                    <div class="formulario__campos-usuario">
                        <label for="dinero_diponible" class="formulario__campos-label">Dinero disponible</label>
                        <input type="number" id="dinero_diponible" class="formulario__campos-input"
                               placeholder="$0.00" readonly>
                    </div>
                </form>
            </div>

            <div class="main-menu__contenedor-derecha">

                <%-- ══ FORMULARIO IMAGEN DE PERFIL ══ --%>
                <form action="${pageContext.request.contextPath}/ImagenControlador"
                      method="post"
                      enctype="multipart/form-data"
                      class="contenedor-derecha__formulario">

                    <%-- Imagen de perfil: foto si existe, SVG por defecto si no --%>
                    <div class="formulario__icono">
                        <% if (urlImagenPerfil != null) { %>
                            <img src="<%= urlImagenPerfil %>"
                                 alt="Foto de perfil"
                                 style="width:180px; height:180px; border-radius:50%;
                                        object-fit:cover; border:3px solid var(--color_terceario);">
                        <% } else { %>
                            <svg xmlns="http://www.w3.org/2000/svg" width="180px" height="180px"
                                 fill="currentColor" class="bi bi-person-fill" viewBox="0 0 16 16">
                                <path d="M3 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6"/>
                            </svg>
                        <% } %>
                    </div>

                    <%-- Mensajes de resultado --%>
                    <% if ("ok".equals(resImagen)) { %>
                        <p style="color:var(--color_ingresos); font-size:13px; font-weight:600; text-align:center;">
                            ✓ Foto actualizada correctamente.
                        </p>
                    <% } else if (resImagen != null && !resImagen.isEmpty()) {
                        String mensajeErrorImg = "";
                        switch (resImagen) {
                            case "vacio":            mensajeErrorImg = "Debes seleccionar una imagen."; break;
                            case "formato_invalido": mensajeErrorImg = "Solo se permiten JPG, PNG o WEBP."; break;
                            case "error":            mensajeErrorImg = "Error al guardar. Intenta de nuevo."; break;
                        }
                    %>
                        <p style="color:var(--color_egresos); font-size:13px; font-weight:600; text-align:center;">
                            <%= mensajeErrorImg %>
                        </p>
                    <% } %>

                    <div class="formulario__campo-imagen">
                        <label for="foto_usuario" class="formulario__imagen-label">Cambiar foto</label>
                        <input type="file"
                               name="foto_usuario"
                               id="foto_usuario"
                               class="formulario__imagen-input"
                               accept="image/jpeg, image/png, image/webp">
                        <p style="font-size:11px; color:rgba(0,35,73,0.45); margin-top:4px; text-align:center;">
                            JPG, PNG o WEBP · Máximo 2 MB
                        </p>
                        <button type="submit" class="boton--guardar" style="margin-top:10px; width:100%;">
                            Guardar foto
                        </button>
                    </div>

                </form>

                <!-- Caja de categorías -->
                <div class="contenedor-derecha__caja-categorias">
                    <h2 class="caja-categorias__titulo">Categorias</h2>
                    <div class="caja-categorias__contenido">
                        <%-- Las categorías se renderizan por JS con el array inyectado abajo --%>
                    </div>
                    <a href="#ventana-crear-categoria-confirmar" class="boton--crear-categoria">Crear categoria +</a>
                </div>
            </div>

            <!-- ══ MODAL: CREAR CATEGORÍA ══ -->
            <div id="ventana-crear-categoria-confirmar" class="ventana-crear-categoria">
                <form action="${pageContext.request.contextPath}/CategoriaControlador"
                      method="post"
                      class="ventana-crear-categoria__formulario-crear">

                    <input type="hidden" name="accion" value="crear">

                    <h2 class="formulario-crear__titulo">Crear categoria</h2>

                    <div class="formulario-crear__contenido">
                        <input type="text"
                               name="txtNombreCategoria"
                               placeholder="Nombre categoria"
                               class="contenido__input">
                        <span id="error-crear-nombre" class="formulario__span-error" style="display:none;"></span>

                        <select name="txtTipoCategoria" class="contenido__input">
                            <option value="" disabled selected>Tipo</option>
                            <option value="Ingreso">Ingreso +</option>
                            <option value="Egreso">Egreso -</option>
                        </select>
                        <span id="error-crear-tipo" class="formulario__span-error" style="display:none;"></span>
                    </div>

                    <nav class="formulario-crear__navegacion">
                        <a href="#ventana-crear-categoria-cerrar" class="boton--cancelar">Cancelar</a>
                        <button type="submit" class="boton--guardar">Guardar</button>
                    </nav>
                </form>
            </div>

            <!-- ══ MODAL: EDITAR CATEGORÍA ══ -->
            <div id="ventana-modificar-categoria-confirmar" class="ventana-modificar-categoria">
                <form action="${pageContext.request.contextPath}/CategoriaControlador"
                      method="post"
                      class="ventana-modificar-categoria__formulario-editar">

                    <input type="hidden" name="accion" value="editar">
                    <input type="hidden" name="txtIdCategoria" id="inputIdEditar">

                    <h2 class="formulario-editar__titulo">Editar categoria</h2>

                    <div class="formulario-editar__contenido">
                        <input type="text"
                               name="txtNombreCategoria"
                               placeholder="Nuevo nombre categoria"
                               class="contenido__input">
                        <span id="error-editar-nombre" class="formulario__span-error" style="display:none;"></span>

                        <select name="txtTipoCategoria" class="contenido__input">
                            <option value="" disabled selected>Nuevo tipo</option>
                            <option value="Ingreso">Ingreso +</option>
                            <option value="Egreso">Egreso -</option>
                        </select>
                        <span id="error-editar-tipo" class="formulario__span-error" style="display:none;"></span>
                    </div>

                    <nav class="formulario-editar__navegacion">
                        <a href="#ventana-modificar-categoria-cerrar" class="boton--cancelar">Cancelar</a>
                        <button type="submit" class="boton--guardar">Guardar</button>
                    </nav>
                </form>
            </div>

        </section>
    </main>

    <%-- ── Inyectamos las categorías como JSON para el JS ── --%>
    <%
        List<Categoria> categorias = (List<Categoria>) request.getAttribute("categorias");
        if (categorias == null) categorias = new java.util.ArrayList<>();
    %>
    <script>
        const categoriasData = [
            <% for (int i = 0; i < categorias.size(); i++) {
                Categoria c = categorias.get(i); %>
            {
                idCategoria:      <%= c.getIdCategoria() %>,
                nombreCategoria:  "<%= c.getNombreCategoria().replace("\"", "\\\"") %>",
                tipoTransaccion:  "<%= c.getTipoTransaccion() %>"
            }<%= i < categorias.size() - 1 ? "," : "" %>
            <% } %>
        ];
    </script>

    <script src="${pageContext.request.contextPath}/Assets/Js/categorias_dinamicas.js"></script>
    <script src="${pageContext.request.contextPath}/Assets/Js/validacion_categoria.js"></script>

</body>
</html>
