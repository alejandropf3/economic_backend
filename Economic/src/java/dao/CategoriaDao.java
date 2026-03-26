package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import configuracion.ConexionDB;
import modelo.Categoria;

/**
 * DAO — CategoriaDao.java
 * <p>
 * Provee las operaciones de base de datos para la gestión de categorías de
 * transacción. Las categorías se relacionan con usuarios mediante la tabla
 * intermedia {@code Usuario_Categoria}, lo que permite que cada usuario tenga
 * su propio conjunto de categorías.
 * </p>
 */
public class CategoriaDao {

    ConexionDB cn = new ConexionDB();
    Connection con;
    PreparedStatement ps;

    /**
     * Crea una nueva categoría y la asocia al usuario dentro de una transacción.
     * <p>
     * Primero inserta en {@code Categoria} y obtiene el ID generado, luego
     * inserta en {@code Usuario_Categoria} para vincularla al usuario.
     * </p>
     *
     * @param categoria Objeto con nombre, tipo y ID de usuario.
     * @return {@code true} si la categoría fue creada exitosamente.
     */
    public boolean crear(Categoria categoria) {
        String sqlCategoria = "INSERT INTO Categoria (Tipo_transaccion, Nombre_categoria) VALUES (?, ?)";
        String sqlRelacion  = "INSERT INTO Usuario_Categoria (ID_usuario, ID_categoria) VALUES (?, ?)";
        try {
            con = cn.getConnection();
            con.setAutoCommit(false);

            ps = con.prepareStatement(sqlCategoria, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, categoria.getTipoTransaccion());
            ps.setString(2, categoria.getNombreCategoria());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int idGenerado = rs.getInt(1);

                ps = con.prepareStatement(sqlRelacion);
                ps.setLong(1, categoria.getIdUsuario());
                ps.setInt(2, idGenerado);
                ps.executeUpdate();

                con.commit();
                return true;
            }
            con.rollback();
            return false;

        } catch (SQLException e) {
            try { if (con != null) con.rollback(); } catch (SQLException ex) {}
            System.err.println("Error en DAO Crear Categoria: " + e.getMessage());
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
    }

