package repository;

import database.DatabaseConnection;
import entities.Aluno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlunoRepository {

    private static final String SQL_CADASTRAR = "INSERT INTO alunos (ra, nome, data_nascimento, rg) VALUES (?, ?, ?, ?)";
    private static final String SQL_RA_EXISTE = "SELECT 1 FROM alunos WHERE ra = ?";
    private static final String SQL_RG_EXISTE = "SELECT 1 FROM alunos WHERE rg = ?";
    private static final String SQL_LISTAR = "SELECT ra, nome, data_nascimento, rg FROM alunos ORDER BY nome";

    public void cadastrar(Aluno aluno) throws SQLException {
        try (Connection conexao = DatabaseConnection.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(SQL_CADASTRAR)) {
            stmt.setString(1, aluno.getRa());
            stmt.setString(2, aluno.getNome());
            stmt.setObject(3, aluno.getDataNascimento());
            stmt.setString(4, aluno.getRg());
            stmt.executeUpdate();
        }
    }

    public boolean raJaExiste(String ra) throws SQLException {
        return verificarExiste(SQL_RA_EXISTE, ra);
    }

    public boolean rgJaExiste(String rg) throws SQLException {
        return verificarExiste(SQL_RG_EXISTE, rg);
    }

    public List<Aluno> listarTodos() throws SQLException {
        List<Aluno> alunos = new ArrayList<>();
        try (Connection conexao = DatabaseConnection.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(SQL_LISTAR);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                alunos.add(new Aluno(
                        rs.getString("ra"),
                        rs.getString("nome"),
                        rs.getDate("data_nascimento").toLocalDate(),
                        rs.getString("rg")));
            }
        }
        return alunos;
    }

    private boolean verificarExiste(String sql, String valor) throws SQLException {
        try (Connection conexao = DatabaseConnection.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, valor);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}