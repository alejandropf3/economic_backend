<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="modelo.Categoria"%>
<%@page import="modelo.Usuario"%>
<%
    Usuario usuarioSesion  = (Usuario) session.getAttribute("usuario");
    String nombreUsuario   = usuarioSesion != null ? usuarioSesion.getNombre()         : "";
    String correoUsuario   = usuarioSesion != null ? usuarioSesion.getCorreo()          : "";
    String correoRespaldo  = usuarioSesion != null && usuarioSesion.getCorreoRespaldo() != null
                             ? usuarioSesion.getCorreoRespaldo() : "";
    String urlImagenPerfil = usuarioSesion != null && usuarioSesion.getUrlImagen() != null
                             ? usuarioSesion.getUrlImagen() : null;
    String resImagen       = request.getParameter("resImagen");
    String resEdicion      = request.getParameter("resEdicion");
    String resCategoria    = request.getParameter("res");
    boolean categoriaExitosa = "exitosa".equals(request.getAttribute("categoriaExitosa"));

    // Mensaje error categoría
    String msgErrorCategoria = "";
    if ("categoria_en_uso".equals(resCategoria)) {
        msgErrorCategoria = "No puedes eliminar esta categoría porque está siendo utilizada en una o más transacciones.";
    } else if ("error_eliminar".equals(resCategoria)) {
        msgErrorCategoria = "Ocurrió un error al eliminar. Intenta de nuevo.";
    }

    // Mensaje edición perfil
    String msgEdicion = "";
    if (resEdicion != null && !resEdicion.isEmpty() && !"ok".equals(resEdicion)) {
        switch (resEdicion) {
            case "nombre_vacio":              msgEdicion = "El nombre es obligatorio."; break;
            case "nombre_muy_corto":          msgEdicion = "El nombre debe tener al menos 3 caracteres."; break;
            case "nombre_muy_largo":          msgEdicion = "El nombre es demasiado largo."; break;
            case "correo_vacio":              msgEdicion = "El correo electrónico es obligatorio."; break;
            case "correo_formato_invalido":   msgEdicion = "El formato del correo no es válido."; break;
            case "correo_duplicado":          msgEdicion = "Ese correo ya está registrado por otro usuario."; break;
            case "respaldo_formato_invalido": msgEdicion = "El formato del correo de respaldo no es válido."; break;
            case "respaldo_igual_principal":  msgEdicion = "El correo de respaldo no puede ser igual al principal."; break;
            case "respaldo_duplicado":        msgEdicion = "Ese correo de respaldo ya está en uso."; break;
            default:                          msgEdicion = "Ocurrió un error. Intenta de nuevo."; break;
        }
    }

    // Mensaje imagen
    String msgImg = "";
    if (resImagen != null && !resImagen.isEmpty() && !"ok".equals(resImagen)) {
        switch (resImagen) {
            case "vacio":            msgImg = "Debes seleccionar una imagen."; break;
            case "formato_invalido": msgImg = "Solo se permiten JPG, PNG o WEBP."; break;
            default:                 msgImg = "Error al guardar. Intenta de nuevo."; break;
        }
    }
