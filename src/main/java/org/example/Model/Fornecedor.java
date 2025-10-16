package org.example.Model;

public class Fornecedor {
    private int id;
    private String nome;
    private String cnpj;

    public Fornecedor(int id, String nome, String cnpj) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
    }

    public Fornecedor(String nome, String cnpj) {
        this.nome = nome;
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Fornecedor[" +
                "id=" + id + ", nome='" + nome + ", cnpj='" + cnpj + ']';
    }
}
