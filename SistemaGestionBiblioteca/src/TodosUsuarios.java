import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TodosUsuarios extends JFrame {

    private static final String ROL_ADMIN = "Administrador";
    private static final String ROL_EST = "Estudiante";
    private static final String ROL_BIB = "Bibliotecario";

    // Superset para el botón Mostrar todos
    private static final String[] COLS_ALL = {
            "ID", "Nombre", "Correo", "Teléfono", "Dirección", "Rol",
            "Carrera", "Código Institucional", "Año Ingreso",
            "Departamento", "Cargo", "Jornada",
            "Horario Laboral"
    };

    private JPanel panelTodosUsuarios;
    private JComboBox<String> comboRol;
    private JTable tableUsuariosRol;
    private JButton btnMostrarTodos;

    private DefaultTableModel modelo;

    public TodosUsuarios() {
        setContentPane(panelTodosUsuarios);
        setTitle("Usuarios por Rol");
        setSize(1000, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        comboRol.addItem(ROL_ADMIN);
        comboRol.addItem(ROL_EST);
        comboRol.addItem(ROL_BIB);

        modelo = new DefaultTableModel();
        tableUsuariosRol.setModel(modelo);

        comboRol.addActionListener(e -> cargarPorRol());
        btnMostrarTodos.addActionListener(e -> cargarTodos());

        // Carga inicial
        comboRol.setSelectedItem(ROL_EST);
        cargarPorRol();

        setVisible(true);
    }

    private void cargarPorRol() {
        String rol = comboRol.getSelectedItem().toString();

        try {
            List<Usuario> usuarios = getUsuariosPorRol(rol);

            if (ROL_EST.equals(rol)) {
                setColumns(Estudiante.COLS);
                clearRows();
                for (Usuario u : usuarios) {
                    Estudiante e = (Estudiante) u;
                    modelo.addRow(e.toRow());
                }
            } else if (ROL_ADMIN.equals(rol)) {
                setColumns(Administrador.COLS);
                clearRows();
                for (Usuario u : usuarios) {
                    Administrador a = (Administrador) u;
                    modelo.addRow(a.toRow());
                }
            } else if (ROL_BIB.equals(rol)) {
                setColumns(Bibliotecario.COLS);
                clearRows();
                for (Usuario u : usuarios) {
                    Bibliotecario b = (Bibliotecario) u;
                    modelo.addRow(b.toRow());
                }
            }
        } catch (SQLException ex) {
            showError(ex);
        }
    }

    private void cargarTodos() {
        try {
            List<Usuario> usuarios = getTodos();

            setColumns(COLS_ALL);
            clearRows();

            for (Usuario u : usuarios) {
                modelo.addRow(toRowAll(u));
            }
        } catch (SQLException ex) {
            showError(ex);
        }
    }


    private List<Usuario> getUsuariosPorRol(String rol) throws SQLException {
        String sql = """
                SELECT id, nombre, correo, telefono, direccion, rol,
                       carrera, codigo_institucional, anio_ingreso,
                       departamento, cargo, jornada,
                       horario_laboral
                FROM usuarios
                WHERE rol = ?
                ORDER BY nombre
                """;
        List<Usuario> list = new ArrayList<>();

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, rol);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(UsuarioFactory.fromResultSet(rs));
                }
            }
        }
        return list;
    }

    private List<Usuario> getTodos() throws SQLException {
        String sql = """
                SELECT id, nombre, correo, telefono, direccion, rol,
                       carrera, codigo_institucional, anio_ingreso,
                       departamento, cargo, jornada,
                       horario_laboral
                FROM usuarios
                ORDER BY rol, nombre
                """;
        List<Usuario> list = new ArrayList<>();

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(UsuarioFactory.fromResultSet(rs));
            }
        }
        return list;
    }

    /* ===================== Helpers UI ===================== */

    private void setColumns(String... cols) {
        modelo.setColumnIdentifiers(cols);
        clearRows();
    }

    private void clearRows() {
        modelo.setRowCount(0);
    }

    private void showError(SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "BD", JOptionPane.ERROR_MESSAGE);
    }

    private Object[] toRowAll(Usuario u) {
        // Alinea los datos del usuario al superset de columnas COLS_ALL
        if (u instanceof Estudiante e) {
            return new Object[]{
                    e.id, e.nombre, e.correo, e.telefono, e.direccion, e.rol,
                    e.getCarrera(), e.getCodigoInstitucional(), e.getAnioIngreso(),
                    "", "", "",
                    ""
            };
        } else if (u instanceof Administrador a) {
            return new Object[]{
                    a.id, a.nombre, a.correo, a.telefono, a.direccion, a.rol,
                    "", "", "",
                    a.getDepartamento(), a.getCargo(), a.getJornada(),
                    ""
            };
        } else if (u instanceof Bibliotecario b) {
            return new Object[]{
                    b.id, b.nombre, b.correo, b.telefono, b.direccion, b.rol,
                    "", "", "",
                    "", "", "",
                    b.getHorarioLaboral()
            };
        } else {
            return new Object[]{
                    u.id, u.nombre, u.correo, u.telefono, u.direccion, u.rol,
                    "", "", "", "", "", "", ""
            };
        }
    }


    private static class Usuario {
        protected String id;
        protected String nombre;
        protected String correo;
        protected String telefono;
        protected String direccion;
        protected String rol;

        public Usuario(String id, String nombre, String correo, String telefono, String direccion, String rol) {
            this.id = id;
            this.nombre = nombre;
            this.correo = correo;
            this.telefono = telefono;
            this.direccion = direccion;
            this.rol = rol;
        }
    }

    private static class Estudiante extends Usuario {
        static final String[] COLS = {
                "ID", "Nombre", "Correo", "Teléfono", "Dirección", "Rol",
                "Carrera", "Código Institucional", "Año Ingreso"
        };

        private final String carrera;
        private final String codigoInstitucional;
        private final String anioIngreso;

        public Estudiante(String id, String nombre, String correo, String telefono, String direccion,
                          String carrera, String codigoInstitucional, String anioIngreso) {
            super(id, nombre, correo, telefono, direccion, ROL_EST);
            this.carrera = carrera;
            this.codigoInstitucional = codigoInstitucional;
            this.anioIngreso = anioIngreso;
        }

        public String getCarrera() { return carrera; }
        public String getCodigoInstitucional() { return codigoInstitucional; }
        public String getAnioIngreso() { return anioIngreso; }

        public Object[] toRow() {
            return new Object[]{id, nombre, correo, telefono, direccion, rol,
                    carrera, codigoInstitucional, anioIngreso};
        }
    }

    private static class Administrador extends Usuario {
        static final String[] COLS = {
                "ID", "Nombre", "Correo", "Teléfono", "Dirección", "Rol",
                "Departamento", "Cargo", "Jornada"
        };

        private final String departamento;
        private final String cargo;
        private final String jornada;

        public Administrador(String id, String nombre, String correo, String telefono, String direccion,
                             String departamento, String cargo, String jornada) {
            super(id, nombre, correo, telefono, direccion, ROL_ADMIN);
            this.departamento = departamento;
            this.cargo = cargo;
            this.jornada = jornada;
        }

        public String getDepartamento() { return departamento; }
        public String getCargo() { return cargo; }
        public String getJornada() { return jornada; }

        public Object[] toRow() {
            return new Object[]{id, nombre, correo, telefono, direccion, rol,
                    departamento, cargo, jornada};
        }
    }

    private static class Bibliotecario extends Usuario {
        static final String[] COLS = {
                "ID", "Nombre", "Correo", "Teléfono", "Dirección", "Rol",
                "Horario Laboral"
        };

        private final String horarioLaboral;

        public Bibliotecario(String id, String nombre, String correo, String telefono, String direccion,
                             String horarioLaboral) {
            super(id, nombre, correo, telefono, direccion, ROL_BIB);
            this.horarioLaboral = horarioLaboral;
        }

        public String getHorarioLaboral() { return horarioLaboral; }

        public Object[] toRow() {
            return new Object[]{id, nombre, correo, telefono, direccion, rol, horarioLaboral};
        }
    }


    private static final class UsuarioFactory {
        private UsuarioFactory() {}

        static Usuario fromResultSet(ResultSet rs) throws SQLException {
            String rol = rs.getString("rol");
            if (ROL_EST.equals(rol)) {
                return new Estudiante(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getString("carrera"),
                        rs.getString("codigo_institucional"),
                        rs.getString("anio_ingreso")
                );
            } else if (ROL_ADMIN.equals(rol)) {
                return new Administrador(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getString("departamento"),
                        rs.getString("cargo"),
                        rs.getString("jornada")
                );
            } else if (ROL_BIB.equals(rol)) {
                return new Bibliotecario(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getString("horario_laboral")
                );
            } else {
                // fallback
                return new Usuario(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rol
                );
            }
        }
    }
}



