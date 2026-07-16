package com.prosperapp.dao;

import com.prosperapp.database.DatabaseConnection;
import com.prosperapp.model.DecisionTecnica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DecisionTecnicaDAO {
    // ============================================================
    // Crea una nueva decisión técnica en la base de datos.
    // Retorna el objeto con su id y fecha de creación.
    // ============================================================
    public DecisionTecnica crear(DecisionTecnica decision) throws SQLException {

        String sql = """
            INSERT INTO decision_tecnica
            (id_funcionalidad, titulo, descripcion)
            VALUES (?, ?, ?)
            RETURNING id_decision, fecha_creacion
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, decision.getIdFuncionalidad());
            stmt.setString(2, decision.getTitulo());
            stmt.setString(3, decision.getDescripcion());

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    decision.setIdDecision(rs.getInt("id_decision"));
                    decision.setFechaCreacion(
                            rs.getTimestamp("fecha_creacion").toLocalDateTime());

                    return decision;
                }
            }
        }

        return null;
    }

    // ============================================================
    // Busca una decisión técnica por su identificador.
    // ============================================================
    public Optional<DecisionTecnica> obtenerPorId(int idDecision) throws SQLException {

        String sql = "SELECT * FROM decision_tecnica WHERE id_decision = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idDecision);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(mapearDecisionTecnica(rs));
                }
            }
        }

        return Optional.empty();
    }

    // ============================================================
    // Obtiene todas las decisiones técnicas registradas.
    // ============================================================
    public List<DecisionTecnica> listarTodos() throws SQLException {

        String sql = "SELECT * FROM decision_tecnica ORDER BY id_decision";

        List<DecisionTecnica> decisiones = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                decisiones.add(mapearDecisionTecnica(rs));
            }
        }

        return decisiones;
    }

    // ============================================================
    // Obtiene todas las decisiones técnicas de una funcionalidad.
    // ============================================================
    public List<DecisionTecnica> listarPorFuncionalidad(int idFuncionalidad) throws SQLException {

        String sql = """
            SELECT *
            FROM decision_tecnica
            WHERE id_funcionalidad = ?
            ORDER BY fecha_creacion
            """;

        List<DecisionTecnica> decisiones = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionalidad);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    decisiones.add(mapearDecisionTecnica(rs));
                }
            }
        }

        return decisiones;
    }

    // ============================================================
    // Actualiza una decisión técnica existente.
    // ============================================================
    public boolean actualizar(DecisionTecnica decision) throws SQLException {

        String sql = """
            UPDATE decision_tecnica
            SET id_funcionalidad = ?,
                titulo = ?,
                descripcion = ?
            WHERE id_decision = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, decision.getIdFuncionalidad());
            stmt.setString(2, decision.getTitulo());
            stmt.setString(3, decision.getDescripcion());
            stmt.setInt(4, decision.getIdDecision());

            return stmt.executeUpdate() > 0;
        }
    }

    // ============================================================
    // Elimina una decisión técnica por su identificador.
    // ============================================================
    public boolean eliminar(int idDecision) throws SQLException {

        String sql = "DELETE FROM decision_tecnica WHERE id_decision = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idDecision);

            return stmt.executeUpdate() > 0;
        }
    }

    // ============================================================
    // Convierte un registro de la base de datos en un objeto
    // DecisionTecnica.
    // ============================================================
    private DecisionTecnica mapearDecisionTecnica(ResultSet rs) throws SQLException {

        return new DecisionTecnica(
                rs.getInt("id_decision"),
                rs.getInt("id_funcionalidad"),
                rs.getString("titulo"),
                rs.getString("descripcion"),
                rs.getTimestamp("fecha_creacion").toLocalDateTime()
        );
    }



}