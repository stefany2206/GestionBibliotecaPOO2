import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

public class UsuarioForm extends JFrame {
    private JPanel panelUsuario;
    private JButton btnConsultarCuenta;
    private JButton btnObtenerInfoLibro;
    private JButton btnVerInfo;
    private JLabel lblLogo;

    private String idUsuario;

    public UsuarioForm(String idUsuario) {
        this.idUsuario = idUsuario;

        setContentPane(panelUsuario);
        setTitle("Perfil de Usuario");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // logo
        try {
            ImageIcon originalIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/imagenUser.png")));
            Image scaledImage = originalIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.err.println("Error cargando el logo: " + e.getMessage());
        }



        btnConsultarCuenta.addActionListener(e -> new CuentaForm(idUsuario));
        btnObtenerInfoLibro.addActionListener(e -> new LibroForm(idUsuario));
        btnVerInfo.addActionListener(e -> new InfoUserEstudiante(idUsuario));
        setVisible(true);
    }
}




