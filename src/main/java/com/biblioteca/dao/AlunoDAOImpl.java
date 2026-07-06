package com.biblioteca.dao;

import com.biblioteca.config.DatabaseConfig;
import com.biblioteca.model.Aluno;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AlunoDAOImpl implements AlunoDAO {

    private final DatabaseConfig databaseConfig;

    public AlunoDAOImpl(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public Aluno save(Aluno aluno) {
        String sql = "INSERT INTO aluno (ra, nome, email, em_debito) VALUES (?, ?, ?, ?)";
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, aluno.getRa());
            ps.setString(2, aluno.getNome());
            ps.setString(3, aluno.getEmail());
            ps.setInt(4, aluno.isEmDebito() ? 1 : 0);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    aluno.setId(rs.getInt(1));
                }
            }
            return aluno;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar aluno", e);
        }
    }

    @Override
    public Optional<Aluno> findById(Integer id) {
        String sql = "SELECT * FROM aluno WHERE id = ?";
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar aluno por id", e);
        }
    }

    @Override
    public Optional<Aluno> findByRa(String ra) {
        String sql = "SELECT * FROM aluno WHERE ra = ?";
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ra);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar aluno por RA", e);
        }
    }

    @Override
    public List<Aluno> findAll() {
        String sql = "SELECT * FROM aluno ORDER BY nome";
        List<Aluno> lista = new ArrayList<>();
        try (Connection conn = databaseConfig.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar alunos", e);
        }
    }

    @Override
    public void updateDebito(Integer id, boolean emDebito) {
        String sql = "UPDATE aluno SET em_debito = ? WHERE id = ?";
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, emDebito ? 1 : 0);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar debito do aluno", e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM aluno WHERE id = ?";
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover aluno", e);
        }
    }

    private Aluno mapRow(ResultSet rs) throws SQLException {
        return new Aluno(
                rs.getInt("id"),
                rs.getString("ra"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getInt("em_debito") == 1
        );
    }
}
