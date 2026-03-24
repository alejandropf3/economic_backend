<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="modelo.Usuario"%>
<%
    if (request.getAttribute("usuarios") == null) {
        response.sendRedirect(request.getContextPath() + "/AdminControlador");
        return;
    }

    Usuario usuarioSesion  = (Usuario) session.getAttribute("usuario");
    String nombreAdmin     = usuarioSesion != null ? usuarioSesion.getNombre() : "";
    String correoAdmin     = usuarioSesion != null ? usuarioSesion.getCorreo()  : "";
    String urlImagenPerfil = usuarioSesion != null && usuarioSesion.getUrlImagen() != null
                             ? usuarioSesion.getUrlImagen() : null;

    List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");

    // Parámetros de respuesta del servidor
    String res           = request.getParameter("res")           != null ? request.getParameter("res")           : "";
    String transStr      = request.getParameter("transacciones") != null ? request.getParameter("transacciones") : "0";
    String catStr        = request.getParameter("categorias")    != null ? request.getParameter("categorias")    : "0";
    String idUsuarioParam= request.getParameter("idUsuario")     != null ? request.getParameter("idUsuario")     : "";

    int numTransacciones = 0;
    int numCategorias    = 0;
    try { numTransacciones = Integer.parseInt(transStr); } catch (NumberFormatException e) {}
    try { numCategorias    = Integer.parseInt(catStr);   } catch (NumberFormatException e) {}
%>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Administrar usuarios — Economic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Assets/Styles_principal.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>

