package com.prosperapp.dao;

import com.prosperapp.database.DatabaseConnection;
import com.prosperapp.model.Nota;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NotaDAO {

    // ============================================================
// Crea una nueva nota en la base de datos.
// Retorna el objeto con su id y fecha de creación.
// ============================================================
    public Nota crear(Nota nota) throws SQLException {

        String sql = """
        INSERT INTO nota
        (id_funcionalidad, contenido)
        VALUES (?, ?)
        RETURNING id_nota, fecha_creacion
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nota.getIdFuncionalidad());
            stmt.setString(2, nota.getContenido());

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    nota.setIdNota(rs.getInt("id_nota"));
                    nota.setFechaCreacion(
                            rs.getTimestamp("fecha_creacion").toLocalDateTime());

                    return nota;
                }
            }
        }

        return null;
    }

    // ============================================================
// Busca una nota por su identificador.
// ============================================================
    public Optional<Nota> obtenerPorId(int idNota) throws SQLException {

        String sql = "SELECT * FROM nota WHERE id_nota = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idNota);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(mapearNota(rs));
                }
            }
        }

        return Optional.empty();
    }

    // ============================================================
// Obtiene todas las notas registradas.
// ============================================================
    public List<Nota> listarTodos() throws SQLException {

        String sql = "SELECT * FROM nota ORDER BY id_nota";

        List<Nota> notas = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                notas.add(mapearNota(rs));
            }
        }

        return notas;
    }

    // ============================================================
// Obtiene todas las notas pertenecientes a una funcionalidad.
// ============================================================
    public List<Nota> listarPorFuncionalidad(int idFuncionalidad) throws SQLException {

        String sql = """
            SELECT *
            FROM nota
            WHERE id_funcionalidad = ?
            ORDER BY fecha_creacion
            """;

        List<Nota> notas = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionalidad);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    notas.add(mapearNota(rs));
                }
            }
        }

        return notas;
    }

    // ============================================================
// Actualiza la información de una nota existente.
// ============================================================
    public boolean actualizar(Nota nota) throws SQLException {

        String sql = """
            UPDATE nota
            SET id_funcionalidad = ?,
                contenido = ?
            WHERE id_nota = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nota.getIdFuncionalidad());
            stmt.setString(2, nota.getContenido());
            stmt.setInt(3, nota.getIdNota());

            return stmt.executeUpdate() > 0;
        }
    }

    // ============================================================
// Elimina una nota por su identificador.
// ============================================================
    public boolean eliminar(int idNota) throws SQLException {

        String sql = "DELETE FROM nota WHERE id_nota = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idNota);

            return stmt.executeUpdate() > 0;
        }
    }

    // ============================================================
// Convierte un registro de la base de datos en un objeto Nota.
// ============================================================
    private Nota mapearNota(ResultSet rs) throws SQLException {

        return new Nota(
                rs.getInt("id_nota"),
                rs.getInt("id_funcionalidad"),
                rs.getString("contenido"),
                rs.getTimestamp("fecha_creacion").toLocalDateTime()
        );
    }



}