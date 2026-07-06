package com.biblioteca.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Fabrica centralizada de conexoes JDBC com o banco SQLite.
 * Usada por todas as classes DAO da camada de persistencia.
 */
@Component
public class DatabaseConfig {

    @Value("${biblioteca.db.path:biblioteca.db}")
    private String dbPath;

    public Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        try (Statement st = conn.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
        }
        return conn;
    }

    /** Cria as tabelas do banco, caso ainda nao existam. Chamado na inicializacao da aplicacao. */
    public void inicializarSchema() {
        String sqlTitulo = """
            CREATE TABLE IF NOT EXISTS titulo (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                autor TEXT,
                isbn TEXT,
                prazo_emprestimo_dias INTEGER NOT NULL DEFAULT 7
            );
            """;

        String sqlExemplar = """
            CREATE TABLE IF NOT EXISTS exemplar (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo_id INTEGER NOT NULL,
                codigo_tombo TEXT NOT NULL UNIQUE,
                situacao TEXT NOT NULL DEFAULT 'DISPONIVEL',
                FOREIGN KEY (titulo_id) REFERENCES titulo(id)
            );
            """;

        String sqlAluno = """
            CREATE TABLE IF NOT EXISTS aluno (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                ra TEXT NOT NULL UNIQUE,
                nome TEXT NOT NULL,
                email TEXT,
                em_debito INTEGER NOT NULL DEFAULT 0
            );
            """;

        String sqlEmprestimo = """
            CREATE TABLE IF NOT EXISTS emprestimo (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                aluno_id INTEGER NOT NULL,
                data_emprestimo TEXT NOT NULL,
                status TEXT NOT NULL DEFAULT 'ATIVO',
                FOREIGN KEY (aluno_id) REFERENCES aluno(id)
            );
            """;

        String sqlItem = """
            CREATE TABLE IF NOT EXISTS item (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                emprestimo_id INTEGER NOT NULL,
                exemplar_id INTEGER NOT NULL,
                data_devolucao_prevista TEXT NOT NULL,
                data_devolucao_efetiva TEXT,
                FOREIGN KEY (emprestimo_id) REFERENCES emprestimo(id),
                FOREIGN KEY (exemplar_id) REFERENCES exemplar(id)
            );
            """;

        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            st.execute(sqlTitulo);
            st.execute(sqlExemplar);
            st.execute(sqlAluno);
            st.execute(sqlEmprestimo);
            st.execute(sqlItem);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inicializar o schema do banco de dados", e);
        }
    }
}
