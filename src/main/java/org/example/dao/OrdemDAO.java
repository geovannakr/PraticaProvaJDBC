package org.example.dao;

import org.example.Conexao;
import org.example.model.OrdemManutencao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdemDAO {

    public void inserirOrdem(OrdemManutencao ordemManutencao){
        String query = "INSERT INTO OrdemManutencao(idMaquina, idTecnico, dataSolicitacao, status) VALUES (?,?,?,?)";

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setInt(1,ordemManutencao.getIdMaquina());
            stmt.setInt(2,ordemManutencao.getIdTecnico());
            stmt.setDate(3, Date.valueOf(ordemManutencao.getDataSolicitacao()));
            stmt.setString(4,ordemManutencao.getStatus());
            stmt.executeUpdate();

            System.out.println("Ordem de manutenção criada com sucesso!");

        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public List<OrdemManutencao> listarOrdensManutencaoPendente(){
        String query = "SELECT id, idMaquina, idTecnico, dataSolicitacao, status FROM OrdemManutencao WHERE status = 'PENDENTE'";

        List<OrdemManutencao> ordens = new ArrayList<>();

        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                int id = rs.getInt("id");
                int idMaquina = rs.getInt("idMaquina");
                int idTecnico = rs.getInt("idTecnico");
                Date dataSolicitacao = rs.getDate("dataSolicitacao");
                String status = rs.getString("status");

                var ordem = new OrdemManutencao(id, idMaquina, idTecnico, dataSolicitacao.toLocalDate(), status);
                ordens.add(ordem);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return ordens;
    }

    public void atualizaStatusOrdem(int idOrdem){
        String query = "UPDATE OrdemManutencao SET status = 'EXECUTADA' WHERE id = ?";

        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setInt(1,idOrdem);
            stmt.executeUpdate();

            System.out.println("Status da ordem de manutenção alterado com sucesso!");

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
