package com.biblioteca.dao;

import com.biblioteca.config.DatabaseConfig;
import com.biblioteca.model.Titulo;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementacao concreta do DAO de Titulo, usando JDBC puro sobre SQLite.
 */
@Repository
public class TituloDAOImpl implements TituloDAO {

    private final DatabaseConfig databaseConfig;

    public TituloDAOImpl(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public Titulo save(Titulo titulo) {
        String sql = "INSERT INTO titulo (nome, autor, isbn, prazo_emprestimo_dias) VALUES (?, ?, ?, ?)";
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, titulo.getNome());
            ps.setString(2, titulo.getAutor());
            ps.setString(3, titulo.getIsbn());
            ps.setInt(4, titulo.getPrazoEmprestimoDias());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    titulo.setId(rs.getInt(1));
                }
            }
            return titulo;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar titulo", e);
        }
    }

    @Override
    public Optional<Titulo> findById(Integer id) {
        String sql = "SELECT * FROM titulo WHERE id = ?";
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar titulo por id", e);
        }
    }

    @Override
    public List<Titulo> findAll() {
        String sql = "SELECT * FROM titulo ORDER BY nome";
        List<Titulo> titulos = new ArrayList<>();
        try (Connection conn = databaseConfig.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                titulos.add(mapRow(rs));
            }
            return titulos;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar titulos", e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM titulo WHERE id = ?";
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover titulo", e);
        }
    }

    private Titulo mapRow(ResultSet rs) throws SQLException {
        return new Titulo(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("autor"),
                rs.getString("isbn"),
                rs.getInt("prazo_emprestimo_dias")
        );
    }
}
