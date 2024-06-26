package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import BD.conexion;
import Model.UserModel;
import at.favre.lib.crypto.bcrypt.BCrypt;

public class UsuarioDAO {
    private static final String INSERT_USUARIO_SQL = "INSERT INTO Users (nombre, apellido, usuario, correo, clave) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_USUARIOS = "SELECT * FROM Users";
      private static final String UPDATE_USUARIO_SQL = "UPDATE Users SET nombre=?, apellido=?, usuario=? WHERE id=?";
      private static final String UPDATE_PASSWORD_USUARIO_SQL = "UPDATE Users SET clave=? WHERE id=?";
    private static final String DELETE_USUARIO_SQL = "DELETE FROM Users WHERE id=?";
    
    
    public void crearUsuario(UserModel usuario) {
        try (Connection connection = conexion.conectar();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USUARIO_SQL)) {
            preparedStatement.setString(1, usuario.getNombre());
            preparedStatement.setString(2, usuario.getApellido());
            preparedStatement.setString(3, usuario.getUsuario());
            preparedStatement.setString(4, usuario.getCorreo());
            preparedStatement.setString(5, BCrypt.withDefaults().hashToString(10, usuario.getClave().toCharArray()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // Manejar excepción
            e.printStackTrace();
        }
    }
    
    // Método para obtener la contraseña de un usuario por su ID
    public String obtenerClaveUsuarioPorId(int id) {
        String clave = null;
        String sql = "SELECT clave FROM Users WHERE id = ?";
        
        try (Connection connection = conexion.conectar();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    clave = rs.getString("clave");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return clave;
    }
    
    // Método para obtener la contraseña de un usuario por su ID
    public UserModel obtenerPorUsuario(String user) {
        UserModel userModel = new UserModel(0, "", "", "", "", "");
        String sql = "SELECT * FROM Users WHERE usuario = ?";
        
        try (Connection connection = conexion.conectar();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                                        
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    String apellido = rs.getString("apellido");
                    String usuario = rs.getString("usuario");
                    String correo = rs.getString("correo");
                    String clave = rs.getString("clave");
                    userModel = new UserModel(id, nombre, apellido, correo, usuario, clave);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return userModel;
    }

    public List<UserModel> obtenerTodosUsuarios() {
        List<UserModel> usuarios = new ArrayList<>();
        try (Connection connection = conexion.conectar();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USUARIOS);
            ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String usuario = rs.getString("usuario");
                String correo = rs.getString("correo");
                String clave = rs.getString("clave");
                usuarios.add(new UserModel(id, nombre, apellido, correo, usuario, clave));
            }
        } catch (SQLException e) {
            // Manejar excepción
            e.printStackTrace();
        }
        return usuarios;
    }
    
    // Método para actualizar un usuario en la base de datos
    public void actualizarUsuario(UserModel usuario) {
        try (Connection connection = conexion.conectar();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USUARIO_SQL)) {
            preparedStatement.setString(1, usuario.getNombre());
            preparedStatement.setString(2, usuario.getApellido());
            preparedStatement.setString(3, usuario.getUsuario());
            preparedStatement.setInt(4, usuario.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // Manejar excepción
            e.printStackTrace();
        }
    }
    
     // Método para actualizar un usuario en la base de datos
    public void actualizarPassword(int id, String newPassword) {
        try (Connection connection = conexion.conectar();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PASSWORD_USUARIO_SQL)) {            
            preparedStatement.setString(1, BCrypt.withDefaults().hashToString(10, newPassword.toCharArray()));            
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // Manejar excepción
            e.printStackTrace();
        }
    }

    // Método para borrar un usuario de la base de datos
    public void borrarUsuario(int id) {
        try (Connection connection = conexion.conectar();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USUARIO_SQL)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // Manejar excepción
            e.printStackTrace();
        }
    }
    
}
