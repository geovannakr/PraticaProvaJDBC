package org.example.dao;

import org.example.Conexao;
import org.example.model.Tecnico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TecnicoDAO {

    public boolean buscarTecnicoPorEspecialidade(Tecnico tecnico){
        String query = "SELECT nome FROM Tecnico WHERE nome = ? AND especialidade = ?";

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1,tecnico.getNome());
            stmt.setString(2, tecnico.getEspecialidade());

            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                return true;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public void inserirTecnico(Tecnico tecnico){
        String query = "INSERT INTO Tecnico(nome, especialidade) VALUES (?,?)";

        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1, tecnico.getNome());
            stmt.setString(2, tecnico.getEspecialidade());
            stmt.executeUpdate();

            System.out.println("Técnico cadastrado com sucesso!");

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public List<Tecnico> listarTecnicos(){
        String query = "SELECT id, nome, especialidade FROM Tecnico";

        List<Tecnico> tecnicos = new ArrayList<>();

        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)){

            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String especialidade = rs.getString("especialidade");

                Tecnico tecnico = new Tecnico(id, nome, especialidade);
                tecnicos.add(tecnico);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return tecnicos;
    }
}