%>
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
                <a href="${pageContext.request.contextPath}/Public/User/historial_resumenes.jsp" class="sidebar__link">
                    <i class="bi bi-file-text-fill"></i> Historial de resumenes
                </a>
                <a href="${pageContext.request.contextPath}/CategoriaControlador" class="sidebar__link sidebar__link--activo">
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
                    <i class="bi bi-box-arrow-right"></i> Cerrar sesion
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
                <a href="${pageContext.request.contextPath}/Public/User/historial_resumenes.jsp" class="encabezado__link">Historial de resumenes</a>
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
        <section class="main-menu__layout-general">

            <div class="layout-menu__informacion">
                <h2 class="layout-menu__titulo">Configuracion de usuario</h2>
            </div>

            <%-- FORMULARIO IZQUIERDA --%>
            <div class="main-menu__contenedor-izquierda">

                <% if ("ok".equals(resEdicion)) { %>
                    <p class="formulario__mensaje-ok">Datos actualizados correctamente.</p>
                <% } else if (!msgEdicion.isEmpty()) { %>
                    <p class="formulario__span-error" style="display:block; font-size:13px;"><%= msgEdicion %></p>
                <% } %>

                <form action="${pageContext.request.contextPath}/UsuarioControlador"
                      method="post"
                      class="contenedor-izquierda__formulario"
                      id="form-editar-perfil">

                    <input type="hidden" name="accion" value="editarPerfil">

                    <div class="formulario__campos-usuario">
                        <label for="nombre_usuario" class="formulario__campos-label">Nombre</label>
                        <input type="text" id="nombre_usuario" name="txtNombre"
                               class="formulario__campos-input"
                               value="<%= nombreUsuario %>"
                               placeholder="Nombre de usuario" readonly>
                    </div>

                    <div class="formulario__campos-usuario">
                        <label for="contrasena_usuario" class="formulario__campos-label">Contrasena</label>
                        <input type="password" id="contrasena_usuario"
                               class="formulario__campos-input"
                               value="12345678" readonly disabled>
                    </div>

                    <div class="formulario__campos-usuario">
                        <label for="correo_usuario" class="formulario__campos-label">Correo electronico</label>
                        <input type="email" id="correo_usuario" name="txtCorreo"
                               class="formulario__campos-input"
                               value="<%= correoUsuario %>"
                               placeholder="Cor****@gmail.com" readonly>
                    </div>

                    <div class="formulario__campos-usuario">
                        <label for="respaldo_correo_usuario" class="formulario__campos-label">Respaldo correo</label>
                        <input type="email" id="respaldo_correo_usuario" name="txtCorreoRespaldo"
                               class="formulario__campos-input"
                               value="<%= correoRespaldo %>"
                               placeholder="Res****@gmail.com" readonly>
                    </div>

                    <button type="button" class="boton--editar-perfil"
                            id="btn-editar-perfil" onclick="activarEdicion()">
                        Editar datos
                    </button>

                    <div class="formulario__botones-edicion"
                         id="botones-guardar-cancelar" style="display:none;">
                        <button type="button" class="boton--cancelar" onclick="cancelarEdicion()">Cancelar</button>
                        <button type="submit" class="boton--guardar">Guardar</button>
                    </div>

                </form>
            </div>

            <%-- CONTENEDOR DERECHA --%>
            <div class="main-menu__contenedor-derecha">

                <form action="${pageContext.request.contextPath}/ImagenControlador"
                      method="post" enctype="multipart/form-data"
                      class="contenedor-derecha__formulario">

                    <div class="formulario__icono">
                        <% if (urlImagenPerfil != null) { %>
                            <img src="<%= urlImagenPerfil %>" alt="Foto de perfil"
                                 style="width:180px; height:180px; border-radius:50%;
                                        object-fit:cover; border:3px solid var(--color_terceario);">
                        <% } else { %>
                            <svg xmlns="http://www.w3.org/2000/svg" width="180px" height="180px"
                                 fill="currentColor" class="bi bi-person-fill" viewBox="0 0 16 16">
                                <path d="M3 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6"/>
                            </svg>
                        <% } %>
                    </div>

                    <% if ("ok".equals(resImagen)) { %>
                        <p class="formulario__mensaje-imagen formulario__mensaje-imagen--ok">Foto actualizada correctamente.</p>
                    <% } else if (!msgImg.isEmpty()) { %>
                        <p class="formulario__mensaje-imagen formulario__mensaje-imagen--error"><%= msgImg %></p>
                    <% } %>

                    <div class="formulario__campo-imagen">
                        <label for="foto_usuario" class="formulario__imagen-label">Cambiar foto</label>
                        <input type="file" name="foto_usuario" id="foto_usuario"
                               class="formulario__imagen-input"
                               accept="image/jpeg, image/png, image/webp">
                        <p class="formulario__imagen-hint">JPG, PNG o WEBP - Maximo 2 MB</p>
                        <button type="submit" class="boton--guardar">
                            Guardar foto
                        </button>
                    </div>

                </form>

                <div class="contenedor-derecha__caja-categorias">
                    <h2 class="caja-categorias__titulo">Categorias</h2>

                    <% if (!msgErrorCategoria.isEmpty()) { %>
                        <p class="formulario__span-error caja-categorias__error"
                           style="display:block; font-size:12px; margin-bottom:8px;">
                            <%= msgErrorCategoria %>
                        </p>
                    <% } %>

                    <div class="caja-categorias__contenido"></div>
                    <a href="#ventana-crear-categoria-confirmar"
                       class="boton--crear-categoria">Crear categoria +</a>
                </div>
            </div>

            <!-- MODAL CREAR CATEGORIA -->
            <div id="ventana-crear-categoria-confirmar" class="ventana-crear-categoria">
                <form action="${pageContext.request.contextPath}/CategoriaControlador"
                      method="post" class="ventana-crear-categoria__formulario-crear">
                    <input type="hidden" name="accion" value="crear">
                    <h2 class="formulario-crear__titulo">Crear categoria</h2>
                    <div class="formulario-crear__contenido">
                        <input type="text" name="txtNombreCategoria"
                               placeholder="Nombre categoria" class="contenido__input">
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

        </section>
    </main>

    <%
        List<Categoria> categorias = (List<Categoria>) request.getAttribute("categorias");
        if (categorias == null) categorias = new java.util.ArrayList<>();
    %>
    <script>
        const categoriasData = [
            <% for (int i = 0; i < categorias.size(); i++) {
                Categoria c = categorias.get(i); %>
            {
                idCategoria:     <%= c.getIdCategoria() %>,
                nombreCategoria: "<%= c.getNombreCategoria().replace("\"", "\\\"") %>",
                tipoTransaccion: "<%= c.getTipoTransaccion() %>"
            }<%= i < categorias.size() - 1 ? "," : "" %>
            <% } %>
        ];

        const valoresOriginales = {
            nombre:   "<%= nombreUsuario.replace("\"", "\\\"") %>",
            correo:   "<%= correoUsuario.replace("\"", "\\\"") %>",
            respaldo: "<%= correoRespaldo.replace("\"", "\\\"") %>"
        };

        const activarEdicion = () => {
            document.getElementById("nombre_usuario").removeAttribute("readonly");
            document.getElementById("correo_usuario").removeAttribute("readonly");
            document.getElementById("respaldo_correo_usuario").removeAttribute("readonly");
            document.getElementById("btn-editar-perfil").style.display = "none";
            document.getElementById("botones-guardar-cancelar").style.display = "flex";
            document.getElementById("nombre_usuario").focus();
        };

        const cancelarEdicion = () => {
            document.getElementById("nombre_usuario").value            = valoresOriginales.nombre;
            document.getElementById("correo_usuario").value            = valoresOriginales.correo;
            document.getElementById("respaldo_correo_usuario").value   = valoresOriginales.respaldo;
            document.getElementById("nombre_usuario").setAttribute("readonly", true);
            document.getElementById("correo_usuario").setAttribute("readonly", true);
            document.getElementById("respaldo_correo_usuario").setAttribute("readonly", true);
            document.getElementById("btn-editar-perfil").style.display = "";
            document.getElementById("botones-guardar-cancelar").style.display = "none";
        };

        // Ocultar mensaje de error de categoria al hacer clic fuera
        const errorCategoria = document.querySelector('.caja-categorias__error');
        if (errorCategoria) {
            document.addEventListener('click', (e) => {
                if (!errorCategoria.contains(e.target)) {
                    errorCategoria.style.display = 'none';
                }
            });
        }
    </script>

    <script src="${pageContext.request.contextPath}/Assets/Js/categorias_dinamicas.js"></script>
    <script src="${pageContext.request.contextPath}/Assets/Js/validacion_categoria.js"></script>

    <%-- VENTANA EXITO CATEGORIA --%>
    <% if (categoriaExitosa) { %>
    <div class="ventana-exito__overlay">
        <div class="ventana-exito__card">
            <div class="ventana-exito__icono">
                <i class="bi bi-check-lg"></i>
            </div>
            <h2 class="ventana-exito__titulo">Categoria creada!</h2>
            <p class="ventana-exito__mensaje">La categoria fue creada correctamente.</p>
            <a href="${pageContext.request.contextPath}/CategoriaControlador"
               class="ventana-exito__boton">Aceptar</a>
        </div>
    </div>
    <% } %>

</body>
</html>
