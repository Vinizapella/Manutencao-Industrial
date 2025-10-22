package org.example.Model;

import java.time.LocalDate;

public class NotaEntrada {
    private int idFornecedor;
    private LocalDate dataEntrada;

    public NotaEntrada(int idFornecedor, LocalDate dataEntrada) {
        this.idFornecedor = idFornecedor;
        this.dataEntrada = dataEntrada;
    }

    public int getIdFornecedor() {
        return idFornecedor;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }
}