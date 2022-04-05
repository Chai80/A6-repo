import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private Connection conn = null;

    public Database(String location) {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + location);

            initialize();
        } catch (SQLException e) {
            System.err.println(e.getMessage());

            System.exit(-1);
        }
    }

    private void initialize() throws SQLException {
        final String sql = "CREATE TABLE IF NOT EXISTS users ("
                + "    user_name TEXT PRIMARY KEY,"
                + "    password TEXT NOT NULL,"
                + "    user_type TEXT NOT NULL CHECK(user_type IN ('ADMIN', 'ANALYST'))"
                + ");";

        Statement s = conn.createStatement();

        s.execute(sql);
    }

    private List<String> findUser(String name) {
        List<String> result = new ArrayList<>();

        final String sql = "SELECT user_name, password, user_type "
                + "FROM users "
                + "WHERE user_name = ?";

        try {
            PreparedStatement p = conn.prepareStatement(sql);

            p.setString(1, name.toLowerCase());

            ResultSet rs = p.executeQuery();

            if (rs.next()) {
                result.add(rs.getString("user_name"));
                result.add(rs.getString("password"));
                result.add(rs.getString("user_type"));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return result;
    }

    private Integer getAdminCount() {
        final String sql = "SELECT COUNT(user_name) FROM users WHERE user_type = 'ADMIN'";

        return getUserCount(sql);
    }

    private Integer getUserCount(String sql) {
        try {
            Statement s = conn.createStatement();

            ResultSet rs = s.executeQuery(sql);

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return 0;
    }

    public User checkUser(String name, String password) {
        List<String> user = findUser(name);

        if ((user.size() == 0) || (!password.equals(user.get(1)))) {
            return null;
        }

        return new User(name, User.Type.valueOf(user.get(2).toUpperCase()));
    }

    public User createUser(String name, String password, User.Type type) {
        List<String> user = findUser(name);

        if (user.size() != 0) {
            return null;
        }

        final String sql = "INSERT INTO users(user_name, password, user_type) VALUES(?, ?, ?)";

        try {
            PreparedStatement p = conn.prepareStatement(sql);

            p.setString(1, name.toLowerCase());
            p.setString(2, password);
            p.setString(3, type.toString());

            p.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return new User(name, type);
    }

    public void updateUser(String name, String password, User.Type type) {
        List<String> user = findUser(name);

        if (user.size() != 0) {
            String sql_pass, sql_type;

            final String sql = "UPDATE users SET password = ?, user_type = ? "
                    + "WHERE user_name = ?";

            if (password != null) {
                sql_pass = password;
            } else {
                sql_pass = user.get(1);
            }

            if (type != null) {
                if ((getAdminCount() == 1) && (type == User.Type.ANALYST)
                        && (User.Type.valueOf(user.get(2).toUpperCase()) == User.Type.ADMIN)) {
                    sql_type = user.get(2);
                } else {
                    sql_type = type.toString();
                }
            } else {
                sql_type = user.get(2);
            }

            try {
                PreparedStatement p = conn.prepareStatement(sql);

                p.setString(1, sql_pass);
                p.setString(2, sql_type.toUpperCase());
                p.setString(3, name.toLowerCase());

                p.executeUpdate();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void deleteUser(String name) {
        List<String> user = findUser(name);

        if (user.size() != 0) {
            if (!((getAdminCount() == 1) && (User.Type.valueOf(user.get(2).toUpperCase()) == User.Type.ADMIN))) {
                final String sql = "DELETE FROM users WHERE user_name = ?";

                try {
                    PreparedStatement p = conn.prepareStatement(sql);

                    p.setString(1, name.toLowerCase());

                    p.executeUpdate();
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    public Integer getUserCount() {
        final String sql = "SELECT COUNT(user_name) FROM users";

        return getUserCount(sql);
    }
}
