<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="modelo.Usuario"%>
<%@page import="modelo.Meta"%>
<%@page import="java.util.List"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%
    if (request.getAttribute("metas") == null) {
        response.sendRedirect(request.getContextPath() + "/MetaControlador");
        return;
    }

    Usuario usuarioSesion  = (Usuario) session.getAttribute("usuario");
    String nombreUsuario   = usuarioSesion != null ? usuarioSesion.getNombre() : "";
    String correoUsuario   = usuarioSesion != null ? usuarioSesion.getCorreo()  : "";
    String urlImagenPerfil = usuarioSesion != null && usuarioSesion.getUrlImagen() != null
                             ? usuarioSesion.getUrlImagen() : null;

    List<Meta> metas = (List<Meta>) request.getAttribute("metas");

    // ── Mensajes del servidor ─────────────────────────────────────────────────
    String resCrear    = (String) request.getAttribute("resCrear");
    String errorCrear  = (String) request.getAttribute("errorCrear");
    String resEditar   = (String) request.getAttribute("resEditar");
    String errorEditar = (String) request.getAttribute("errorEditar");
    String resEliminar = (String) request.getAttribute("resEliminar");
    String errorEliminar = (String) request.getAttribute("errorEliminar");
    String resAbonar   = (String) request.getAttribute("resAbonar");
    String errorAbonar = (String) request.getAttribute("errorAbonar");

    Long   idMetaError    = (Long)       request.getAttribute("idMetaError");
    Long   idMetaEliminar = (Long)       request.getAttribute("idMetaEliminar");
    Long   idMetaAbonar   = (Long)       request.getAttribute("idMetaAbonar");
    BigDecimal montoEliminar = (BigDecimal) request.getAttribute("montoMetaEliminar");

    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
%>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Metas de Ahorro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Assets/Styles_principal.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Assets/Css/Pages/Style_metas_ahorro.css">
</head>

