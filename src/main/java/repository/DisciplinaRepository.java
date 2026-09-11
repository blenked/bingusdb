package repository;

import database.DatabaseConnection;
import entities.Disciplina;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DisciplinaRepository {

    private static final String SQL_LISTAR = "SELECT id_disciplina, nome_disciplina, carga_horaria FROM disciplinas ORDER BY nome_disciplina";

    public List<Disciplina> listarTodas() throws SQLException {
        List<Disciplina> disciplinas = new ArrayList<>();
        try (Connection conexao = DatabaseConnection.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(SQL_LISTAR);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                disciplinas.add(new Disciplina(
                        rs.getInt("id_disciplina"),
                        rs.getString("nome_disciplina"),
                        rs.getInt("carga_horaria")));
            }
        }
        return disciplinas;
    }
}