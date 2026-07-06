package com.biblioteca.dao;

import com.biblioteca.config.DatabaseConfig;
import com.biblioteca.model.Emprestimo;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class EmprestimoDAOImpl implements EmprestimoDAO {

    private final DatabaseConfig databaseConfig;

    public EmprestimoDAOImpl(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public Emprestimo save(Emprestimo emprestimo) {
        String sql = "INSERT INTO emprestimo (aluno_id, data_emprestimo, status) VALUES (?, ?, ?)";
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, emprestimo.getAlunoId());
            ps.setString(2, emprestimo.getDataEmprestimo().toString());
            ps.setString(3, emprestimo.getStatus().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    emprestimo.setId(rs.getInt(1));
                }
            }
            return emprestimo;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar emprestimo", e);
        }
    }

    @Override
    public Optional<Emprestimo> findById(Integer id) {
        String sql = "SELECT * FROM emprestimo WHERE id = ?";
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar emprestimo por id", e);
        }
    }

    @Override
    public List<Emprestimo> findAtivosPorAluno(Integer alunoId) {
        String sql = "SELECT * FROM emprestimo WHERE aluno_id = ? AND status = 'ATIVO'";
        List<Emprestimo> lista = new ArrayList<>();
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, alunoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar emprestimos ativos do aluno", e);
        }
    }

    @Override
    public List<Emprestimo> findAll() {
        String sql = "SELECT * FROM emprestimo ORDER BY data_emprestimo DESC";
        List<Emprestimo> lista = new ArrayList<>();
        try (Connection conn = databaseConfig.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar emprestimos", e);
        }
    }

    @Override
    public void atualizarStatus(Integer id, Emprestimo.Status status) {
        String sql = "UPDATE emprestimo SET status = ? WHERE id = ?";
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar status do emprestimo", e);
        }
    }

    private Emprestimo mapRow(ResultSet rs) throws SQLException {
        return new Emprestimo(
                rs.getInt("id"),
                rs.getInt("aluno_id"),
                LocalDate.parse(rs.getString("data_emprestimo")),
                Emprestimo.Status.valueOf(rs.getString("status"))
        );
    }
}
