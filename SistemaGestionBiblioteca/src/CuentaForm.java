import javax.swing.*;
import java.sql.*;

public class CuentaForm extends JFrame {
    private JPanel panelCuenta;
    private JLabel lblPrestados;
    private JLabel lblDevueltos;
    private JLabel lblPerdidos;
    private JLabel lblMulta;
    private JTextField txtPrestados;
    private JTextField txtDevueltos;
    private JTextField txtPerdidos;
    private JTextField txtMulta;

    private String idUsuario;

    public CuentaForm(String idUsuario) {
        this.idUsuario = idUsuario;

        setContentPane(panelCuenta);
        setTitle("Cuenta del Usuario");
        setSize(500, 350);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        cargarDatosCuenta();
    }

    private void cargarDatosCuenta() {
        try (Connection conn = ConexionBD.conectar()) {

            // Contar préstamos por estado
            String sql = "SELECT estado, COUNT(*) AS total FROM prestamos WHERE id_usuario = ? GROUP BY estado";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            int prestados = 0, devueltos = 0, perdidos = 0;

            while (rs.next()) {
                String estado = rs.getString("estado");
                int total = rs.getInt("total");

                switch (estado) {
                    case "En curso" -> prestados = total;
                    case "Devuelto" -> devueltos = total;
                    case "Perdido" -> perdidos = total;
                }
            }

            txtPrestados.setText(String.valueOf(prestados));
            txtDevueltos.setText(String.valueOf(devueltos));
            txtPerdidos.setText(String.valueOf(perdidos));

            // Sumar todas las multas del usuario
            sql = "SELECT SUM(monto) AS totalMulta FROM multas WHERE id_usuario = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, idUsuario);
            rs = ps.executeQuery();

            if (rs.next()) {
                double totalMulta = rs.getDouble("totalMulta");
                txtMulta.setText(String.format("%.2f", totalMulta));
            } else {
                txtMulta.setText("0.00");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar cuenta: " + e.getMessage());
        }
    }
}

