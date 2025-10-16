package org.example.Model;

public class RequisicaoItem {

    private int id;
    private int idRequisicao;
    private int idMaterial;
    private double quantidade;

    public RequisicaoItem(int id, int idRequisicao, int idMaterial, double quantidade) {
        this.id = id;
        this.idRequisicao = idRequisicao;
        this.idMaterial = idMaterial;
        this.quantidade = quantidade;
    }

    public RequisicaoItem(double quantidade) {
        this.quantidade = quantidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdRequisicao() {
        return idRequisicao;
    }

    public void setIdRequisicao(int idRequisicao) {
        this.idRequisicao = idRequisicao;
    }

    public int getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(int idMaterial) {
        this.idMaterial = idMaterial;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "RequisicaoItem[" + "id=" + id + ", idRequisicao=" + idRequisicao + ", idMaterial=" + idMaterial + ", quantidade=" + quantidade + ']';
    }
}
