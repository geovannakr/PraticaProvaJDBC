package org.example.dao;

import org.example.Conexao;
import org.example.model.OrdemPeca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrdemPecaDAO {

    public void inserirOrdemPeca(OrdemPeca ordemPeca){
        String query = "INSERT INTO OrdemPeca(idOrdem, idPeca, quantidade) VALUES (?,?,?)";

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setInt(1,ordemPeca.getIdOrdem());
            stmt.setInt(2,ordemPeca.getIdPeca());
            stmt.setDouble(3,ordemPeca.getQuantidade());
            stmt.executeUpdate();

            System.out.println("Peças associadas a ordem com sucesso!");

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean verificarOrdemEPeca(int idOrdem, int idPeca){
        String query = "SELECT COUNT(0) AS contagem FROM OrdemPeca WHERE idOrdem = ? AND idPeca = ?";

        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setInt(1,idOrdem);
            stmt.setInt(2,idPeca);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                int contagem = rs.getInt("contagem");
                if(contagem > 0){
                    return true;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public List<OrdemPeca> buscarOrdemPecaPorIdOrdemManutencao(int idOrdem){
        List<OrdemPeca> ordens = new ArrayList<>();
        String query = "SELECT idOrdem, idPeca, quantidade FROM OrdemPeca WHERE idOrdem = ?";

        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setInt(1,idOrdem);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                int idOrdemNew = rs.getInt("idOrdem");
                int idPeca = rs.getInt("idPeca");
                double quantidade = rs.getDouble("quantidade");

                var ordem = new OrdemPeca(idOrdemNew, idPeca, quantidade);
                ordens.add(ordem);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return ordens;
    }
}
