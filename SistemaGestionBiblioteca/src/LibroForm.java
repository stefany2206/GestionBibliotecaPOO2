import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.LocalDate;

public class LibroForm extends JFrame {
    private JLabel txtTitulo;
    private JLabel txtAutor;
    private JLabel txtISBN;
    private JLabel txtPublicacion;
    private JButton btnFechaDevolucion;
    private JButton btnEstadoReserva;
    private JTextField txtComentario;
    private JButton btnDejarComentario;
    private JButton btnSolicitar;
    private JLabel lblTitulo;
    private JLabel lblAutor;
    private JLabel lblISBN;
    private JLabel lblPublicacion;
    private JPanel panelLibro;
    private JTextField colocaElNombreOTextField;
    private JButton buscarButton;

    private String idUsuario;

    public LibroForm(String idUsuario) {
        this.idUsuario = idUsuario;

        setContentPane(panelLibro);
        setTitle("Información del Libro");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        buscarButton.addActionListener(e -> buscarLibro());
        btnDejarComentario.addActionListener(e -> dejarComentario());
        btnSolicitar.addActionListener(e -> solicitarLibro());
        btnEstadoReserva.addActionListener(e -> verificarEstadoReserva());
        btnFechaDevolucion.addActionListener(e -> mostrarFechaDevolucion());
    }

    private void buscarLibro() {
        String isbn = colocaElNombreOTextField.getText().trim();
        try (Connection conn = ConexionBD.conectar()) {
            String sql = "SELECT * FROM libros WHERE isbn = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtTitulo.setText(rs.getString("titulo"));
                txtAutor.setText(rs.getString("autor"));
                txtISBN.setText(rs.getString("isbn"));
                txtPublicacion.setText(rs.getString("anio_publicacion"));
            } else {
                JOptionPane.showMessageDialog(null, "Libro no encontrado");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar libro: " + ex.getMessage());
        }
    }

    private void dejarComentario() {
        String isbn = colocaElNombreOTextField.getText().trim();
        String comentario = txtComentario.getText().trim();

        if (comentario.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese un comentario primero");
            return;
        }

        try (Connection conn = ConexionBD.conectar()) {
            String sql = "INSERT INTO comentarios (id_usuario, isbn, comentario, fecha) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idUsuario);
            ps.setString(2, isbn);
            ps.setString(3, comentario);
            ps.setDate(4, Date.valueOf(LocalDate.now()));
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Comentario guardado");
            txtComentario.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar comentario: " + ex.getMessage());
        }
    }

    private void solicitarLibro() {
        String isbn = colocaElNombreOTextField.getText().trim();
        try (Connection conn = ConexionBD.conectar()) {
            String estadoSql = "SELECT estado_reserva FROM libros WHERE isbn = ?";
            PreparedStatement checkPs = conn.prepareStatement(estadoSql);
            checkPs.setString(1, isbn);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next() && rs.getString("estado_reserva").equalsIgnoreCase("Disponible")) {
                LocalDate fechaPrestamo = LocalDate.now();
                LocalDate fechaDevolucion = fechaPrestamo.plusDays(15);

                String insertSql = "INSERT INTO prestamos (id_usuario, isbn, fecha_prestamo, fecha_devolucion, estado) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(insertSql);
                ps.setString(1, idUsuario);
                ps.setString(2, isbn);
                ps.setDate(3, Date.valueOf(fechaPrestamo));
                ps.setDate(4, Date.valueOf(fechaDevolucion));
                ps.setString(5, "En curso");
                ps.executeUpdate();

                String updateSql = "UPDATE libros SET estado_reserva = 'Reservado' WHERE isbn = ?";
                PreparedStatement psUpdate = conn.prepareStatement(updateSql);
                psUpdate.setString(1, isbn);
                psUpdate.executeUpdate();

                JOptionPane.showMessageDialog(null, "Libro solicitado con éxito");
            } else {
                JOptionPane.showMessageDialog(null, "El libro no está disponible");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al solicitar libro: " + ex.getMessage());
        }
    }

    private void verificarEstadoReserva() {
        String isbn = colocaElNombreOTextField.getText().trim();
        try (Connection conn = ConexionBD.conectar()) {
            String sql = "SELECT estado_reserva FROM libros WHERE isbn = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "Estado de reserva: " + rs.getString("estado_reserva"));
            } else {
                JOptionPane.showMessageDialog(null, "Libro no encontrado");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al verificar estado: " + ex.getMessage());
        }
    }

    private void mostrarFechaDevolucion() {
        String isbn = colocaElNombreOTextField.getText().trim();
        try (Connection conn = ConexionBD.conectar()) {
            String sql = "SELECT fecha_devolucion FROM prestamos WHERE id_usuario = ? AND isbn = ? ORDER BY fecha_devolucion DESC LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idUsuario);
            ps.setString(2, isbn);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "Fecha de devolución: " + rs.getDate("fecha_devolucion"));
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un préstamo activo para este libro");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al obtener fecha: " + ex.getMessage());
        }
    }
}

