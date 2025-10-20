package org.example.DAO;

import org.example.Conexao;
import org.example.Model.Fornecedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FornecedorDAO {

    public void inserir(Fornecedor fornecedor) throws SQLException{
        String sql = "INSERT INTO Fornecedor (nome, cnpj) VALUES (?, ?)";
        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, fornecedor.getNome());
            pstmt.setString(2, fornecedor.getCnpj());
            pstmt.executeUpdate();
        }
    }

    public boolean existeCNPJ(Fornecedor fornecedor) throws SQLException{
        String sql = "SELECT COUNT(*) FROM Fornecedor WHERE cnpj = ?";
        try (Connection conn = Conexao.conectar();
        PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, fornecedor.getCnpj());

        try(ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()){
                return rs.getInt(1)>0;
                }
            }
        }
        return false;
    }

}
