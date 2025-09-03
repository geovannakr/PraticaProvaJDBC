package org.example.dao;

import org.example.Conexao;
import org.example.model.Peca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<Peca> listarPecas(){
        String query = "SELECT id, nome, estoque FROM Peca";

        List<Peca> pecas = new ArrayList<>();

        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)){

            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                Double quantidade = rs.getDouble("estoque");

                var peca = new Peca(id, nome, quantidade);
                pecas.add(peca);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return pecas;
    }

    public double buscarEstoquePorId(int idPeca){
        String query = "SELECT estoque FROM Peca WHERE id = ?";

        double quantidade = 0;

        try (Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setInt(1, idPeca);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                quantidade = rs.getDouble("estoque");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return quantidade;
    }

    public void atualizaEstque(int id, double estoque){
        String query = "UPDATE Peca SET estoque = ? WHERE id = ?";

        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setDouble(1,estoque);
            stmt.setInt(2,id);
            stmt.executeUpdate();

            System.out.println("Estoque da peça atualizado!");
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}