    /**
     * Lista todas las categorías asociadas a un usuario específico.
     *
     * @param idUsuario ID del usuario propietario.
     * @return Lista de {@link Categoria} del usuario; lista vacía si no tiene ninguna.
     */
    public List<Categoria> listarPorUsuario(long idUsuario) {
        String sql = "SELECT C.ID_categoria, C.Tipo_transaccion, C.Nombre_categoria " +
                     "FROM Categoria C " +
                     "JOIN Usuario_Categoria UC ON C.ID_categoria = UC.ID_categoria " +
                     "WHERE UC.ID_usuario = ?";
        List<Categoria> lista = new ArrayList<>();
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setLong(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Categoria c = new Categoria();
                c.setIdCategoria(rs.getInt("ID_categoria"));
                c.setTipoTransaccion(rs.getString("Tipo_transaccion"));
                c.setNombreCategoria(rs.getString("Nombre_categoria"));
                c.setIdUsuario(idUsuario);
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error en DAO Listar Categorias: " + e.getMessage());
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
        return lista;
    }

    /**
     * Edita el tipo y nombre de una categoría, verificando que pertenezca al usuario.
     *
     * @param categoria Objeto con ID de categoría, ID de usuario, nuevo tipo y nuevo nombre.
     * @return {@code true} si la actualización fue exitosa.
     */
    public boolean editar(Categoria categoria) {
        String sql = "UPDATE Categoria C " +
                     "JOIN Usuario_Categoria UC ON C.ID_categoria = UC.ID_categoria " +
                     "SET C.Tipo_transaccion = ?, C.Nombre_categoria = ? " +
                     "WHERE C.ID_categoria = ? AND UC.ID_usuario = ?";
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setString(1, categoria.getTipoTransaccion());
            ps.setString(2, categoria.getNombreCategoria());
            ps.setInt(3, categoria.getIdCategoria());
            ps.setLong(4, categoria.getIdUsuario());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error en DAO Editar Categoria: " + e.getMessage());
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
    }

    /**
     * Elimina la relación usuario-categoría y, si la categoría queda huérfana
     * (sin ningún usuario asociado), la elimina también de la tabla {@code Categoria}.
     *
     * @param idCategoria ID de la categoría a eliminar.
     * @param idUsuario   ID del usuario propietario.
     * @return {@code true} si la operación fue exitosa.
     */
    public boolean eliminar(int idCategoria, long idUsuario) {
        String sqlRelacion  = "DELETE FROM Usuario_Categoria " +
                              "WHERE ID_categoria = ? AND ID_usuario = ?";
        String sqlCategoria = "DELETE FROM Categoria WHERE ID_categoria = ? " +
                              "AND NOT EXISTS (" +
                              "SELECT 1 FROM Usuario_Categoria WHERE ID_categoria = ?)";
        try {
            con = cn.getConnection();
            con.setAutoCommit(false);

            ps = con.prepareStatement(sqlRelacion);
            ps.setInt(1, idCategoria);
            ps.setLong(2, idUsuario);
            ps.executeUpdate();

            ps = con.prepareStatement(sqlCategoria);
            ps.setInt(1, idCategoria);
            ps.setInt(2, idCategoria);
            ps.executeUpdate();

            con.commit();
            return true;
        } catch (SQLException e) {
            try { if (con != null) con.rollback(); } catch (SQLException ex) {}
            System.err.println("Error en DAO Eliminar Categoria: " + e.getMessage());
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
    }

    /**
     * Verifica si el usuario ya tiene una categoría con el nombre dado.
     *
     * @param nombreCategoria Nombre a verificar.
     * @param idUsuario       ID del usuario.
     * @return {@code true} si ya existe una categoría con ese nombre para el usuario.
     */
    public boolean existeNombre(String nombreCategoria, long idUsuario) {
        String sql = "SELECT COUNT(*) FROM Categoria C " +
                     "JOIN Usuario_Categoria UC ON C.ID_categoria = UC.ID_categoria " +
                     "WHERE C.Nombre_categoria = ? AND UC.ID_usuario = ?";
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setString(1, nombreCategoria);
            ps.setLong(2, idUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error en validación nombre categoria: " + e.getMessage());
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
        return false;
    }

    /**
     * Verifica si ya existe una categoría con el nombre dado para el usuario,
     * excluyendo la categoría que se está editando (para evitar falsos positivos).
     *
     * @param nombreCategoria Nombre a verificar.
     * @param idCategoria     ID de la categoría actual (excluida de la búsqueda).
     * @param idUsuario       ID del usuario.
     * @return {@code true} si existe otra categoría con el mismo nombre.
     */
    public boolean existeNombreExcluyendoId(String nombreCategoria, int idCategoria, long idUsuario) {
        String sql = "SELECT COUNT(*) FROM Categoria C " +
                     "JOIN Usuario_Categoria UC ON C.ID_categoria = UC.ID_categoria " +
                     "WHERE C.Nombre_categoria = ? AND C.ID_categoria != ? AND UC.ID_usuario = ?";
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setString(1, nombreCategoria);
            ps.setInt(2, idCategoria);
            ps.setLong(3, idUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error en validación nombre (editar): " + e.getMessage());
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
        return false;
    }

    /**
     * Verifica si una categoría tiene transacciones asociadas.
     * Se usa para impedir la eliminación de categorías en uso.
     *
     * @param idCategoria ID de la categoría a verificar.
     * @return {@code true} si la categoría está referenciada en alguna transacción.
     */
    public boolean categoriaEnUso(int idCategoria) {
        String sql = "SELECT COUNT(*) FROM Transacciones WHERE ID_categoria = ?";
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setInt(1, idCategoria);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error en categoriaEnUso: " + e.getMessage());
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
        return false;
    }
}
