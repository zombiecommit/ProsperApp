package com.prosperapp.dao;

import com.prosperapp.database.DatabaseConnection;
import com.prosperapp.model.Subtarea;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SubtareaDAO {

    // Crea una nueva subtarea en la base de datos.

    public Subtarea crear(Subtarea subtarea) throws SQLException {

        String sql = """
        INSERT INTO subtarea
        (id_funcionalidad, descripcion, estado)
        VALUES (?, ?, ?)
        RETURNING id_subtarea
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, subtarea.getIdFuncionalidad());
            stmt.setString(2, subtarea.getDescripcion());
            stmt.setString(3, subtarea.getEstado());

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    subtarea.setIdSubtarea(
                            rs.getInt("id_subtarea"));

                    return subtarea;
                }
            }
        }

        return null;
    }

    // Busca una subtarea por su identificador.
    public Optional<Subtarea> obtenerPorId(int idSubtarea) throws SQLException {

        String sql = "SELECT * FROM subtarea WHERE id_subtarea = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSubtarea);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(mapearSubtarea(rs));
                }
            }
        }

        return Optional.empty();
    }


    // Obtiene todas las subtareas registradas.

    public List<Subtarea> listarTodos() throws SQLException {

        String sql = "SELECT * FROM subtarea ORDER BY id_subtarea";

        List<Subtarea> subtareas = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                subtareas.add(mapearSubtarea(rs));
            }
        }

        return subtareas;
    }

    // Obtiene todas las subtareas pertenecientes a una funcionalidad.

    public List<Subtarea> listarPorFuncionalidad(int idFuncionalidad) throws SQLException {

        String sql = """
            SELECT *
            FROM subtarea
            WHERE id_funcionalidad = ?
            ORDER BY id_subtarea
            """;

        List<Subtarea> subtareas = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionalidad);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    subtareas.add(mapearSubtarea(rs));
                }
            }
        }

        return subtareas;
    }


    // Actualiza la información de una subtarea existente.

    public boolean actualizar(Subtarea subtarea) throws SQLException {

        String sql = """
            UPDATE subtarea
            SET id_funcionalidad = ?,
                descripcion = ?,
                estado = ?
            WHERE id_subtarea = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, subtarea.getIdFuncionalidad());
            stmt.setString(2, subtarea.getDescripcion());
            stmt.setString(3, subtarea.getEstado());
            stmt.setInt(4, subtarea.getIdSubtarea());

            return stmt.executeUpdate() > 0;
        }
    }

    // Elimina una subtarea por su identificador.

    public boolean eliminar(int idSubtarea) throws SQLException {

        String sql = "DELETE FROM subtarea WHERE id_subtarea = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSubtarea);

            return stmt.executeUpdate() > 0;
        }
    }


    // Convierte un registro de la base de datos en un objeto
    // Subtarea.

    private Subtarea mapearSubtarea(ResultSet rs) throws SQLException {

        return new Subtarea(
                rs.getInt("id_subtarea"),
                rs.getInt("id_funcionalidad"),
                rs.getString("descripcion"),
                rs.getString("estado")
        );
    }

}