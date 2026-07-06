package com.biblioteca.dao;

import com.biblioteca.config.DatabaseConfig;
import com.biblioteca.model.Exemplar;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ExemplarDAOImpl implements ExemplarDAO {

    private final DatabaseConfig databaseConfig;

    public ExemplarDAOImpl(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public Exemplar save(Exemplar exemplar) {
        String sql = "INSERT INTO exemplar (titulo_id, codigo_tombo, situacao) VALUES (?, ?, ?)";
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, exemplar.getTituloId());
            ps.setString(2, exemplar.getCodigoTombo());
            ps.setString(3, exemplar.getSituacao().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    exemplar.setId(rs.getInt(1));
                }
            }
            return exemplar;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar exemplar", e);
        }
    }

    @Override
    public Optional<Exemplar> findById(Integer id) {
        String sql = "SELECT * FROM exemplar WHERE id = ?";
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar exemplar por id", e);
        }
    }

    @Override
    public List<Exemplar> findAll() {
        String sql = "SELECT * FROM exemplar ORDER BY id";
        List<Exemplar> lista = new ArrayList<>();
        try (Connection conn = databaseConfig.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar exemplares", e);
        }
    }

    @Override
    public List<Exemplar> findDisponiveisPorTitulo(Integer tituloId) {
        String sql = "SELECT * FROM exemplar WHERE titulo_id = ? AND situacao = 'DISPONIVEL'";
        List<Exemplar> lista = new ArrayList<>();
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tituloId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar exemplares disponiveis", e);
        }
    }

    @Override
    public void updateSituacao(Integer id, Exemplar.Situacao situacao) {
        String sql = "UPDATE exemplar SET situacao = ? WHERE id = ?";
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, situacao.name());
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar situacao do exemplar", e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM exemplar WHERE id = ?";
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover exemplar", e);
        }
    }

    private Exemplar mapRow(ResultSet rs) throws SQLException {
        return new Exemplar(
                rs.getInt("id"),
                rs.getInt("titulo_id"),
                rs.getString("codigo_tombo"),
                Exemplar.Situacao.valueOf(rs.getString("situacao"))
        );
    }
}
