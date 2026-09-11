package repository;

import database.DatabaseConnection;
import entities.TipoProva;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TipoProvaRepository {

    private static final String SQL_LISTAR = "SELECT id_tipo_prova, nome_prova FROM tipos_provas ORDER BY nome_prova";

    public List<TipoProva> listarTodos() throws SQLException {
        List<TipoProva> tipos = new ArrayList<>();
        try (Connection conexao = DatabaseConnection.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(SQL_LISTAR);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                tipos.add(new TipoProva(
                        rs.getInt("id_tipo_prova"),
                        rs.getString("nome_prova")));
            }
        }
        return tipos;
    }
}