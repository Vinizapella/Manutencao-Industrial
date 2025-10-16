package org.example.Model;

public class NotaEntradaItem {

    private int id;
    private int idNotaEntrada;
    private int idMaterial;
    private double quantidade;

    public NotaEntradaItem(int id, int idNotaEntrada, int idMaterial, double quantidade) {
        this.id = id;
        this.idNotaEntrada = idNotaEntrada;
        this.idMaterial = idMaterial;
        this.quantidade = quantidade;
    }

    public NotaEntradaItem(double quantidade) {
        this.quantidade = quantidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "NotaEntradaItem[" + "id=" + id + ", idNotaEntrada=" + idNotaEntrada + ", idMaterial=" + idMaterial + ", quantidade=" + quantidade + ']';
    }
}
