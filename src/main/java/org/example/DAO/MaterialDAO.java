package org.example.DAO;

import org.example.Conexao;
import org.example.Model.Material;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {

    public void inserir(Material material) throws SQLException {
        String sql = "INSERT INTO Material (nome, unidade, estoque) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, material.getNome());
            pstmt.setString(2, material.getUnidade());
            pstmt.setDouble(3, material.getEstoque());
            pstmt.executeUpdate();
        }
    }

    public boolean existeNome(String nome) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Material WHERE nome = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public Material buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Material WHERE id = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Material(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("unidade"),
                            rs.getDouble("estoque")
                    );
                }
            }
        }
        return null;
    }

    public List<Material> listarTodos() throws SQLException {
        List<Material> materiais = new ArrayList<>();
        String sql = "SELECT * FROM Material";

        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                materiais.add(new Material(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("unidade"),
                        rs.getDouble("estoque")
                ));
            }
        }
        return materiais;
    }

    public void atualizarEstoque(int idMaterial, double novaQuantidade, Connection conn) throws SQLException {
        String sql = "UPDATE Material SET estoque = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, novaQuantidade);
            pstmt.setInt(2, idMaterial);
            pstmt.executeUpdate();
        }
    }
}