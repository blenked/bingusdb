package repository;

import database.DatabaseConnection;
import entities.Notas;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotasRepository {

    private static final String SQL_LANCAR = "INSERT INTO notas (ra, id_disciplina, id_tipo_prova, nota) VALUES (?, ?, ?, ?)";
    private static final String SQL_NOTA_EXISTE = "SELECT 1 FROM notas WHERE ra = ? AND id_disciplina = ? AND id_tipo_prova = ?";
    private static final String SQL_CONSULTAR = """
            SELECT a.nome, a.ra, d.nome_disciplina, tp.nome_prova, n.nota
            FROM notas n
            JOIN alunos a ON a.ra = n.ra
            JOIN disciplinas d ON d.id_disciplina = n.id_disciplina
            JOIN tipos_provas tp ON tp.id_tipo_prova = n.id_tipo_prova
            WHERE a.ra = ? AND d.id_disciplina = ? AND tp.id_tipo_prova = ?
            """;
    private static final String SQL_BOLETIM = """
            SELECT d.nome_disciplina, tp.nome_prova, n.nota
            FROM notas n
            JOIN disciplinas d ON d.id_disciplina = n.id_disciplina
            JOIN tipos_provas tp ON tp.id_tipo_prova = n.id_tipo_prova
            WHERE n.ra = ?
            ORDER BY d.nome_disciplina, tp.nome_prova
            """;

    public void lancarNota(Notas nota) throws SQLException {
        try (Connection conexao = DatabaseConnection.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(SQL_LANCAR)) {
            stmt.setString(1, nota.getRa());
            stmt.setInt(2, nota.getIdDisciplina());
            stmt.setInt(3, nota.getIdTipoProva());
            stmt.setBigDecimal(4, BigDecimal.valueOf(nota.getNota()));
            stmt.executeUpdate();
        }
    }

    public boolean notaJaExiste(String ra, int idDisciplina, int idTipoProva) throws SQLException {
        try (Connection conexao = DatabaseConnection.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(SQL_NOTA_EXISTE)) {
            stmt.setString(1, ra);
            stmt.setInt(2, idDisciplina);
            stmt.setInt(3, idTipoProva);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public ConsultaNotaResultado consultarNota(String ra, int idDisciplina, int idTipoProva) throws SQLException {
        try (Connection conexao = DatabaseConnection.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(SQL_CONSULTAR)) {
            stmt.setString(1, ra);
            stmt.setInt(2, idDisciplina);
            stmt.setInt(3, idTipoProva);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return new ConsultaNotaResultado(
                        rs.getString("nome"),
                        rs.getString("ra"),
                        rs.getString("nome_disciplina"),
                        rs.getString("nome_prova"),
                        rs.getBigDecimal("nota").doubleValue());
            }
        }
    }

    public List<RegistroBoletim> listarBoletim(String ra) throws SQLException {
        List<RegistroBoletim> registros = new ArrayList<>();
        try (Connection conexao = DatabaseConnection.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(SQL_BOLETIM)) {
            stmt.setString(1, ra);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    registros.add(new RegistroBoletim(
                            rs.getString("nome_disciplina"),
                            rs.getString("nome_prova"),
                            rs.getBigDecimal("nota").doubleValue()));
                }
            }
        }
        return registros;
    }
}