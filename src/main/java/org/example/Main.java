package org.example;

import com.sun.security.jgss.GSSUtil;
import org.example.dao.MaquinaDAO;
import org.example.dao.OrdemDAO;
import org.example.dao.PecaDAO;
import org.example.dao.TecnicoDAO;
import org.example.model.Maquina;
import org.example.model.OrdemManutencao;
import org.example.model.Peca;
import org.example.model.Tecnico;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner ler = new Scanner(System.in);

    public static void main(String[] args) {
        inicio();
    }

    public static void inicio(){
        boolean sair = false;

        System.out.println("----- SISTEMA DE MANUTENÇÃO INDUSTRIAL -----\n" +
                "1 - Cadastrar Máquina\n"+
                "2 - Cadastrar Técnico\n"+
                "3 - Cadastrar Peça\n"+
                "4 - Criar Ordem de Manutenção\n" +
                "5 - Associar peças à ordem\n" +
                "6 - Executar Manutenção\n" +
                "0 - Sair do Sistema\n" +
                "Escolha a opção desejada: ");
        int op = ler.nextInt();
        ler.nextLine();

        switch(op) {
            case 1: {
                cadastrarMaquina();
                break;
            }
            case 2: {
                cadastrarTecnico();
                break;
            }
            case 3: {
                cadastrarPeca();
                break;
            }
            case 4: {
                criarOrdemManutencao();
                break;
            }
            case 5: {
                associarPecasOrdem();
                break;
            }
            case 6: {
                executarManutencao();
                break;
            }
            case 0: {
                sair = true;
                break;
            }
        }
            if(!sair){
                inicio();
        }
    }

    public static void cadastrarMaquina(){
        System.out.println("Digite o nome da máquina: ");
        String nomeMaquina = ler.nextLine();

        System.out.println("Digite o setor da máquina: ");
        String setorMaquina = ler.nextLine();

        if(!nomeMaquina.isEmpty() && !setorMaquina.isEmpty()){
            var maquina = new Maquina(nomeMaquina, setorMaquina, "OPERACIONAL");
            var maquinaDao = new MaquinaDAO();

            boolean maquinaExiste = maquinaDao.buscarMaquinaPorSetor(maquina);

            if(!maquinaExiste){
                maquinaDao.inserirMaquina(maquina);
            }else{
                System.out.println("Máquina já cadastrada no setor!");
            }
        }else{
            System.out.println("Insira dados válidos! Nome da máquina ou setor da máquina estão vazios.");
        }
    }

    public static void cadastrarTecnico(){
        System.out.println("Digite o nome do técnico: ");
        String nomeTecnico = ler.nextLine();

        System.out.println("Digite a especialidade do técnico: ");
        String especialidadeTecnico = ler.nextLine();

        if(!nomeTecnico.isEmpty() && !especialidadeTecnico.isEmpty()){
            var tecnico = new Tecnico(nomeTecnico, especialidadeTecnico);
            var tecnicoDao = new TecnicoDAO();

            boolean tecnicoExiste = tecnicoDao.buscarTecnicoPorEspecialidade(tecnico);

            if(!tecnicoExiste){
                tecnicoDao.inserirTecnico(tecnico);
            }else{
                System.out.println("Técnico com especialidade já cadastrado!");
            }
        }else{
            System.out.println("Insira dados válidos! Nome do técnico ou especialiade estão vazios.");
        }
    }

    public static void cadastrarPeca(){
        System.out.println("Digite o nome da peça:");
        String nomePeca = ler.nextLine();

        System.out.println("Digite a quantidade de peças em estoque: ");
        Double quantidadePeca = ler.nextDouble();
        ler.nextLine();

        if(!nomePeca.isEmpty() && quantidadePeca >=0){
            var peca = new Peca(nomePeca, quantidadePeca);
            var pecaDao = new PecaDAO();

            boolean pecaExiste = pecaDao.buscarPeca(peca);

            if(!pecaExiste){
                pecaDao.inserirPeca(peca);
            }else{
                System.out.println("Peça já cadastrada no sistema!");
            }
        }else{
            System.out.println("Insira dados válidos! Nome da peça vazio ou quantidade de peças insuficientes (menor que ou igual a 0)");
        }
    }

    public static void criarOrdemManutencao(){

        List<Integer> opcoesIdMaquina = new ArrayList<>();
        var maquinaDao = new MaquinaDAO();
        List<Maquina> maquinas = maquinaDao.listarMaquinas();

        for (Maquina maquina : maquinas){
            System.out.println("----- MÁQUINAS -----\n"+
                    "ID: " + maquina.getId() + "\n"+
                    "Nome: " + maquina.getNome() + "\n"+
                    "Setor: " + maquina.getSetor() + "\n"+
                    "Status: " + maquina.getStatus() + "\n"+
                    "-----------------------------------------\n");

            opcoesIdMaquina.add(maquina.getId());
        }

        System.out.println("Digite o id da máquina que gostaria de selecionar: ");
        int idMaquina = ler.nextInt();
        ler.nextLine();

        if(opcoesIdMaquina.contains(idMaquina)){
            List<Integer> opcoesIdTecnico = new ArrayList<>();
            var tecnicoDao = new TecnicoDAO();
            List<Tecnico> tecnicos = tecnicoDao.listarTecnicos();

            for(Tecnico tecnico : tecnicos){
                System.out.println("----- TÉCNICOS -----\n"+
                        "ID: " + tecnico.getId() + "\n" +
                        "Nome: " + tecnico.getNome() + "\n"+
                        "Especialidade: " + tecnico.getEspecialidade() + "\n"+
                        "----------------------------------------------------");

                opcoesIdTecnico.add(tecnico.getId());
            }

            System.out.println("Digite o id do técnico que gostaria de selecionar: ");
            int idTecnico = ler.nextInt();
            ler.nextLine();

            if(opcoesIdTecnico.contains(idTecnico)){
                var ordemDao = new OrdemDAO();
                var ordem = new OrdemManutencao(idMaquina, idTecnico, LocalDate.now(), "PENDENTE");

                try{
                    ordemDao.inserirOrdem(ordem);
                    maquinaDao.atualizaStatus(idMaquina, "EM_MANUTENCAO");
                }catch (RuntimeException e){
                    e.printStackTrace();
                }
            } else{
                System.out.println("Opção inválida! Tente novamente:");
                criarOrdemManutencao();
            }
        }else{
            System.out.println("Opção inválida! Tente novamente: ");
            criarOrdemManutencao();
        }

    }

    public static void associarPecasOrdem(){
        List<Integer> opcoesIdOrdem = new ArrayList<>();
        var ordemDao = new OrdemDAO();
        List<OrdemManutencao> ordens = ordemDao.listarOrdensManutencaoPendente();

        for(OrdemManutencao ordemManutencao : ordens){
            System.out.println("------ ORDENS DE MANUTENÇÃO -----\n" +
                    "ID: " + ordemManutencao.getId() + "\n" +
                    "ID Máquina: " + ordemManutencao.getIdMaquina() + "\n" +
                    "ID Técnico: " + ordemManutencao.getIdTecnico() + "\n" +
                    "Data de solicitação: " + ordemManutencao.getDataSolicitacao() + "\n" +
                    "Status: " + ordemManutencao.getStatus() + "\n" +
                    "---------------------------------------------------------------------");

            opcoesIdOrdem.add(ordemManutencao.getId());
        }
    }

    public static void executarManutencao(){

    }

}