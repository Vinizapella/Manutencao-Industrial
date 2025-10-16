package org.example.Model;

import java.time.LocalDate;

public class Requisicao {

    private int id;
    private String setor;
    private LocalDate dataSolicitacao;
    private String status;

    public Requisicao(int id, String setor, LocalDate dataSolicitacao, String status) {
        this.id = id;
        this.setor = setor;
        this.dataSolicitacao = dataSolicitacao;
        this.status = status;
    }

    public Requisicao(String setor, LocalDate dataSolicitacao, String status) {
        this.setor = setor;
        this.dataSolicitacao = dataSolicitacao;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public LocalDate getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDate dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Requisicao[" + "id=" + id + ", setor='" + setor + ", dataSolicitacao=" + dataSolicitacao + ", status='" + status + ']';
    }
}