<body>

    <!-- ══ SIDEBAR ══════════════════════════════════════════════════════════ -->
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
                <a href="${pageContext.request.contextPath}/ResumenControlador" class="sidebar__link">
                    <i class="bi bi-file-text-fill"></i> Historial de resúmenes
                </a>
                <a href="${pageContext.request.contextPath}/CategoriaControlador" class="sidebar__link">
                    <i class="bi bi-gear-fill"></i> Opciones
                </a>
                <a href="${pageContext.request.contextPath}/AdminControlador" class="sidebar__link sidebar__link--activo">
                    <i class="bi bi-people-fill"></i> Admin
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
                        <p class="sidebar__usuario-nombre"><%= nombreAdmin %></p>
                        <p class="sidebar__usuario-email"><%= correoAdmin %></p>
                    </div>
                </div>
                <a href="${pageContext.request.contextPath}/index.jsp" class="sidebar__salir">
                    <i class="bi bi-box-arrow-right"></i> Cerrar sesión
                </a>
            </div>
        </div>
    </aside>

    <!-- ══ HEADER ═══════════════════════════════════════════════════════════ -->
    <header class="main-header">
        <div class="encabezado">
            <h1 class="encabezado__logo">ECONOMIC</h1>
            <label for="sidebar-toggle" class="hamburguesa">
                <span class="hamburguesa__linea"></span>
                <span class="hamburguesa__linea"></span>
                <span class="hamburguesa__linea"></span>
            </label>
            <nav class="encabezado__navegacion">
                <a href="${pageContext.request.contextPath}/MenuControlador"     class="encabezado__link">Inicio</a>
                <a href="${pageContext.request.contextPath}/HistorialControlador" class="encabezado__link">Historial de transacciones</a>
                <a href="${pageContext.request.contextPath}/ResumenControlador"   class="encabezado__link">Historial de resúmenes</a>
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
                            <p><%= nombreAdmin %></p>
                        </div>
                        <div class="ventana-salida__informacion">
                            <p>Email</p>
                            <p><%= correoAdmin %></p>
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

    <!-- ══ MAIN ══════════════════════════════════════════════════════════════ -->
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
                        <th>Correo electrónico</th>
                        <th>Rol</th>
                        <th>Opciones</th>
                    </tr>
                </thead>
                <tbody class="layout-general__tabla-cuerpo">
                    <% if (usuarios.isEmpty()) { %>
                    <tr>
                        <td colspan="5" style="text-align:center; padding:30px; color:#666;">
                            No hay usuarios registrados.
                        </td>
                    </tr>
                    <% } else {
                           for (Usuario u : usuarios) { %>
                    <tr>
                        <td><%= u.getIdUsuario() %></td>
                        <td><%= u.getNombre() %></td>
                        <td><%= u.getCorreo() %></td>
                        <td><%= u.getCorreoRespaldo() /* contiene el nombre del rol */ %></td>
                        <td>
                            <button class="btn-eliminar"
                                    title="Eliminar usuario"
                                    onclick="abrirModalEliminar(<%= u.getIdUsuario() %>, '<%= u.getNombre().replace("'", "\\'") %>')">
                                <i class="bi bi-trash3-fill"></i>
                            </button>
                        </td>
                    </tr>
                    <% } } %>
                </tbody>
            </table>

        </section>
    </main>

    <!-- ══ MODAL ELIMINAR ════════════════════════════════════════════════════ -->
    <div class="modal-overlay" id="modalEliminar">
        <div class="modal">
            <div class="modal__cabecera">
                <i class="bi bi-exclamation-triangle-fill"></i>
                <h3>Eliminar usuario</h3>
            </div>
            <div class="modal__cuerpo">
                <p class="modal__nombre-usuario">
                    Usuario seleccionado: <strong id="modal-nombre-usuario">—</strong>
                </p>
                <div>
                    <label class="modal__label" for="modal-pass">
                        Ingresa tu contraseña de administrador para confirmar:
                    </label>
                    <input type="password" id="modal-pass" class="modal__input-pass"
                           placeholder="Contraseña del administrador" autocomplete="off">
                </div>
                <div class="modal__mensaje-error" id="modal-error-js">
                    <i class="bi bi-x-circle-fill"></i>
                    <span id="modal-error-texto">La contraseña no puede estar vacía.</span>
                </div>
            </div>
            <div class="modal__acciones">
                <button class="boton--cancelar" onclick="cerrarModal()">Cancelar</button>
                <button class="boton--eliminar-confirmar" onclick="confirmarEliminar()">
                    <i class="bi bi-trash3-fill"></i> Eliminar
                </button>
            </div>
        </div>
    </div>

    <!-- Formulario oculto para enviar el POST -->
    <form id="form-eliminar" action="${pageContext.request.contextPath}/AdminControlador"
          method="POST" style="display:none;">
        <input type="hidden" name="accion"    value="eliminar">
        <input type="hidden" name="idUsuario" id="form-id-usuario">
        <input type="hidden" name="passAdmin" id="form-pass-admin">
    </form>

    <!-- ══ TOASTS (mensajes del servidor) ════════════════════════════════════ -->
    <% if ("eliminado_ok".equals(res)) { %>
        <div class="toast toast--ok" id="toast">
            <i class="bi bi-check-circle-fill"></i> Usuario eliminado correctamente.
        </div>
    <% } else if ("pass_incorrecta".equals(res)) { %>
        <div class="toast toast--error" id="toast">
            <i class="bi bi-x-circle-fill"></i> Contraseña incorrecta. Intenta de nuevo.
        </div>
    <% } else if ("tiene_asociados".equals(res)) { %>
        <div class="toast toast--error" id="toast">
            <i class="bi bi-x-circle-fill"></i>
            No se puede eliminar: el usuario tiene
            <% if (numTransacciones > 0) { %>
                <strong><%= numTransacciones %> transacción(es)</strong>
            <% } %>
            <% if (numTransacciones > 0 && numCategorias > 0) { %> y <% } %>
            <% if (numCategorias > 0) { %>
                <strong><%= numCategorias %> categoría(s)</strong>
            <% } %>
            asociada(s). Elimínalas antes.
        </div>
    <% } else if ("no_autoeliminacion".equals(res)) { %>
        <div class="toast toast--error" id="toast">
            <i class="bi bi-x-circle-fill"></i> No puedes eliminar tu propia cuenta.
        </div>
    <% } else if ("error_eliminar".equals(res)) { %>
        <div class="toast toast--error" id="toast">
            <i class="bi bi-x-circle-fill"></i> Ocurrió un error al eliminar. Intenta de nuevo.
        </div>
    <% } %>

    <!-- ══ JAVASCRIPT ═════════════════════════════════════════════════════════ -->
    <script>
        let idUsuarioSeleccionado = null;

        // Abrir modal de confirmación
        function abrirModalEliminar(id, nombre) {
            idUsuarioSeleccionado = id;
            document.getElementById("modal-nombre-usuario").textContent = nombre;
            document.getElementById("modal-pass").value = "";
            ocultarErrorJs();
            document.getElementById("modalEliminar").classList.add("activo");
            setTimeout(() => document.getElementById("modal-pass").focus(), 100);
        }

        // Cerrar modal
        function cerrarModal() {
            document.getElementById("modalEliminar").classList.remove("activo");
            idUsuarioSeleccionado = null;
        }

        // Validar y enviar formulario
        function confirmarEliminar() {
            const pass = document.getElementById("modal-pass").value.trim();
            if (!pass) {
                mostrarErrorJs("La contraseña no puede estar vacía.");
                return;
            }
            // Rellenar el formulario oculto y enviarlo
            document.getElementById("form-id-usuario").value = idUsuarioSeleccionado;
            document.getElementById("form-pass-admin").value  = pass;
            document.getElementById("form-eliminar").submit();
        }

        // Cerrar modal al hacer clic fuera
        document.getElementById("modalEliminar").addEventListener("click", function (e) {
            if (e.target === this) cerrarModal();
        });

        // Enter en el input de contraseña confirma
        document.getElementById("modal-pass").addEventListener("keydown", function (e) {
            if (e.key === "Enter") confirmarEliminar();
        });

        // Helpers para mensajes de error locales (JS)
        function mostrarErrorJs(texto) {
            const el = document.getElementById("modal-error-js");
            document.getElementById("modal-error-texto").textContent = texto;
            el.classList.add("visible");
        }
        function ocultarErrorJs() {
            document.getElementById("modal-error-js").classList.remove("visible");
        }

        // Auto-ocultar toasts del servidor tras 5 segundos
        const toast = document.getElementById("toast");
        if (toast) {
            setTimeout(() => {
                toast.style.transition = "opacity 0.5s ease";
                toast.style.opacity = "0";
                setTimeout(() => toast.remove(), 500);
            }, 5000);
        }

        // Re-abrir modal si hubo error del servidor para el mismo usuario
        <% if (("pass_incorrecta".equals(res) || "tiene_asociados".equals(res)) && !idUsuarioParam.isEmpty()) { %>
            // El modal se cierra tras la redirección; el toast ya informa al usuario.
        <% } %>
    </script>

</body>
</html>
