package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TesteConexao {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/coursejdbc"; // URL do banco
        String user = "root"; // Ou seu usuário MySQL
        String password = "040898b157"; // Substitua pela senha correta

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("✅ Conexão bem-sucedida!");
        } catch (SQLException e) {
            System.out.println("❌ Erro na conexão: " + e.getMessage());
        }
    }
}