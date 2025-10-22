package org.example.DAO;

import org.example.Model.NotaEntrada;
import org.example.Model.NotaEntradaItem;

import java.sql.*;

public class NotaEntradaDAO {

    public int inserirNota(NotaEntrada nota, Connection conn) throws SQLException {
        String sql = "INSERT INTO NotaEntrada (idFornecedor, dataEntrada) VALUES (?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, nota.getIdFornecedor());
            pstmt.setDate(2, Date.valueOf(nota.getDataEntrada()));

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Não foi possível inserir a nota de entrada.");
    }

    public void inserirItemNota(NotaEntradaItem item, Connection conn) throws SQLException {
        String sql = "INSERT INTO NotaEntradaItem (idNotaEntrada, idMaterial, quantidade) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, item.getIdNotaEntrada());
            pstmt.setInt(2, item.getIdMaterial());
            pstmt.setDouble(3, item.getQuantidade());
            pstmt.executeUpdate();
        }
    }
}