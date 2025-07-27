import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InfoUserAdministrador  extends JFrame {
    private JLabel lblTitulo;
    private JLabel lblNombre;
    private JLabel lblCorreo;
    private JLabel lblTelefono;
    private JLabel lblDireccion;
    private JLabel lblDepartamento;
    private JLabel lblCargo;
    private JLabel lblJornada;
    private JLabel lblID;
    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JTextField txtTelefono;
    private JTextField txtDireccion;
    private JTextField txtDepartamento;
    private JTextField txtCargo;
    private JTextField txtJornada;
    private JTextField txtID;
    private JPanel panelUserAdmi;

    private final String idUsuario;

    public InfoUserAdministrador(String idUsuario) {
        this.idUsuario = idUsuario;

        setContentPane(panelUserAdmi);
        setTitle("Información de Administrador");
        setSize(400, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // Opcional: solo lectura
        setReadOnly(true);

        cargarDatosUsuario();
        setVisible(true);
    }

    private void setReadOnly(boolean ro) {
        txtID.setEditable(!ro);
        txtNombre.setEditable(!ro);
        txtCorreo.setEditable(!ro);
        txtTelefono.setEditable(!ro);
        txtDireccion.setEditable(!ro);
        txtDepartamento.setEditable(!ro);
        txtCargo.setEditable(!ro);
        txtJornada.setEditable(!ro);
    }

    private void cargarDatosUsuario() {
        final String sql = "SELECT id, nombre, correo, telefono, direccion, departamento, cargo, jornada FROM usuarios WHERE id = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nombre = rs.getString("nombre");
                    lblTitulo.setText("Administrador: " + nombre);

                    txtID.setText(rs.getString("id"));
                    txtNombre.setText("nombre");
                    txtCorreo.setText(rs.getString("correo"));
                    txtTelefono.setText(rs.getString("telefono"));
                    txtDireccion.setText(rs.getString("direccion"));
                    txtDepartamento.setText(rs.getString("departamento"));
                    txtCargo.setText(rs.getString("cargo"));
                    txtJornada.setText(rs.getString("jornada"));
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se encontró el usuario en la base de datos",
                            "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                }

            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar datos del usuario:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}