package dao;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import configuracion.ConexionDB;
import modelo.Transaccion;
 
public class TransaccionDao {
 
    ConexionDB cn = new ConexionDB();
    Connection con;
    PreparedStatement ps;
 
    public boolean crear(Transaccion t) {
        String sql = "INSERT INTO Transacciones (ID_usuario, ID_categoria, Valor_transaccion, Descripcion, Fecha_realizacion) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, t.getIdUsuario());
            ps.setInt(2, t.getIdCategoria());
            ps.setBigDecimal(3, t.getValorTransaccion());
            ps.setString(4, t.getDescripcion());
            ps.setDate(5, java.sql.Date.valueOf(t.getFechaRealizacion()));
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error en DAO Crear Transaccion: " + e.getMessage());
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
    }
}