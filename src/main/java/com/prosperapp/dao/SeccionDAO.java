package com.prosperapp.dao;

import com.prosperapp.database.DatabaseConnection;
import com.prosperapp.model.Seccion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SeccionDAO {

    public Seccion crear(Seccion seccion) throws SQLException {

        String sql = """
                INSERT INTO seccion
                (id_proyecto, nombre, orden)
                VALUES (?, ?, ?)
                RETURNING id_seccion
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, seccion.getIdProyecto());
            stmt.setString(2, seccion.getNombre());
            stmt.setInt(3, seccion.getOrden());

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    seccion.setIdSeccion(rs.getInt("id_seccion"));

                    return seccion;
                }
            }
        }

        return null;
    }

    public Optional<Seccion> obtenerPorId(int idSeccion) throws SQLException {

        String sql = "SELECT * FROM seccion WHERE id_seccion = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSeccion);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(mapearSeccion(rs));
                }
            }
        }

        return Optional.empty();
    }

    public List<Seccion> listarTodos() throws SQLException {

        String sql = "SELECT * FROM seccion ORDER BY id_seccion";

        List<Seccion> secciones = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                secciones.add(mapearSeccion(rs));
            }
        }

        return secciones;
    }

    public boolean actualizar(Seccion seccion) throws SQLException {

        String sql = """
                UPDATE seccion
                SET id_proyecto = ?, nombre = ?, orden = ?
                WHERE id_seccion = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, seccion.getIdProyecto());
            stmt.setString(2, seccion.getNombre());
            stmt.setInt(3, seccion.getOrden());
            stmt.setInt(4, seccion.getIdSeccion());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idSeccion) throws SQLException {

        String sql = "DELETE FROM seccion WHERE id_seccion = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSeccion);

            return stmt.executeUpdate() > 0;
        }
    }

    private Seccion mapearSeccion(ResultSet rs) throws SQLException {

        return new Seccion(
                rs.getInt("id_seccion"),
                rs.getInt("id_proyecto"),
                rs.getString("nombre"),
                rs.getInt("orden")
        );
    }
}