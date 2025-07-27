import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistroUsuarioForm extends JFrame {
    private JTextField txtNombre;
    private JLabel lblNombre;
    private JLabel lblID;
    private JTextField txtID;
    private JTextField txtCorreo;
    private JLabel lblCorreo;
    private JLabel lblTelefono;
    private JLabel lblDireccion;
    private JTextField txtTelefono;
    private JTextField txtDireccion;
    private JLabel lblCarrera;
    private JLabel lblClave;
    private JPasswordField txtClave;
    private JButton btnRegistrar;
    private JTextArea txtResumen;
    private JPanel panelRegistro;
    private JTextField txtCarrera;
    private JTextField txtCodigoInstitucional;
    private JTextField txtAnioIngreso;
    private JLabel lblCodigoInstitucional;
    private JLabel lblAnioIngreso;

    public RegistroUsuarioForm() {
        setContentPane(panelRegistro);
        setTitle("Registro de Estudiante");
        setSize(400, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarEstudiante();
            }
        });
    }

    private void registrarEstudiante() {
        String nombre = txtNombre.getText().trim();
        String id = txtID.getText().trim();
        String correo = txtCorreo.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String clave = new String(txtClave.getPassword()).trim();
        String carrera = txtCarrera.getText().trim();
        String codigoInstitucional = txtCodigoInstitucional.getText().trim();
        String anioIngreso = txtAnioIngreso.getText().trim();

        // Validaciones básicas
        if (nombre.isEmpty() || id.isEmpty() || correo.isEmpty() || telefono.isEmpty() ||
                direccion.isEmpty() || clave.isEmpty() || carrera.isEmpty() ||
                codigoInstitucional.isEmpty() || anioIngreso.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos deben estar completos");
            return;
        }

        if (!correo.contains("@") || !correo.contains(".")) {
            JOptionPane.showMessageDialog(this, "Ingrese un correo válido");
            return;
        }

        // Guardar en la base de datos
        String sql = "INSERT INTO usuarios " +
                "(id, nombre, correo, clave, telefono, direccion, carrera, codigo_institucional, anio_ingreso) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.setString(2, nombre);
            ps.setString(3, correo);
            ps.setString(4, clave);
            ps.setString(5, telefono);
            ps.setString(6, direccion);
            ps.setString(7, carrera);
            ps.setString(8, codigoInstitucional);
            ps.setString(9, anioIngreso);

            ps.executeUpdate();

            txtResumen.setText("Estudiante registrado en BD:\n" +
                    "   - Nombre: " + nombre +
                    "\n   - ID: " + id +
                    "\n   - Correo: " + correo +
                    "\n   - Teléfono: " + telefono +
                    "\n   - Dirección: " + direccion +
                    "\n   - Carrera: " + carrera +
                    "\n   - Código Institucional: " + codigoInstitucional +
                    "\n   - Año Ingreso: " + anioIngreso);

            limpiarCampos();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage());
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtID.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        txtClave.setText("");
        txtCarrera.setText("");
        txtCodigoInstitucional.setText("");
        txtAnioIngreso.setText("");
    }

    public static void main(String[] args) {
        new RegistroUsuarioForm();
    }
}

