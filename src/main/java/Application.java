import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Application {
    static void main() {
        String sql = "SELECT * FROM alunos";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            var metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                StringBuilder row = new StringBuilder();
                for (int i = 1; i <= columnCount; i++) {
                    if (i > 1) {
                        row.append(" | ");
                    }
                    row.append(metaData.getColumnLabel(i)).append("=").append(resultSet.getString(i));
                }
                System.out.println(row);
            }
        } catch (SQLException e) {
            System.err.println("Falha ao consultar alunos: " + e.getMessage());
        }
    }
}