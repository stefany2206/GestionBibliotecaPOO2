import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DB_BibliotecaForm extends JFrame {
    private JTable tableLibros;
    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtISBN;
    private JTextField txtEstado;
    private JTextField txtAnio; // NUEVO CAMPO
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnAgregar;
    private JButton btnEliminar;
    private JButton btnActualizar;
    private JPanel panelDB;

    private DefaultTableModel modelo;

    public DB_BibliotecaForm() {
        setContentPane(panelDB);
        setTitle("Gestión de Libros - Biblioteca");
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[]{"ISBN", "Título", "Autor", "Año", "Estado"});
        tableLibros.setModel(modelo);

        cargarLibros();

        btnAgregar.addActionListener(e -> agregarLibro());
        btnEliminar.addActionListener(e -> eliminarLibro());
        btnActualizar.addActionListener(e -> actualizarLibro());
        btnBuscar.addActionListener(e -> buscarLibro());

        tableLibros.getSelectionModel().addListSelectionListener(e -> {
            int fila = tableLibros.getSelectedRow();
            if (fila >= 0) {
                txtISBN.setText(modelo.getValueAt(fila, 0).toString());
                txtTitulo.setText(modelo.getValueAt(fila, 1).toString());
                txtAutor.setText(modelo.getValueAt(fila, 2).toString());
                txtAnio.setText(modelo.getValueAt(fila, 3).toString()); // AÑO
                txtEstado.setText(modelo.getValueAt(fila, 4).toString());
            }
        });
    }

    private void cargarLibros() {
        modelo.setRowCount(0);
        try (Connection conn = ConexionBD.conectar()) {
            String sql = "SELECT * FROM libros";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getString("isbn"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("anio_publicacion"),
                        rs.getString("estado_reserva")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar libros:\n" + e.getMessage());
        }
    }

    private void agregarLibro() {
        try (Connection conn = ConexionBD.conectar()) {
            String sql = "INSERT INTO libros (isbn, titulo, autor, anio_publicacion, estado_reserva) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtISBN.getText());
            stmt.setString(2, txtTitulo.getText());
            stmt.setString(3, txtAutor.getText());
            stmt.setString(4, txtAnio.getText()); // Año
            stmt.setString(5, txtEstado.getText());
            stmt.executeUpdate();
            cargarLibros();
            limpiarCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar libro:\n" + e.getMessage());
        }
    }

    private void eliminarLibro() {
        int fila = tableLibros.getSelectedRow();
        if (fila >= 0) {
            String isbn = modelo.getValueAt(fila, 0).toString();
            try (Connection conn = ConexionBD.conectar()) {
                String sql = "DELETE FROM libros WHERE isbn = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, isbn);
                stmt.executeUpdate();
                cargarLibros();
                limpiarCampos();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar libro:\n" + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un libro para eliminar");
        }
    }

    private void actualizarLibro() {
        int fila = tableLibros.getSelectedRow();
        if (fila >= 0) {
            try (Connection conn = ConexionBD.conectar()) {
                String sql = "UPDATE libros SET titulo=?, autor=?, anio_publicacion=?, estado_reserva=? WHERE isbn=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, txtTitulo.getText());
                stmt.setString(2, txtAutor.getText());
                stmt.setString(3, txtAnio.getText()); // Año
                stmt.setString(4, txtEstado.getText());
                stmt.setString(5, txtISBN.getText());
                stmt.executeUpdate();
                cargarLibros();
                limpiarCampos();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al actualizar libro:\n" + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un libro para actualizar");
        }
    }

    private void buscarLibro() {
        String termino = txtBuscar.getText().toLowerCase();
        boolean encontrado = false;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            String isbn = modelo.getValueAt(i, 0).toString().toLowerCase();
            String titulo = modelo.getValueAt(i, 1).toString().toLowerCase();
            if (isbn.contains(termino) || titulo.contains(termino)) {
                tableLibros.setRowSelectionInterval(i, i);
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            JOptionPane.showMessageDialog(this, "No se encontró el libro");
        }
    }

    private void limpiarCampos() {
        txtTitulo.setText("");
        txtAutor.setText("");
        txtISBN.setText("");
        txtEstado.setText("");
        txtAnio.setText("");
    }

}
