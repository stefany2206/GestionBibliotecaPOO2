import javax.swing.*;
import java.sql.*;

public class ReportesForm extends JFrame {
    private JPanel panelReportes;
    private JLabel lblTotalUser;
    private JLabel lblTotalLib;
    private JLabel lblPrestamosAct;
    private JLabel lblCanComentarios;
    private JLabel lblMultasTotales;
    private JLabel lblUsuarios;
    private JLabel lblLibros;
    private JLabel lblPrestamos;
    private JLabel lblComentarios;
    private JLabel lblMultas;
    private JButton btnCerrar;

    public ReportesForm() {
        setContentPane(panelReportes);
        setTitle("Reportes del Sistema");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        cargarDatosDesdeBD();

        btnCerrar.addActionListener(e -> dispose());
    }

    private void cargarDatosDesdeBD() {
        try (Connection conn = ConexionBD.conectar()) {
            // Total de usuarios
            String sqlUsuarios = "SELECT COUNT(*) FROM usuarios";
            PreparedStatement psUsuarios = conn.prepareStatement(sqlUsuarios);
            ResultSet rsUsuarios = psUsuarios.executeQuery();
            int totalUsuarios = rsUsuarios.next() ? rsUsuarios.getInt(1) : 0;

            // Total de libros
            String sqlLibros = "SELECT COUNT(*) FROM libros";
            PreparedStatement psLibros = conn.prepareStatement(sqlLibros);
            ResultSet rsLibros = psLibros.executeQuery();
            int totalLibros = rsLibros.next() ? rsLibros.getInt(1) : 0;

            // Préstamos activos (estado = 'En curso')
            String sqlPrestamos = "SELECT COUNT(*) FROM prestamos WHERE estado = 'En curso'";
            PreparedStatement psPrestamos = conn.prepareStatement(sqlPrestamos);
            ResultSet rsPrestamos = psPrestamos.executeQuery();
            int prestamosActivos = rsPrestamos.next() ? rsPrestamos.getInt(1) : 0;

            // Comentarios
            String sqlComentarios = "SELECT COUNT(*) FROM comentarios";
            PreparedStatement psComentarios = conn.prepareStatement(sqlComentarios);
            ResultSet rsComentarios = psComentarios.executeQuery();
            int comentarios = rsComentarios.next() ? rsComentarios.getInt(1) : 0;

            // Total en multas
            String sqlMultas = "SELECT SUM(monto) FROM multas";
            PreparedStatement psMultas = conn.prepareStatement(sqlMultas);
            ResultSet rsMultas = psMultas.executeQuery();
            double totalMultas = rsMultas.next() ? rsMultas.getDouble(1) : 0.0;

            // Mostrar datos en etiquetas
            lblUsuarios.setText(" " + totalUsuarios);
            lblLibros.setText(" " + totalLibros);
            lblPrestamos.setText(" " + prestamosActivos);
            lblComentarios.setText(" " + comentarios);
            lblMultas.setText("S/. " + String.format("%.2f", totalMultas));

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar reportes: " + ex.getMessage());
        }
    }
}