<body>

    <%-- ══ Toast de notificaciones ══ --%>
    <div id="toast" class="toast"></div>

    <%-- ══ SIDEBAR ══ --%>
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
                <a href="${pageContext.request.contextPath}/MetaControlador" class="sidebar__link sidebar__link--activo">
                    <i class="bi bi-piggy-bank-fill"></i> Metas de ahorro
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

    <%-- ══ HEADER ══ --%>
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
                <a href="${pageContext.request.contextPath}/HistorialControlador" class="encabezado__link">Transacciones</a>
                <a href="${pageContext.request.contextPath}/ResumenControlador" class="encabezado__link">Resúmenes</a>
                <a href="${pageContext.request.contextPath}/MetaControlador" class="encabezado__link">Metas</a>
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

    <%-- ══ MAIN ══ --%>
    <main class="main-menu">
        <section class="main-menu__layout-general">

            <div class="layout-menu__informacion">
                <h2 class="layout-menu__titulo">Metas de Ahorro</h2>
            </div>

            <div class="layout-metas__contenedor">

                <%-- ── Barra superior ── --%>
                <div class="metas__barra-superior">
                    <h2 class="metas__titulo">Mis metas</h2>
                    <button class="boton" onclick="abrirModalCrear()">
                        <i class="bi bi-plus-lg"></i>&nbsp;Nueva meta
                    </button>
                </div>

                <%-- ── Tabla de metas ── --%>
                <% if (metas.isEmpty()) { %>
                <div class="metas__vacio">
                    <i class="bi bi-piggy-bank"></i>
                    <p>No tienes metas registradas aún.</p>
                    <button class="boton" onclick="abrirModalCrear()">Crear primera meta</button>
                </div>
                <% } else { %>
                <div class="metas__tabla-wrapper">
                    <table class="metas__tabla">
                        <thead>
                            <tr>
                                <th>Nombre</th>
                                <th>Objetivo</th>
                                <th>Progreso</th>
                                <th>Creación</th>
                                <th>Fecha límite</th>
                                <th>Estado</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                        <% for (Meta m : metas) {
                               int pct = m.getPorcentajeProgreso();
                               String claseBarra = m.isCompletada() ? "completada"
                                                 : m.isVencida()    ? "vencida" : "";
                               String estadoLabel = m.isCompletada() ? "Completada"
                                                  : m.isVencida()    ? "Incompleta" : "En proceso";
                               String estadoClase = m.isCompletada() ? "completada"
                                                  : m.isVencida()    ? "cancelada" : "activa";
                        %>
                            <tr>
                                <%-- Nombre --%>
                                <td style="text-align:left; font-weight:500;">
                                    <%= m.getNombreMeta() %>
                                </td>

                                <%-- Monto objetivo / actual --%>
                                <td>
                                    <span style="font-weight:700; font-family:'Courier New',monospace;">
                                        $<%= String.format("%,.2f", m.getMontoObjetivo()) %>
                                    </span>
                                    <br>
                                    <span style="font-size:13px; color:#888;">
                                        Ahorrado: $<%= String.format("%,.2f", m.getMontoActual()) %>
                                    </span>
                                </td>

                                <%-- Barra de progreso --%>
                                <td>
                                    <div class="progreso__contenedor">
                                        <div class="progreso__barra-fondo">
                                            <div class="progreso__barra-relleno <%= claseBarra %>"
                                                 style="width:<%= pct %>%"></div>
                                        </div>
                                        <span class="progreso__texto"><%= pct %>%</span>
                                    </div>
                                </td>

                                <%-- Fechas --%>
                                <td style="font-size:14px; color:#666;">
                                    <%= m.getFechaCreacion().format(fmt) %>
                                </td>
                                <td style="font-size:14px; <%= m.isVencida() ? "color:var(--color_egresos); font-weight:600;" : "" %>">
                                    <%= m.getFechaLimite().format(fmt) %>
                                </td>

                                <%-- Estado --%>
                                <td>
                                    <span class="estado-badge <%= estadoClase %>">
                                        <i class="bi <%= m.isCompletada() ? "bi-check-circle-fill"
                                                       : m.isVencida()    ? "bi-x-circle-fill"
                                                       : "bi-clock" %>"></i>
                                        <%= estadoLabel %>
                                    </span>
                                </td>

                                <%-- Acciones --%>
                                <td>
                                    <div class="metas__acciones">
                                        <button class="meta-btn meta-btn--abonar"
                                                <%= !m.isActiva() ? "disabled" : "" %>
                                                data-action="abonar"
                                                data-id="<%= m.getIdMeta() %>"
                                                data-nombre="<%= m.getNombreMeta() %>"
                                                data-objetivo="<%= String.format("%,.2f", m.getMontoObjetivo()) %>"
                                                data-actual="<%= String.format("%,.2f", m.getMontoActual()) %>">
                                            <i class="bi bi-plus-circle"></i> Abonar
                                        </button>
                                        <button class="meta-btn meta-btn--editar"
                                                <%= !m.isActiva() ? "disabled" : "" %>
                                                data-action="editar"
                                                data-id="<%= m.getIdMeta() %>"
                                                data-nombre="<%= m.getNombreMeta() %>"
                                                data-fecha="<%= m.getFechaLimite().toString() %>">
                                            <i class="bi bi-pencil-fill"></i> Editar
                                        </button>
                                        <button class="meta-btn meta-btn--eliminar"
                                                data-action="eliminar"
                                                data-id="<%= m.getIdMeta() %>"
                                                data-nombre="<%= m.getNombreMeta() %>"
                                                data-monto="<%= m.getMontoActual() %>">
                                            <i class="bi bi-trash-fill"></i> Eliminar
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
                <% } %>

            </div>
        </section>
    </main>

    <%-- ══════════════════════════════════════════════════════════
         MODAL — CREAR META
    ══════════════════════════════════════════════════════════ --%>
    <div id="modal-crear" class="modal-overlay">
        <div class="modal">
            <div class="modal__header">
                <div class="modal__header-icono"><i class="bi bi-piggy-bank-fill"></i></div>
                <h3>Nueva meta de ahorro</h3>
            </div>
            <form action="${pageContext.request.contextPath}/MetaControlador"
                  method="post" id="form-crear" novalidate>
                <input type="hidden" name="accion" value="crear">
                <div class="modal__body">

                    <div class="modal__campo">
                        <label class="modal__label" for="crear-nombre">Nombre de la meta</label>
                        <input type="text" id="crear-nombre" name="nombre"
                               class="modal__input" placeholder="Ej: Vacaciones 2027"
                               maxlength="100">
                        <span class="modal__error" id="err-crear-nombre"></span>
                    </div>

                    <div class="modal__campo">
                        <label class="modal__label" for="crear-monto">Monto objetivo ($)</label>
                        <input type="number" id="crear-monto" name="montoObjetivo"
                               class="modal__input" placeholder="Ej: 500000"
                               min="0.01" step="0.01">
                        <span class="modal__error" id="err-crear-monto"></span>
                    </div>

                    <div class="modal__campo">
                        <label class="modal__label" for="crear-fecha">Fecha límite</label>
                        <input type="date" id="crear-fecha" name="fechaLimite"
                               class="modal__input">
                        <span class="modal__error" id="err-crear-fecha"></span>
                    </div>

                </div>
                <div class="modal__footer">
                    <button type="button" class="boton--salir" onclick="cerrarModal('modal-crear')">
                        Cancelar
                    </button>
                    <button type="submit" class="boton">Crear meta</button>
                </div>
            </form>
        </div>
    </div>

    <%-- ══════════════════════════════════════════════════════════
         MODAL — EDITAR META
    ══════════════════════════════════════════════════════════ --%>
    <div id="modal-editar" class="modal-overlay">
        <div class="modal">
            <div class="modal__header">
                <div class="modal__header-icono"><i class="bi bi-pencil-fill"></i></div>
                <h3>Editar meta de ahorro</h3>
            </div>
            <form action="${pageContext.request.contextPath}/MetaControlador"
                  method="post" id="form-editar" novalidate>
                <input type="hidden" name="accion" value="editar">
                <input type="hidden" name="idMeta" id="editar-id">
                <div class="modal__body">

                    <div class="modal__campo">
                        <label class="modal__label" for="editar-nombre">Nombre de la meta</label>
                        <input type="text" id="editar-nombre" name="nombre"
                               class="modal__input" maxlength="100">
                        <span class="modal__error" id="err-editar-nombre"></span>
                    </div>

                    <div class="modal__campo">
                        <label class="modal__label" for="editar-fecha">Fecha límite</label>
                        <input type="date" id="editar-fecha" name="fechaLimite"
                               class="modal__input">
                        <span class="modal__error" id="err-editar-fecha"></span>
                    </div>

                    <p style="font-size:13px; color:#888; margin-top:-8px;">
                        <i class="bi bi-info-circle"></i>
                        Solo se puede modificar el nombre y la fecha límite.
                    </p>

                </div>
                <div class="modal__footer">
                    <button type="button" class="boton--salir" onclick="cerrarModal('modal-editar')">
                        Cancelar
                    </button>
                    <button type="submit" class="boton">Guardar cambios</button>
                </div>
            </form>
        </div>
    </div>

    <%-- ══════════════════════════════════════════════════════════
         MODAL — ABONAR
    ══════════════════════════════════════════════════════════ --%>
    <div id="modal-abonar" class="modal-overlay">
        <div class="modal">
            <div class="modal__header">
                <div class="modal__header-icono"><i class="bi bi-plus-circle-fill"></i></div>
                <h3>Agregar ahorro</h3>
            </div>
            <form action="${pageContext.request.contextPath}/MetaControlador"
                  method="post" id="form-abonar" novalidate>
                <input type="hidden" name="accion" value="abonar">
                <input type="hidden" name="idMeta" id="abonar-id">
                <div class="modal__body">

                    <div id="abonar-resumen" style="
                        background: rgba(0,35,73,0.04);
                        border-radius: 12px;
                        padding: 14px 16px;
                        display: flex;
                        flex-direction: column;
                        gap: 6px;">
                        <p style="font-weight:600; font-size:16px;" id="abonar-nombre-meta"></p>
                        <p style="font-size:14px; color:#666;">
                            Objetivo: <strong id="abonar-objetivo"></strong>
                            &nbsp;|&nbsp;
                            Ahorrado: <strong id="abonar-actual"></strong>
                        </p>
                    </div>

                    <div class="modal__campo">
                        <label class="modal__label" for="abonar-monto">Monto a abonar ($)</label>
                        <input type="number" id="abonar-monto" name="montoAbono"
                               class="modal__input" placeholder="Ej: 50000"
                               min="0.01" step="0.01">
                        <span class="modal__error" id="err-abonar-monto"></span>
                    </div>

                </div>
                <div class="modal__footer">
                    <button type="button" class="boton--salir" onclick="cerrarModal('modal-abonar')">
                        Cancelar
                    </button>
                    <button type="submit" class="boton">Abonar</button>
                </div>
            </form>
        </div>
    </div>

    <%-- ══════════════════════════════════════════════════════════
         MODAL — ELIMINAR META
    ══════════════════════════════════════════════════════════ --%>
    <div id="modal-eliminar" class="modal-overlay">
        <div class="modal">
            <div class="modal__header" style="background: linear-gradient(135deg,#8B0000,#C20000);">
                <div class="modal__header-icono"><i class="bi bi-trash-fill"></i></div>
                <h3>Eliminar meta</h3>
            </div>
            <form action="${pageContext.request.contextPath}/MetaControlador"
                  method="post" id="form-eliminar">
                <input type="hidden" name="accion" value="eliminar">
                <input type="hidden" name="idMeta" id="eliminar-id">
                <input type="hidden" name="confirmado" id="eliminar-confirmado" value="false">
                <div class="modal__body">
                    <p id="eliminar-mensaje" style="font-size:16px; color:var(--color_texto-oscuro);"></p>
                    <p id="eliminar-advertencia" style="display:none; font-size:14px;
                        color:var(--color_egresos); background:rgba(194,0,0,0.08);
                        border-radius:10px; padding:12px; margin-top:4px;">
                        <i class="bi bi-exclamation-triangle-fill"></i>
                        Esta meta tiene fondos acumulados. ¿Confirmas que deseas eliminarla de todas formas?
                    </p>
                </div>
                <div class="modal__footer">
                    <button type="button" class="boton--salir" onclick="cerrarModal('modal-eliminar')">
                        Cancelar
                    </button>
                    <button type="submit" id="btn-confirmar-eliminar"
                            style="background:linear-gradient(135deg,#dc3545,#c82333);"
                            class="boton">
                        Eliminar
                    </button>
                </div>
            </form>
        </div>
    </div>

    <%-- ══ Script de mensajes del servidor y lógica de modales ══ --%>
    <script>
    // ── Utilidades de modal ───────────────────────────────────────────────────
    function abrirModal(id) {
        document.getElementById(id).classList.add('activo');
    }

    function cerrarModal(id) {
        document.getElementById(id).classList.remove('activo');
        // Limpiar errores del modal
        document.querySelectorAll('#' + id + ' .modal__error').forEach(e => {
            e.textContent = '';
            e.classList.remove('visible');
        });
        document.querySelectorAll('#' + id + ' .modal__input').forEach(i => {
            i.classList.remove('error');
        });
    }

    // Cerrar modal al hacer click fuera de él
    document.querySelectorAll('.modal-overlay').forEach(overlay => {
        overlay.addEventListener('click', function(e) {
            if (e.target === this) cerrarModal(this.id);
        });
    });

    // ── Delegación de eventos en botones de tabla ─────────────────────────────
    document.addEventListener('click', function(e) {
        const btn = e.target.closest('[data-action]');
        if (!btn || btn.disabled) return;

        const action  = btn.dataset.action;
        const id      = btn.dataset.id;
        const nombre  = btn.dataset.nombre;

        if (action === 'abonar') {
            abrirModalAbonar(id, nombre, btn.dataset.objetivo, btn.dataset.actual);
        } else if (action === 'editar') {
            abrirModalEditar(id, nombre, btn.dataset.fecha);
        } else if (action === 'eliminar') {
            abrirModalEliminar(id, nombre, parseFloat(btn.dataset.monto));
        }
    });

    // ── Abrir modal crear ─────────────────────────────────────────────────────
    function abrirModalCrear() {
        document.getElementById('form-crear').reset();
        // Establecer fecha mínima = mañana
        const manana = new Date();
        manana.setDate(manana.getDate() + 1);
        document.getElementById('crear-fecha').min = manana.toISOString().split('T')[0];
        abrirModal('modal-crear');
    }

    // ── Abrir modal editar ────────────────────────────────────────────────────
    function abrirModalEditar(idMeta, nombre, fechaLimite) {
        document.getElementById('editar-id').value = idMeta;
        document.getElementById('editar-nombre').value = nombre;
        document.getElementById('editar-fecha').value = fechaLimite;
        // Fecha mínima = mañana
        const manana = new Date();
        manana.setDate(manana.getDate() + 1);
        document.getElementById('editar-fecha').min = manana.toISOString().split('T')[0];
        abrirModal('modal-editar');
    }

    // ── Abrir modal abonar ────────────────────────────────────────────────────
    function abrirModalAbonar(idMeta, nombre, objetivo, actual) {
        document.getElementById('abonar-id').value = idMeta;
        document.getElementById('abonar-nombre-meta').textContent = nombre;
        document.getElementById('abonar-objetivo').textContent = '$' + objetivo;
        document.getElementById('abonar-actual').textContent = '$' + actual;
        document.getElementById('abonar-monto').value = '';
        abrirModal('modal-abonar');
    }

    // ── Abrir modal eliminar ──────────────────────────────────────────────────
    function abrirModalEliminar(idMeta, nombre, montoActual) {
        document.getElementById('eliminar-id').value = idMeta;
        document.getElementById('eliminar-mensaje').textContent =
            '¿Estás seguro de que deseas eliminar la meta "' + nombre + '"? Esta acción no se puede deshacer.';

        const advertencia = document.getElementById('eliminar-advertencia');
        const confirmado  = document.getElementById('eliminar-confirmado');

        if (montoActual > 0) {
            advertencia.style.display = 'block';
            confirmado.value = 'true';
        } else {
            advertencia.style.display = 'none';
            confirmado.value = 'false';
        }
        abrirModal('modal-eliminar');
    }

    // ── Validación cliente — Crear ────────────────────────────────────────────
    document.getElementById('form-crear').addEventListener('submit', function(e) {
        let valido = true;

        const nombre = document.getElementById('crear-nombre');
        const monto  = document.getElementById('crear-monto');
        const fecha  = document.getElementById('crear-fecha');

        limpiarErrorModal('crear-nombre', 'err-crear-nombre');
        limpiarErrorModal('crear-monto',  'err-crear-monto');
        limpiarErrorModal('crear-fecha',  'err-crear-fecha');

        if (!nombre.value.trim()) {
            mostrarErrorModal('crear-nombre', 'err-crear-nombre', 'El nombre es obligatorio.');
            valido = false;
        } else if (nombre.value.trim().length < 3) {
            mostrarErrorModal('crear-nombre', 'err-crear-nombre', 'El nombre debe tener al menos 3 caracteres.');
            valido = false;
        }

        if (!monto.value || parseFloat(monto.value) <= 0) {
            mostrarErrorModal('crear-monto', 'err-crear-monto', 'El monto debe ser mayor a cero.');
            valido = false;
        }

        if (!fecha.value) {
            mostrarErrorModal('crear-fecha', 'err-crear-fecha', 'La fecha límite es obligatoria.');
            valido = false;
        } else {
            const hoy = new Date(); hoy.setHours(0,0,0,0);
            const fl  = new Date(fecha.value + 'T00:00:00');
            if (fl <= hoy) {
                mostrarErrorModal('crear-fecha', 'err-crear-fecha', 'La fecha límite debe ser posterior a hoy.');
                valido = false;
            }
        }

        if (!valido) e.preventDefault();
    });

    // ── Validación cliente — Editar ───────────────────────────────────────────
    document.getElementById('form-editar').addEventListener('submit', function(e) {
        let valido = true;

        const nombre = document.getElementById('editar-nombre');
        const fecha  = document.getElementById('editar-fecha');

        limpiarErrorModal('editar-nombre', 'err-editar-nombre');
        limpiarErrorModal('editar-fecha',  'err-editar-fecha');

        if (!nombre.value.trim()) {
            mostrarErrorModal('editar-nombre', 'err-editar-nombre', 'El nombre es obligatorio.');
            valido = false;
        } else if (nombre.value.trim().length < 3) {
            mostrarErrorModal('editar-nombre', 'err-editar-nombre', 'El nombre debe tener al menos 3 caracteres.');
            valido = false;
        }

        if (!fecha.value) {
            mostrarErrorModal('editar-fecha', 'err-editar-fecha', 'La fecha límite es obligatoria.');
            valido = false;
        } else {
            const hoy = new Date(); hoy.setHours(0,0,0,0);
            const fl  = new Date(fecha.value + 'T00:00:00');
            if (fl <= hoy) {
                mostrarErrorModal('editar-fecha', 'err-editar-fecha', 'La fecha límite debe ser posterior a hoy.');
                valido = false;
            }
        }

        if (!valido) e.preventDefault();
    });

    // ── Validación cliente — Abonar ───────────────────────────────────────────
    document.getElementById('form-abonar').addEventListener('submit', function(e) {
        const monto = document.getElementById('abonar-monto');
        limpiarErrorModal('abonar-monto', 'err-abonar-monto');

        if (!monto.value || parseFloat(monto.value) <= 0) {
            mostrarErrorModal('abonar-monto', 'err-abonar-monto', 'El monto debe ser mayor a cero.');
            e.preventDefault();
        }
    });

    // ── Helpers de error en modal ─────────────────────────────────────────────
    function mostrarErrorModal(inputId, spanId, mensaje) {
        document.getElementById(inputId).classList.add('error');
        const span = document.getElementById(spanId);
        span.textContent = mensaje;
        span.classList.add('visible');
    }

    function limpiarErrorModal(inputId, spanId) {
        document.getElementById(inputId).classList.remove('error');
        const span = document.getElementById(spanId);
        span.textContent = '';
        span.classList.remove('visible');
    }

    // ── Toast de notificaciones ───────────────────────────────────────────────
    function mostrarToast(mensaje, tipo) {
        const toast = document.getElementById('toast');
        toast.textContent = mensaje;
        toast.className = 'toast ' + tipo + ' visible';
        setTimeout(() => toast.classList.remove('visible'), 3500);
    }

    // ── Mensajes del servidor ─────────────────────────────────────────────────
    const mensajesServidor = {
        // Crear
        resCrear:   '<%= resCrear != null ? resCrear : "" %>',
        errorCrear: '<%= errorCrear != null ? errorCrear : "" %>',
        // Editar
        resEditar:   '<%= resEditar != null ? resEditar : "" %>',
        errorEditar: '<%= errorEditar != null ? errorEditar : "" %>',
        // Eliminar
        resEliminar:   '<%= resEliminar != null ? resEliminar : "" %>',
        errorEliminar: '<%= errorEliminar != null ? errorEliminar : "" %>',
        // Abonar
        resAbonar:   '<%= resAbonar != null ? resAbonar : "" %>',
        errorAbonar: '<%= errorAbonar != null ? errorAbonar : "" %>',
    };

    const textoError = {
        nombre_vacio:           'El nombre es obligatorio.',
        nombre_muy_corto:       'El nombre debe tener al menos 3 caracteres.',
        nombre_muy_largo:       'El nombre no puede superar 100 caracteres.',
        monto_vacio:            'El monto es obligatorio.',
        monto_invalido:         'El monto no es válido.',
        monto_debe_ser_positivo:'El monto debe ser mayor a cero.',
        fecha_vacia:            'La fecha límite es obligatoria.',
        fecha_invalida:         'El formato de fecha no es válido.',
        fecha_debe_ser_futura:  'La fecha límite debe ser posterior a hoy.',
        meta_duplicada:         'Ya tienes una meta con ese nombre y fecha límite.',
        error_general:          'Ocurrió un error. Intenta de nuevo.',
    };

    document.addEventListener('DOMContentLoaded', function() {
        if (mensajesServidor.resCrear   === 'ok') mostrarToast('¡Meta creada exitosamente!', 'exito');
        if (mensajesServidor.resEditar  === 'ok') mostrarToast('¡Meta actualizada exitosamente!', 'exito');
        if (mensajesServidor.resEliminar=== 'ok') mostrarToast('Meta eliminada.', 'advertencia');
        if (mensajesServidor.resAbonar  === 'ok') mostrarToast('¡Abono registrado exitosamente!', 'exito');

        if (mensajesServidor.errorCrear) {
            mostrarToast(textoError[mensajesServidor.errorCrear] || mensajesServidor.errorCrear, 'error');
            abrirModal('modal-crear');
        }
        if (mensajesServidor.errorEditar) {
            mostrarToast(textoError[mensajesServidor.errorEditar] || mensajesServidor.errorEditar, 'error');
            abrirModal('modal-editar');
        }
        if (mensajesServidor.errorAbonar) {
            mostrarToast(textoError[mensajesServidor.errorAbonar] || mensajesServidor.errorAbonar, 'error');
            abrirModal('modal-abonar');
        }
        if (mensajesServidor.errorEliminar && mensajesServidor.errorEliminar !== 'requiere_confirmacion') {
            mostrarToast(textoError[mensajesServidor.errorEliminar] || mensajesServidor.errorEliminar, 'error');
        }
    });
    </script>

</body>
</html>
