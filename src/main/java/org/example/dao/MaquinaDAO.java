package org.example.dao;

import org.example.Conexao;
import org.example.model.Maquina;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaquinaDAO {

    public boolean buscarMaquinaPorSetor(Maquina maquina){
        String query = "SELECT nome FROM Maquina WHERE nome = ? AND setor = ?";

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1,maquina.getNome());
            stmt.setString(2, maquina.getSetor());

            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public void inserirMaquina(Maquina maquina){
        String query = "INSERT INTO Maquina(nome, setor, status) VALUES (?,?,?)";

        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1, maquina.getNome());
            stmt.setString(2, maquina.getSetor());
            stmt.setString(3, maquina.getStatus());
            stmt.executeUpdate();

            System.out.println("Máquina cadastrada com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List <Maquina> listarMaquinas(){
        String query = "SELECT id, nome, setor, status FROM Maquina WHERE status = 'OPERACIONAL'";

        List<Maquina> maquinas = new ArrayList<>();

        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)){

            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String setor = rs.getString("setor");
                String status = rs.getString("status");

                Maquina maquina = new Maquina(id, nome, setor, status);
                maquinas.add(maquina);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return maquinas;
    }

    public void atualizaStatus(int id, String status){
        String query = "UPDATE Maquina SET status = ? WHERE id = ?";

        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1,status);
            stmt.setInt(2,id);
            stmt.executeUpdate();

            System.out.println("Status da máquina atualizao para " + status);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
