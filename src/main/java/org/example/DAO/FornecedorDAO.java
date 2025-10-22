package org.example.DAO;

import org.example.Conexao;
import org.example.Model.Fornecedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDAO {

    public void inserir(Fornecedor fornecedor) throws SQLException {
        String sql = "INSERT INTO Fornecedor (nome, cnpj) VALUES (?, ?)";
        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, fornecedor.getNome());
            pstmt.setString(2, fornecedor.getCnpj());
            pstmt.executeUpdate();
        }
    }

    public boolean existeCNPJ(String cnpj) throws SQLException{
        String sql = "SELECT COUNT(*) FROM Fornecedor WHERE cnpj = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, cnpj);

            try(ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()){
                    return rs.getInt(1)>0;
                }
            }
        }
        return false;
    }

    public List<Fornecedor> listarTodos() throws SQLException {
        List<Fornecedor> fornecedores = new ArrayList<>();
        String sql = "SELECT * FROM Fornecedor";

        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                fornecedores.add(new Fornecedor(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cnpj")
                ));
            }
        }
        return fornecedores;
    }
}