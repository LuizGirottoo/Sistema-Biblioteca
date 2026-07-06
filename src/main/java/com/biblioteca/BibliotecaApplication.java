package com.biblioteca;

import com.biblioteca.config.DatabaseConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BibliotecaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BibliotecaApplication.class, args);
    }

    /** Garante que o schema do SQLite exista assim que a aplicacao subir. */
    @Bean
    CommandLineRunner inicializarBancoDeDados(DatabaseConfig databaseConfig) {
        return args -> databaseConfig.inicializarSchema();
    }
}
