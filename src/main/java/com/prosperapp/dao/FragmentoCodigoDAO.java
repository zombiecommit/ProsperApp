package com.prosperapp.dao;

import com.prosperapp.database.DatabaseConnection;
import com.prosperapp.model.FragmentoCodigo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FragmentoCodigoDAO {

    // Crea un nuevo fragmento de código en la base de datos.
    // Retorna el objeto con su id y fecha de creación.

    public FragmentoCodigo crear(FragmentoCodigo fragmento) throws SQLException {

        String sql = """
            INSERT INTO fragmento_codigo
            (id_funcionalidad, lenguaje, codigo)
            VALUES (?, ?, ?)
            RETURNING id_fragmento, fecha_creacion
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, fragmento.getIdFuncionalidad());
            stmt.setString(2, fragmento.getLenguaje());
            stmt.setString(3, fragmento.getCodigo());

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    fragmento.setIdFragmento(rs.getInt("id_fragmento"));
                    fragmento.setFechaCreacion(
                            rs.getTimestamp("fecha_creacion").toLocalDateTime());

                    return fragmento;
                }
            }
        }

        return null;
    }

    // Busca un fragmento de código por su identificador.

    public Optional<FragmentoCodigo> obtenerPorId(int idFragmento) throws SQLException {

        String sql = "SELECT * FROM fragmento_codigo WHERE id_fragmento = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFragmento);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(mapearFragmentoCodigo(rs));
                }
            }
        }

        return Optional.empty();
    }

    // Obtiene todos los fragmentos de código registrados.
    public List<FragmentoCodigo> listarTodos() throws SQLException {

        String sql = "SELECT * FROM fragmento_codigo ORDER BY id_fragmento";

        List<FragmentoCodigo> fragmentos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                fragmentos.add(mapearFragmentoCodigo(rs));
            }
        }

        return fragmentos;
    }


// Obtiene todos los fragmentos de código de una funcionalidad.

    public List<FragmentoCodigo> listarPorFuncionalidad(int idFuncionalidad) throws SQLException {

        String sql = """
        SELECT *
        FROM fragmento_codigo
        WHERE id_funcionalidad = ?
        ORDER BY fecha_creacion
        """;

        List<FragmentoCodigo> fragmentos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionalidad);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    fragmentos.add(mapearFragmentoCodigo(rs));
                }
            }
        }

        return fragmentos;
    }

    // Actualiza un fragmento de código existente.

    public boolean actualizar(FragmentoCodigo fragmento) throws SQLException {

        String sql = """
            UPDATE fragmento_codigo
            SET id_funcionalidad = ?,
                lenguaje = ?,
                codigo = ?
            WHERE id_fragmento = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, fragmento.getIdFuncionalidad());
            stmt.setString(2, fragmento.getLenguaje());
            stmt.setString(3, fragmento.getCodigo());
            stmt.setInt(4, fragmento.getIdFragmento());

            return stmt.executeUpdate() > 0;
        }
    }


    // Elimina un fragmento de código por su identificador.

    public boolean eliminar(int idFragmento) throws SQLException {

        String sql = "DELETE FROM fragmento_codigo WHERE id_fragmento = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFragmento);

            return stmt.executeUpdate() > 0;
        }
    }

    // Convierte un registro de la base de datos en un objeto
    // FragmentoCodigo.

    private FragmentoCodigo mapearFragmentoCodigo(ResultSet rs) throws SQLException {

        return new FragmentoCodigo(
                rs.getInt("id_fragmento"),
                rs.getInt("id_funcionalidad"),
                rs.getString("lenguaje"),
                rs.getString("codigo"),
                rs.getTimestamp("fecha_creacion").toLocalDateTime()
        );
    }


}