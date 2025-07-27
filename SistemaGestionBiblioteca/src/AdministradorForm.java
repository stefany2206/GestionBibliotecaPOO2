import javax.swing.*;

public class AdministradorForm extends JFrame {
    private JPanel lblTitulo;
    private JButton btnGestionUsuarios;
    private JButton btnGestionLibros;
    private JButton btnVerReportes;
    private JButton btnSalir;
    private JPanel panelAdmin;
    private JButton btnVerInfo;
    private JButton btnGestionBibliotecario;
    private JButton btnGestionAdministrador;
    private JButton btnVerUsuarios;

    private final String idUsuario;

    public AdministradorForm(String idUsuario) {
        this.idUsuario = idUsuario;
        setContentPane(panelAdmin);
        setTitle("Panel de Administración");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        btnVerInfo.addActionListener(e -> new InfoUserAdministrador(idUsuario));
        btnVerUsuarios.addActionListener(e -> new TodosUsuarios());
        btnGestionUsuarios.addActionListener(e -> new GestionUsuariosForm());
        btnGestionBibliotecario.addActionListener(e -> new GestionUsuarioBibliotecaForm());
        btnGestionAdministrador.addActionListener(e -> new GestionUsuarioAdministradorForm());
        btnVerReportes.addActionListener(e -> new ReportesForm());
        btnGestionLibros.addActionListener(e -> new DB_BibliotecaForm());

        btnSalir.addActionListener(e -> dispose());
    }
}
