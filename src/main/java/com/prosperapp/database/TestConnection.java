package com.prosperapp.database;

import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {

    public static void main(String[] args) {

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            System.out.println(
                    "Conexión exitosa con PostgreSQL."
            );

        } catch (SQLException e) {

            System.out.println(
                    "Error al conectar con PostgreSQL."
            );

            e.printStackTrace();
        }
    }
}