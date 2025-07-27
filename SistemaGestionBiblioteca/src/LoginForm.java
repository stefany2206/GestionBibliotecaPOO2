import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.*;
import java.util.Objects;

public class LoginForm extends JFrame {
    private JPanel panelPrincipal;
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JComboBox<String> comboTipoUsuario;
    private JButton btnIniciarSesion;
    private JButton btnRegistrar;
    private JLabel lblUsuario;
    private JLabel lblContrasena;
    private JLabel lblTipoUsuario;
    private JLabel lblLogo;

    public LoginForm() {
        setContentPane(panelPrincipal);
        setTitle("Sistema de Gestión de Biblioteca - Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // logo
        try {
            ImageIcon originalIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/login.png")));
            Image scaledImage = originalIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.err.println("Error cargando el logo: " + e.getMessage());
        }

        comboTipoUsuario.addItem("Estudiante");
        comboTipoUsuario.addItem("Bibliotecario");
        comboTipoUsuario.addItem("Administrador");

        btnIniciarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = txtUsuario.getText().trim();
                String contrasena = new String(txtContrasena.getPassword()).trim();
                String tipo = (String) comboTipoUsuario.getSelectedItem();

                if (usuario.isEmpty() || contrasena.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Ingrese usuario y contraseña");
                    return;
                }

                try {
                    Connection conn = ConexionBD.conectar();
                    String sql = "SELECT * FROM usuarios WHERE id = ? AND clave = ? AND rol = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, usuario);
                    ps.setString(2, contrasena);
                    ps.setString(3, tipo);

                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso como " + usuario);
                        dispose();

                        switch (tipo) {
                            case "Estudiante" -> new UsuarioForm(usuario);
                            case "Bibliotecario" -> new BibliotecarioForm(usuario);
                            case "Administrador" -> new AdministradorForm(usuario);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Credenciales incorrectas o rol inválido.");
                    }

                    conn.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos:\n" + ex.getMessage());
                }
            }
        });

        btnRegistrar.addActionListener(e -> new RegistroUsuarioForm());

        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginForm();
    }
}




