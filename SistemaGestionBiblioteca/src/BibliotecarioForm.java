import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class BibliotecarioForm extends JFrame {
    private JPanel panelBibliotecario;
    private JTextField txtBuscarISBN;
    private JButton btnBuscarLibro;
    private JTextArea txtInfoLibro;
    private JButton btnMarcarDevuelto;
    private JButton btnMarcarPerdido;
    private JTable tablaPrestamos;
    private JScrollPane scrollTabla;
    private JScrollPane scrollInfo;
    private JLabel lblBusqueda;
    private JButton btnGestionarLibros;
    private JButton btnVerInfo;

    private String idUsuario;


    public BibliotecarioForm(String idUsuario) {
        this.idUsuario = idUsuario;
        setContentPane(panelBibliotecario);
        setTitle("Panel del Bibliotecario");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        btnBuscarLibro.addActionListener(e -> buscarLibro());
        btnMarcarDevuelto.addActionListener(e -> marcarComoDevuelto());
        btnMarcarPerdido.addActionListener(e -> marcarComoPerdido());
        btnGestionarLibros.addActionListener(e -> new DB_BibliotecaForm());
        btnVerInfo.addActionListener(e -> new InfoUserBibliotecario(idUsuario));
    }

    private void buscarLibro() {
        String isbn = txtBuscarISBN.getText().trim();
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese un ISBN válido");
            return;
        }

        try (Connection conn = ConexionBD.conectar()) {
            String sql = "SELECT * FROM libros WHERE isbn = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String info = "Título: " + rs.getString("titulo") +
                        "\nAutor: " + rs.getString("autor") +
                        "\nAño: " + rs.getString("anio_publicacion") +
                        "\nEstado Reserva: " + rs.getString("estado_reserva");
                txtInfoLibro.setText(info);
                cargarHistorialPrestamos(isbn);
            } else {
                JOptionPane.showMessageDialog(null, "Libro no encontrado");
                txtInfoLibro.setText("");
                limpiarTabla();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar libro: " + ex.getMessage());
        }
    }

    private void cargarHistorialPrestamos(String isbn) {
        try (Connection conn = ConexionBD.conectar()) {
            String sql = "SELECT id_usuario, isbn, fecha_prestamo, fecha_devolucion, estado FROM prestamos WHERE isbn = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID Usuario");
            model.addColumn("ISBN");
            model.addColumn("Fecha Préstamo");
            model.addColumn("Fecha Devolución");
            model.addColumn("Estado");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("id_usuario"),
                        rs.getString("isbn"),
                        rs.getDate("fecha_prestamo"),
                        rs.getDate("fecha_devolucion"),
                        rs.getString("estado")
                });
            }
            tablaPrestamos.setModel(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar historial: " + ex.getMessage());
        }
    }

    private void marcarComoDevuelto() {
        String isbn = txtBuscarISBN.getText().trim();
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese el ISBN del libro");
            return;
        }

        try (Connection conn = ConexionBD.conectar()) {
            String sql = "SELECT id_usuario, fecha_devolucion FROM prestamos WHERE isbn = ? AND estado = 'En curso' ORDER BY id DESC LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String idUsuario = rs.getString("id_usuario");
                Date fechaDevolucion = rs.getDate("fecha_devolucion");
                Date hoy = new Date(System.currentTimeMillis());

                long diasRetraso = (hoy.getTime() - fechaDevolucion.getTime()) / (1000 * 60 * 60 * 24);
                double multa = diasRetraso > 0 ? diasRetraso * 1.0 : 0.0;

                String updateEstado = "UPDATE prestamos SET estado = 'Devuelto' WHERE isbn = ? AND estado = 'En curso'";
                PreparedStatement updatePs = conn.prepareStatement(updateEstado);
                updatePs.setString(1, isbn);
                updatePs.executeUpdate();

                // Actualizar estado_reserva
                String updateReserva = "UPDATE libros SET estado_reserva = 'Disponible' WHERE isbn = ?";
                PreparedStatement psReserva = conn.prepareStatement(updateReserva);
                psReserva.setString(1, isbn);
                psReserva.executeUpdate();

                if (multa > 0) {
                    String checkMulta = "SELECT COUNT(*) FROM multas WHERE id_usuario = ? AND motivo = 'Devolución tardía' AND fecha = CURDATE()";
                    PreparedStatement checkPs = conn.prepareStatement(checkMulta);
                    checkPs.setString(1, idUsuario);
                    ResultSet rsCheck = checkPs.executeQuery();
                    rsCheck.next();
                    int count = rsCheck.getInt(1);

                    if (count == 0) {
                        String insertMulta = "INSERT INTO multas (id_usuario, monto, motivo, fecha) VALUES (?, ?, 'Devolución tardía', CURDATE())";
                        PreparedStatement multaPs = conn.prepareStatement(insertMulta);
                        multaPs.setString(1, idUsuario);
                        multaPs.setDouble(2, multa);
                        multaPs.executeUpdate();
                    }

                    JOptionPane.showMessageDialog(null, "Libro devuelto. Multa por retraso: S/ " + multa);
                } else {
                    JOptionPane.showMessageDialog(null, "Libro devuelto a tiempo.");
                }

                cargarHistorialPrestamos(isbn);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró préstamo activo para este libro.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al marcar como devuelto: " + ex.getMessage());
        }
    }

    private void marcarComoPerdido() {
        String isbn = txtBuscarISBN.getText().trim();
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese el ISBN del libro");
            return;
        }

        try (Connection conn = ConexionBD.conectar()) {
            String sql = "SELECT id_usuario FROM prestamos WHERE isbn = ? AND estado = 'En curso' ORDER BY id DESC LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String idUsuario = rs.getString("id_usuario");

                String update = "UPDATE prestamos SET estado = 'Perdido' WHERE isbn = ? AND estado = 'En curso'";
                PreparedStatement updatePs = conn.prepareStatement(update);
                updatePs.setString(1, isbn);
                updatePs.executeUpdate();

                // Actualizar estado_reserva
                String updateReserva = "UPDATE libros SET estado_reserva = 'Disponible' WHERE isbn = ?";
                PreparedStatement psReserva = conn.prepareStatement(updateReserva);
                psReserva.setString(1, isbn);
                psReserva.executeUpdate();

                String insertMulta = "INSERT INTO multas (id_usuario, monto, motivo, fecha) VALUES (?, ?, 'Libro perdido', CURDATE())";
                PreparedStatement multaPs = conn.prepareStatement(insertMulta);
                multaPs.setString(1, idUsuario);
                multaPs.setDouble(2, 30.00);
                multaPs.executeUpdate();

                JOptionPane.showMessageDialog(null, "Libro marcado como perdido y multa registrada.");
                cargarHistorialPrestamos(isbn);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró préstamo activo para este libro.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al marcar como perdido: " + ex.getMessage());
        }
    }

    private void limpiarTabla() {
        tablaPrestamos.setModel(new DefaultTableModel());
    }
}





