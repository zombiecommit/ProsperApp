package com.prosperapp.dao;

import com.prosperapp.database.DatabaseConnection;
import com.prosperapp.model.Funcionalidad;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FuncionalidadDAO {

    // Crea una nueva funcionalidad en la base de datos.
    // Retorna el objeto con su id y fecha de creación asignados.

    public Funcionalidad crear(Funcionalidad funcionalidad) throws SQLException {

        String sql = """
            INSERT INTO funcionalidad
            (id_seccion, titulo, descripcion, prioridad, fecha_limite)
            VALUES (?, ?, ?, ?, ?)
            RETURNING id_funcionalidad, fecha_creacion
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, funcionalidad.getIdSeccion());
            stmt.setString(2, funcionalidad.getTitulo());
            stmt.setString(3, funcionalidad.getDescripcion());
            stmt.setString(4, funcionalidad.getPrioridad());
            if (funcionalidad.getFechaLimite() != null) {
                stmt.setDate(5, Date.valueOf(funcionalidad.getFechaLimite()));
            } else {
                stmt.setNull(5, Types.DATE);
            }

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    funcionalidad.setIdFuncionalidad(
                            rs.getInt("id_funcionalidad"));

                    funcionalidad.setFechaCreacion(
                            rs.getTimestamp("fecha_creacion").toLocalDateTime());

                    return funcionalidad;
                }
            }
        }

        return null;
    }


    // Busca una funcionalidad por su identificador.
    // Retorna un Optional para indicar si fue encontrada o no.

    public Optional<Funcionalidad> obtenerPorId(int idFuncionalidad) throws SQLException {

        String sql = "SELECT * FROM funcionalidad WHERE id_funcionalidad = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionalidad);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(mapearFuncionalidad(rs));
                }
            }
        }

        return Optional.empty();
    }

    // Obtiene todas las funcionalidades registradas.

    public List<Funcionalidad> listarTodos() throws SQLException {

        String sql = "SELECT * FROM funcionalidad ORDER BY id_funcionalidad";

        List<Funcionalidad> funcionalidades = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                funcionalidades.add(mapearFuncionalidad(rs));
            }
        }

        return funcionalidades;
    }

    // Obtiene todas las funcionalidades pertenecientes
    // a una sección específica.

    public List<Funcionalidad> listarPorSeccion(int idSeccion) throws SQLException {

        String sql = """
            SELECT *
            FROM funcionalidad
            WHERE id_seccion = ?
            ORDER BY id_funcionalidad
            """;

        List<Funcionalidad> funcionalidades = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSeccion);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    funcionalidades.add(mapearFuncionalidad(rs));
                }
            }
        }

        return funcionalidades;
    }


    // Actualiza la información de una funcionalidad existente.

    public boolean actualizar(Funcionalidad funcionalidad) throws SQLException {

        String sql = """
            UPDATE funcionalidad
            SET id_seccion = ?,
                titulo = ?,
                descripcion = ?,
                prioridad = ?,
                fecha_limite = ?
            WHERE id_funcionalidad = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, funcionalidad.getIdSeccion());
            stmt.setString(2, funcionalidad.getTitulo());
            stmt.setString(3, funcionalidad.getDescripcion());
            stmt.setString(4, funcionalidad.getPrioridad());
            if (funcionalidad.getFechaLimite() != null) {
                stmt.setDate(5, Date.valueOf(funcionalidad.getFechaLimite()));
            } else {
                stmt.setNull(5, Types.DATE);
            }
            stmt.setInt(6, funcionalidad.getIdFuncionalidad());

            return stmt.executeUpdate() > 0;
        }
    }

    // Elimina una funcionalidad a partir de su identificador.

    public boolean eliminar(int idFuncionalidad) throws SQLException {

        String sql = "DELETE FROM funcionalidad WHERE id_funcionalidad = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionalidad);

            return stmt.executeUpdate() > 0;
        }
    }

    // Convierte un registro de la base de datos en un objeto
    // Funcionalidad.

    private Funcionalidad mapearFuncionalidad(ResultSet rs) throws SQLException {

        return new Funcionalidad(
                rs.getInt("id_funcionalidad"),
                rs.getInt("id_seccion"),
                rs.getString("titulo"),
                rs.getString("descripcion"),
                rs.getString("prioridad"),
                rs.getTimestamp("fecha_creacion").toLocalDateTime(),
                rs.getDate("fecha_limite") != null ? rs.getDate("fecha_limite").toLocalDate() : null
        );
    }

}