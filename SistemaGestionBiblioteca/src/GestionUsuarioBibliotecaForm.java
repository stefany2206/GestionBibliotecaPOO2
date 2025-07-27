import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class GestionUsuarioBibliotecaForm extends JFrame {

    private static final String ROL_BIBLIOTECARIO = "Bibliotecario";

    private JPanel PanelBibliotecario;
    private JTable tableUsuarios;
    private JLabel lblNombre;
    private JLabel lblID;
    private JLabel lblCorreo;
    private JLabel lblContrasena;
    private JLabel lblTelefono;
    private JLabel lblDireccion;
    private JLabel lblHorarioLaboral;
    private JTextField txtNombre;
    private JTextField txtID;
    private JTextField txtCorreo;
    private JPasswordField txtClave;
    private JTextField txtTelefono;
    private JTextField txtDireccion;
    private JTextField txtHorarioLaboral;
    private JButton btnAgregar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JTextField txtBuscar;
    private JButton btnBuscar;

    private DefaultTableModel modelo;

    public GestionUsuarioBibliotecaForm() {
        setContentPane(PanelBibliotecario);
        setTitle("Gestión de Bibliotecario");
        setSize(1000, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Columnas solo para bibliotecarios
        modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[]{
                "Nombre", "ID", "Correo", "Teléfono", "Dirección",
                "Carrera", "Horario Laboral"
        });
        tableUsuarios.setModel(modelo);

        cargarBibliotecarios();

        btnAgregar.addActionListener(e -> agregar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnBuscar.addActionListener(e -> buscar());

        tableUsuarios.getSelectionModel().addListSelectionListener(e -> {
            int fila = tableUsuarios.getSelectedRow();
            if (fila >= 0) {
                txtNombre.setText(modelo.getValueAt(fila, 0).toString());
                txtID.setText(modelo.getValueAt(fila, 1).toString());
                txtCorreo.setText(modelo.getValueAt(fila, 2).toString());
                txtTelefono.setText(modelo.getValueAt(fila, 3).toString());
                txtDireccion.setText(modelo.getValueAt(fila, 4).toString());
                txtHorarioLaboral.setText(modelo.getValueAt(fila, 5).toString());
                txtClave.setText(modelo.getValueAt(fila, 6).toString());
            }
        });

        setVisible(true);
    }

    private void agregar() {
        String nombre = txtNombre.getText().trim();
        String id = txtID.getText().trim();
        String correo = txtCorreo.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String horario_laboral = txtHorarioLaboral.getText().trim();
        String clave = new String(txtClave.getPassword()).trim();

        if (nombre.isEmpty() || id.isEmpty() || correo.isEmpty() || telefono.isEmpty() ||
                direccion.isEmpty() || clave.isEmpty() || horario_laboral.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }

        String sql = "INSERT INTO usuarios " +
                "(id, nombre, correo, telefono, direccion, rol, clave, horario_laboral) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.setString(2, nombre);
            ps.setString(3, correo);
            ps.setString(4, telefono);
            ps.setString(5, direccion);
            ps.setString(6, ROL_BIBLIOTECARIO); // forzado
            ps.setString(7, clave);
            ps.setString(8, horario_laboral);

            ps.executeUpdate();

            modelo.addRow(new String[]{
                    nombre, id, correo, telefono, direccion,
                    horario_laboral, clave
            });
            limpiarCampos();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al insertar: " + ex.getMessage());
        }
    }

    private void actualizar() {
        int fila = tableUsuarios.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una fila para actualizar.");
            return;
        }

        String nombre = txtNombre.getText().trim();
        String id = txtID.getText().trim();
        String correo = txtCorreo.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String horario_laboral = txtHorarioLaboral.getText().trim();
        String clave = new String(txtClave.getPassword()).trim();

        if (nombre.isEmpty() || id.isEmpty() || correo.isEmpty() || telefono.isEmpty() ||
                direccion.isEmpty() || clave.isEmpty() || horario_laboral.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }

        String sql = "UPDATE usuarios SET nombre=?, correo=?, telefono=?, direccion=?, " +
                "rol=?, clave=?, horario_laboral=? WHERE id=? AND rol=?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, correo);
            ps.setString(3, telefono);
            ps.setString(4, direccion);
            ps.setString(5, ROL_BIBLIOTECARIO); // forzado
            ps.setString(6, clave);
            ps.setString(7, horario_laboral);
            ps.setString(8, id);
            ps.setString(9, ROL_BIBLIOTECARIO);

            ps.executeUpdate();

            modelo.setValueAt(nombre, fila, 0);
            modelo.setValueAt(id, fila, 1);
            modelo.setValueAt(correo, fila, 2);
            modelo.setValueAt(telefono, fila, 3);
            modelo.setValueAt(direccion, fila, 4);
            modelo.setValueAt(horario_laboral, fila, 5);
            modelo.setValueAt(clave, fila, 6);

            limpiarCampos();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage());
        }
    }

    private void eliminar() {
        int fila = tableUsuarios.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una fila para eliminar.");
            return;
        }

        String id = modelo.getValueAt(fila, 1).toString();

        String sql = "DELETE FROM usuarios WHERE id=? AND rol=?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.setString(2, ROL_BIBLIOTECARIO);
            ps.executeUpdate();

            modelo.removeRow(fila);
            limpiarCampos();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
        }
    }

    private void buscar() {
        String idBuscar = txtBuscar.getText().trim().toLowerCase();
        if (idBuscar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID.");
            return;
        }

        // La tabla ya contiene solo estudiantes; buscamos ahí
        for (int i = 0; i < modelo.getRowCount(); i++) {
            String idTabla = modelo.getValueAt(i, 1).toString().toLowerCase();
            if (idTabla.equals(idBuscar)) {
                tableUsuarios.setRowSelectionInterval(i, i);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "ID no encontrado (solo bibliotecarios).");
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtID.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        txtHorarioLaboral.setText("");
        txtClave.setText("");
        txtBuscar.setText("");
        tableUsuarios.clearSelection();
    }

    private void cargarBibliotecarios() {
        modelo.setRowCount(0);
        String sql = "SELECT id, nombre, correo, telefono, direccion, clave, horario_laboral " +
                "FROM usuarios WHERE rol = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ROL_BIBLIOTECARIO);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    modelo.addRow(new String[]{
                            rs.getString("nombre"),
                            rs.getString("id"),
                            rs.getString("correo"),
                            rs.getString("telefono"),
                            rs.getString("direccion"),
                            rs.getString("horario_laboral"),
                            rs.getString("clave")
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar estudiantes: " + ex.getMessage());
        }
    }
}