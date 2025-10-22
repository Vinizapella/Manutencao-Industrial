package org.example;

import org.example.DAO.FornecedorDAO;
import org.example.DAO.MaterialDAO;
import org.example.DAO.NotaEntradaDAO;
import org.example.DAO.RequisicaoDAO;
import org.example.Model.*;
import org.example.view.MenuView;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final FornecedorDAO fornecedorDAO = new FornecedorDAO();
    private static final MaterialDAO materialDAO = new MaterialDAO();
    private static final NotaEntradaDAO notaEntradaDAO = new NotaEntradaDAO();
    private static final RequisicaoDAO requisicaoDAO = new RequisicaoDAO();

    public static void main(String[] args) {
        MenuView menuView = new MenuView();
        int opcao = -1;

        while (opcao != 0) {
            menuView.exibirMenu();
            System.out.print("Escolha uma opção: ");

            try {
                opcao = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Opção inválida. Digite um número.");
                sc.nextLine();
                continue;
            }
            sc.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarFornecedor();
                    break;
                case 2:
                    cadastrarMaterial();
                    break;
                case 3:
                    registrarNotaEntrada();
                    break;
                case 4:
                    criarRequisicaoMaterial();
                    break;
                case 5:
                    atenderRequisicao();
                    break;
                case 6:
                    cancelarRequisicao();
                    break;
                case 0:
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
        sc.close();
    }


    private static void cadastrarFornecedor() {
        System.out.println("\n--- 1. Cadastrar Fornecedor ---");
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("CNPJ (apenas números, 14 dígitos): ");
        String cnpj = sc.nextLine();

        if (nome.trim().isEmpty() || cnpj.trim().isEmpty()) {
            System.out.println("ERRO: Nome e CNPJ são obrigatórios.");
            return;
        }
        if (cnpj.length() != 14) {
            System.out.println("ERRO: CNPJ deve ter 14 dígitos.");
            return;
        }

        try {
            if (fornecedorDAO.existeCNPJ(cnpj)) {
                System.out.println("ERRO: CNPJ já cadastrado!");
                return;
            }

            Fornecedor fornecedor = new Fornecedor(nome, cnpj);
            fornecedorDAO.inserir(fornecedor);
            System.out.println("Fornecedor cadastrado com sucesso!");

        } catch (SQLException e) {
            System.out.println("ERRO ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    private static void cadastrarMaterial() {
        System.out.println("\n--- 2. Cadastrar Material ---");
        System.out.print("Nome do material: ");
        String nome = sc.nextLine();
        System.out.print("Unidade de medida (ex: kg, m, peça): ");
        String unidade = sc.nextLine();
        System.out.print("Quantidade inicial em estoque: ");
        double estoqueInicial = -1;

        try {
            estoqueInicial = sc.nextDouble();
        } catch (Exception e) {
            System.out.println("ERRO: Quantidade inválida.");
            sc.nextLine();
            return;
        }
        sc.nextLine();

        if (nome.trim().isEmpty() || unidade.trim().isEmpty()) {
            System.out.println("ERRO: Nome e Unidade são obrigatórios.");
            return;
        }
        if (estoqueInicial < 0) {
            System.out.println("ERRO: Estoque inicial não pode ser negativo.");
            return;
        }

        try {
            if (materialDAO.existeNome(nome)) {
                System.out.println("ERRO: Material com este nome já cadastrado!");
                return;
            }

            Material material = new Material(nome, unidade, estoqueInicial);
            materialDAO.inserir(material);
            System.out.println("Material cadastrado com sucesso!");

        } catch (SQLException e) {
            System.out.println("ERRO ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    private static void registrarNotaEntrada() {
        System.out.println("\n--- 3. Registrar Nota de Entrada ---");

        try {
            List<Fornecedor> fornecedores = fornecedorDAO.listarTodos();
            if (fornecedores.isEmpty()) {
                System.out.println("ERRO: Cadastre um fornecedor antes de registrar uma nota.");
                return;
            }

            System.out.println("Fornecedores disponíveis:");
            fornecedores.forEach(System.out::println);
            System.out.print("Digite o ID do Fornecedor: ");
            int idFornecedor = sc.nextInt();
            sc.nextLine();

            if (fornecedores.stream().noneMatch(f -> f.getId() == idFornecedor)) {
                System.out.println("ERRO: ID do fornecedor inválido.");
                return;
            }

            List<NotaEntradaItem> itens = new ArrayList<>();
            Set<Integer> idsMateriaisAdicionados = new HashSet<>();

            char continuar = 's';
            while (continuar == 's' || continuar == 'S') {
                System.out.println("\nAdicionando item à nota:");
                List<Material> materiais = materialDAO.listarTodos();

                if (materiais.isEmpty()) {
                    System.out.println("Nenhum material cadastrado.");
                    break;
                }

                System.out.println("Materiais disponíveis:");
                for (Material m : materiais) {
                    if (!idsMateriaisAdicionados.contains(m.getId())) {
                        System.out.println(m);
                    }
                }

                System.out.print("Digite o ID do Material: ");
                int idMaterial = sc.nextInt();

                if (idsMateriaisAdicionados.contains(idMaterial)) {
                    System.out.println("ERRO: Este material já foi adicionado a esta nota.");
                    sc.nextLine();
                    continue;
                }

                Material materialSelecionado = materialDAO.buscarPorId(idMaterial);
                if (materialSelecionado == null) {
                    System.out.println("ERRO: ID do material inválido.");
                    sc.nextLine();
                    continue;
                }

                System.out.print("Digite a Quantidade (unidade: " + materialSelecionado.getUnidade() + "): ");
                double quantidade = sc.nextDouble();
                sc.nextLine();

                if (quantidade <= 0) {
                    System.out.println("ERRO: Quantidade deve ser maior que zero.");
                    continue;
                }

                itens.add(new NotaEntradaItem(0, idMaterial, quantidade));
                idsMateriaisAdicionados.add(idMaterial);
                System.out.println("Material adicionado!");

                System.out.print("Adicionar outro material? (S/N): ");
                continuar = sc.nextLine().charAt(0);
            }

            if (itens.isEmpty()) {
                System.out.println("Registro de nota cancelado (nenhum item adicionado).");
                return;
            }

            Connection conn = null;
            try {
                conn = Conexao.conectar();
                conn.setAutoCommit(false);

                NotaEntrada nota = new NotaEntrada(idFornecedor, LocalDate.now());
                int idNotaGerada = notaEntradaDAO.inserirNota(nota, conn);

                for (NotaEntradaItem item : itens) {
                    item.setIdNotaEntrada(idNotaGerada);
                    notaEntradaDAO.inserirItemNota(item, conn);

                    Material m = materialDAO.buscarPorId(item.getIdMaterial());
                    double novoEstoque = m.getEstoque() + item.getQuantidade();
                    materialDAO.atualizarEstoque(item.getIdMaterial(), novoEstoque, conn);
                }

                conn.commit();
                System.out.println("Nota de entrada registrada com sucesso!");

            } catch (SQLException e) {
                if (conn != null) conn.rollback();
                System.out.println("ERRO ao registrar nota: " + e.getMessage());
            } finally {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            }

        } catch (SQLException e) {
            System.out.println("ERRO ao conectar ao banco de dados: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERRO: Entrada inválida. Tente novamente.");
            sc.nextLine();
        }
    }

    private static void criarRequisicaoMaterial() {
        System.out.println("\n--- 4. Criar Requisição de Material ---");

        System.out.print("Nome do Setor requisitante: ");
        String setor = sc.nextLine();

        if (setor.trim().isEmpty()) {
            System.out.println("ERRO: O setor é obrigatório.");
            return;
        }

        List<RequisicaoItem> itens = new ArrayList<>();
        Set<Integer> idsMateriaisAdicionados = new HashSet<>();

        try {
            char continuar = 's';
            while (continuar == 's' || continuar == 'S') {
                System.out.println("\nAdicionando item à requisição:");
                List<Material> materiais = materialDAO.listarTodos();

                if (materiais.isEmpty()) {
                    System.out.println("Nenhum material cadastrado.");
                    break;
                }

                System.out.println("Materiais disponíveis (Nome - Estoque Atual):");
                for (Material m : materiais) {
                    if (!idsMateriaisAdicionados.contains(m.getId())) {
                        System.out.println("ID: " + m.getId() + ", Nome: " + m.getNome() + ", Estoque: " + m.getEstoque() + " " + m.getUnidade());
                    }
                }

                System.out.print("Digite o ID do Material: ");
                int idMaterial = sc.nextInt();

                if (idsMateriaisAdicionados.contains(idMaterial)) {
                    System.out.println("ERRO: Este material já foi adicionado a esta requisição.");
                    sc.nextLine();
                    continue;
                }

                Material materialSelecionado = materialDAO.buscarPorId(idMaterial);
                if (materialSelecionado == null) {
                    System.out.println("ERRO: ID do material inválido.");
                    sc.nextLine();
                    continue;
                }

                System.out.print("Digite a Quantidade (Estoque atual: " + materialSelecionado.getEstoque() + "): ");
                double quantidade = sc.nextDouble();
                sc.nextLine();

                if (quantidade <= 0) {
                    System.out.println("ERRO: Quantidade deve ser maior que zero.");
                    continue;
                }
                if (quantidade > materialSelecionado.getEstoque()) {
                    System.out.println("ERRO: Estoque insuficiente! Solicitado: " + quantidade + ", Disponível: " + materialSelecionado.getEstoque());
                    continue;
                }

                itens.add(new RequisicaoItem(0, idMaterial, quantidade));
                idsMateriaisAdicionados.add(idMaterial);
                System.out.println("Material adicionado!");

                System.out.print("Adicionar outro material? (S/N): ");
                continuar = sc.nextLine().charAt(0);
            }

            if (itens.isEmpty()) {
                System.out.println("Criação de requisição cancelada (nenhum item adicionado).");
                return;
            }

            Connection conn = null;
            try {
                conn = Conexao.conectar();
                conn.setAutoCommit(false);

                Requisicao req = new Requisicao(setor, LocalDate.now(), "PENDENTE");
                int idReqGerada = requisicaoDAO.inserirRequisicao(req, conn);

                for (RequisicaoItem item : itens) {
                    item.setIdRequisicao(idReqGerada);
                    requisicaoDAO.inserirItemRequisicao(item, conn);
                }

                conn.commit();
                System.out.println("Requisição de material criada com sucesso! (Status: PENDENTE)");

            } catch (SQLException e) {
                if (conn != null) conn.rollback();
                System.out.println("ERRO ao criar requisição: " + e.getMessage());
            } finally {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            }

        } catch (SQLException e) {
            System.out.println("ERRO ao conectar ao banco de dados: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERRO: Entrada inválida. Tente novamente.");
            sc.nextLine();
        }
    }

    private static void atenderRequisicao() {
        System.out.println("\n--- 5. Atender Requisição ---");

        try {
            List<Requisicao> pendentes = requisicaoDAO.listarPendentes();
            if (pendentes.isEmpty()) {
                System.out.println("Nenhuma requisição pendente para atender.");
                return;
            }

            System.out.println("Requisições PENDENTES:");
            pendentes.forEach(System.out::println);

            System.out.print("Digite o ID da Requisição que deseja ATENDER: ");
            int idRequisicao = sc.nextInt();
            sc.nextLine();

            if (pendentes.stream().noneMatch(r -> r.getId() == idRequisicao)) {
                System.out.println("ERRO: ID inválido ou requisição não está pendente.");
                return;
            }

            List<RequisicaoItem> itens = requisicaoDAO.buscarItens(idRequisicao);

            boolean estoqueSuficiente = true;
            System.out.println("Verificando estoque para os itens...");
            for (RequisicaoItem item : itens) {
                Material m = materialDAO.buscarPorId(item.getIdMaterial());
                if (item.getQuantidade() > m.getEstoque()) {
                    System.out.println("ERRO: Estoque insuficiente para o material '" + m.getNome() + "'");
                    System.out.println("Solicitado: " + item.getQuantidade() + ", Disponível: " + m.getEstoque());
                    estoqueSuficiente = false;
                }
            }

            if (!estoqueSuficiente) {
                System.out.println("A requisição não pode ser atendida por falta de estoque.");
                return;
            }

            Connection conn = null;
            try {
                conn = Conexao.conectar();
                conn.setAutoCommit(false);

                for (RequisicaoItem item : itens) {
                    Material m = materialDAO.buscarPorId(item.getIdMaterial());
                    double novoEstoque = m.getEstoque() - item.getQuantidade();
                    materialDAO.atualizarEstoque(m.getId(), novoEstoque, conn);
                }

                requisicaoDAO.atualizarStatus(idRequisicao, "ATENDIDA", conn);

                conn.commit();
                System.out.println("Requisição " + idRequisicao + " atendida com sucesso! Estoque atualizado.");

            } catch (SQLException e) {
                if (conn != null) conn.rollback();
                System.out.println("ERRO ao atender requisição: " + e.getMessage());
            } finally {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            }

        } catch (SQLException e) {
            System.out.println("ERRO ao conectar ao banco de dados: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERRO: Entrada inválida.");
            sc.nextLine();
        }
    }

    private static void cancelarRequisicao() {
        System.out.println("\n--- 6. Cancelar Requisição ---");

        try {
            List<Requisicao> pendentes = requisicaoDAO.listarPendentes();
            if (pendentes.isEmpty()) {
                System.out.println("Nenhuma requisição pendente para cancelar.");
                return;
            }

            System.out.println("Requisições PENDENTES:");
            pendentes.forEach(System.out::println);

            System.out.print("Digite o ID da Requisição que deseja CANCELAR: ");
            int idRequisicao = sc.nextInt();
            sc.nextLine();

            if (pendentes.stream().noneMatch(r -> r.getId() == idRequisicao)) {
                System.out.println("ERRO: ID inválido ou requisição não está pendente.");
                return;
            }

            Connection conn = null;
            try {
                conn = Conexao.conectar();
                conn.setAutoCommit(false);

                requisicaoDAO.atualizarStatus(idRequisicao, "CANCELADA", conn);

                conn.commit();
                System.out.println("Requisição " + idRequisicao + " CANCELADA com sucesso.");

            } catch (SQLException e) {
                if (conn != null) conn.rollback();
                System.out.println("ERRO ao cancelar requisição: " + e.getMessage());
            } finally {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            }

        } catch (SQLException e) {
            System.out.println("ERRO ao conectar ao banco de dados: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERRO: Entrada inválida.");
            sc.nextLine();
        }
    }
}