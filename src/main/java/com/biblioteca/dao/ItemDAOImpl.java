package com.biblioteca.dao;

import com.biblioteca.config.DatabaseConfig;
import com.biblioteca.model.Item;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ItemDAOImpl implements ItemDAO {

    private final DatabaseConfig databaseConfig;

    public ItemDAOImpl(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public Item save(Item item) {
        String sql = "INSERT INTO item (emprestimo_id, exemplar_id, data_devolucao_prevista, data_devolucao_efetiva) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, item.getEmprestimoId());
            ps.setInt(2, item.getExemplarId());
            ps.setString(3, item.getDataDevolucaoPrevista().toString());
            ps.setString(4, item.getDataDevolucaoEfetiva() == null ? null : item.getDataDevolucaoEfetiva().toString());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    item.setId(rs.getInt(1));
                }
            }
            return item;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar item de emprestimo", e);
        }
    }

    @Override
    public Optional<Item> findById(Integer id) {
        String sql = "SELECT * FROM item WHERE id = ?";
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar item por id", e);
        }
    }

    @Override
    public List<Item> findByEmprestimoId(Integer emprestimoId) {
        String sql = "SELECT * FROM item WHERE emprestimo_id = ?";
        List<Item> lista = new ArrayList<>();
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, emprestimoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar itens do emprestimo", e);
        }
    }

    @Override
    public List<Item> findPendentes() {
        String sql = "SELECT * FROM item WHERE data_devolucao_efetiva IS NULL";
        List<Item> lista = new ArrayList<>();
        try (Connection conn = databaseConfig.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar itens pendentes de devolucao", e);
        }
    }

    @Override
    public void registrarDevolucao(Integer itemId, LocalDate dataDevolucaoEfetiva) {
        String sql = "UPDATE item SET data_devolucao_efetiva = ? WHERE id = ?";
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dataDevolucaoEfetiva.toString());
            ps.setInt(2, itemId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao registrar devolucao do item", e);
        }
    }

    private Item mapRow(ResultSet rs) throws SQLException {
        String devEfetivaStr = rs.getString("data_devolucao_efetiva");
        return new Item(
                rs.getInt("id"),
                rs.getInt("emprestimo_id"),
                rs.getInt("exemplar_id"),
                LocalDate.parse(rs.getString("data_devolucao_prevista")),
                devEfetivaStr == null ? null : LocalDate.parse(devEfetivaStr)
        );
    }
}
