package org.example.Model;

public class NotaEntradaItem {
    private int idNotaEntrada;
    private int idMaterial;
    private double quantidade;

    public NotaEntradaItem(int idNotaEntrada, int idMaterial, double quantidade) {
        this.idNotaEntrada = idNotaEntrada;
        this.idMaterial = idMaterial;
        this.quantidade = quantidade;
    }

    public int getIdNotaEntrada() {
        return idNotaEntrada;
    }

    public void setIdNotaEntrada(int idNotaEntrada) {
        this.idNotaEntrada = idNotaEntrada;
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

}