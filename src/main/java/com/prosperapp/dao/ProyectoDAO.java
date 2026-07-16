package com.prosperapp.dao;

import com.prosperapp.database.DatabaseConnection;
import com.prosperapp.model.Proyecto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProyectoDAO {
    public Proyecto crear(Proyecto proyecto) throws SQLException {

        String sql = """
            INSERT INTO proyecto
            (id_usuario, nombre, descripcion, fecha_limite, estado)
            VALUES (?, ?, ?, ?, ?)
            RETURNING id_proyecto, fecha_creacion
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, proyecto.getIdUsuario());
            stmt.setString(2, proyecto.getNombre());
            stmt.setString(3, proyecto.getDescripcion());
            if (proyecto.getFechaLimite() != null) {
                stmt.setDate(4, Date.valueOf(proyecto.getFechaLimite()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            stmt.setString(5, proyecto.getEstado());

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    proyecto.setIdProyecto(rs.getInt("id_proyecto"));
                    proyecto.setFechaCreacion(
                            rs.getTimestamp("fecha_creacion").toLocalDateTime());

                    return proyecto;
                }
            }
        }

        return null;
    }

    public Optional<Proyecto> obtenerPorId(int idProyecto) throws SQLException {

        String sql = "SELECT * FROM proyecto WHERE id_proyecto = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProyecto);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(mapearProyecto(rs));
                }
            }
        }

        return Optional.empty();
    }

    public List<Proyecto> listarTodos() throws SQLException {

        String sql = "SELECT * FROM proyecto ORDER BY id_proyecto";

        List<Proyecto> proyectos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                proyectos.add(mapearProyecto(rs));
            }
        }

        return proyectos;
    }

    public boolean actualizar(Proyecto proyecto) throws SQLException {

        String sql = """
            UPDATE proyecto
            SET id_usuario = ?, nombre = ?, descripcion = ?,
                fecha_limite = ?, estado = ?
            WHERE id_proyecto = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, proyecto.getIdUsuario());
            stmt.setString(2, proyecto.getNombre());
            stmt.setString(3, proyecto.getDescripcion());
            if (proyecto.getFechaLimite() != null) {
                stmt.setDate(4, Date.valueOf(proyecto.getFechaLimite()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            stmt.setString(5, proyecto.getEstado());
            stmt.setInt(6, proyecto.getIdProyecto());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idProyecto) throws SQLException {

        String sql = "DELETE FROM proyecto WHERE id_proyecto = ?";


        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProyecto);

            return stmt.executeUpdate() > 0;
        }
    }

    private Proyecto mapearProyecto(ResultSet rs) throws SQLException {

        return new Proyecto(
                rs.getInt("id_proyecto"),
                rs.getInt("id_usuario"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getTimestamp("fecha_creacion").toLocalDateTime(),
                rs.getDate("fecha_limite") != null ? rs.getDate("fecha_limite").toLocalDate() : null,
                rs.getString("estado")
        );
    }

}
