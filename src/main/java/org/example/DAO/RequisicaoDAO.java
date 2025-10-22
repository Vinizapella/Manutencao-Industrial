package org.example.DAO;

import org.example.Conexao;
import org.example.Model.Requisicao;
import org.example.Model.RequisicaoItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequisicaoDAO {

    public int inserirRequisicao(Requisicao requisicao, Connection conn) throws SQLException {
        String sql = "INSERT INTO Requisicao (setor, dataSolicitacao, status) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, requisicao.getSetor());
            pstmt.setDate(2, Date.valueOf(requisicao.getDataSolicitacao()));
            pstmt.setString(3, requisicao.getStatus());

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Não foi possível inserir a requisição.");
    }

    public void inserirItemRequisicao(RequisicaoItem item, Connection conn) throws SQLException {
        String sql = "INSERT INTO RequisicaoItem (idRequisicao, idMaterial, quantidade) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, item.getIdRequisicao());
            pstmt.setInt(2, item.getIdMaterial());
            pstmt.setDouble(3, item.getQuantidade());
            pstmt.executeUpdate();
        }
    }

    public List<Requisicao> listarPendentes() throws SQLException {
        List<Requisicao> requisicoes = new ArrayList<>();
        String sql = "SELECT * FROM Requisicao WHERE status = 'PENDENTE'";

        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                requisicoes.add(new Requisicao(
                        rs.getInt("id"),
                        rs.getString("setor"),
                        rs.getDate("dataSolicitacao").toLocalDate(),
                        rs.getString("status")
                ));
            }
        }
        return requisicoes;
    }

    public List<RequisicaoItem> buscarItens(int idRequisicao) throws SQLException {
        List<RequisicaoItem> itens = new ArrayList<>();
        String sql = "SELECT * FROM RequisicaoItem WHERE idRequisicao = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idRequisicao);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    itens.add(new RequisicaoItem(
                            rs.getInt("idRequisicao"),
                            rs.getInt("idMaterial"),
                            rs.getDouble("quantidade")
                    ));
                }
            }
        }
        return itens;
    }

    public void atualizarStatus(int idRequisicao, String novoStatus, Connection conn) throws SQLException {
        String sql = "UPDATE Requisicao SET status = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, novoStatus);
            pstmt.setInt(2, idRequisicao);
            pstmt.executeUpdate();
        }
    }
}