package org.example.dao;

import org.example.Conexao;
import org.example.model.Peca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PecaDAO {

    public boolean buscarPeca(Peca peca){
        String query = "SELECT nome FROM Peca WHERE nome = ?";

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1,peca.getNome());

            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                return true;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public void inserirPeca(Peca peca){
        String query = "INSERT INTO Peca(nome, estoque) VALUES (?,?)";

        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1,peca.getNome());
            stmt.setDouble(2,peca.getQuantidade());
            stmt.executeUpdate();

            System.out.println("Peça cadastrada com sucesso!");

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
