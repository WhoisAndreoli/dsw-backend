package br.com.dswbackend.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Tarefa {

  private String conteudo;
  private String dataCriacao;
  private String dataUpdate;

  public Tarefa(String conteudo) {
    this.conteudo = conteudo;
    this.dataCriacao = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy, hh:mm:ss"));
    this.dataUpdate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy, hh:mm:ss"));
  }

  public Tarefa() {
  }

  public String getConteudo() {
    return conteudo;
  }

  public void setConteudo(String conteudo) {
    this.conteudo = conteudo;
  }

  public String getDataCriacao() {
    return dataCriacao;
  }

  public void setDataCriacao(String dataCriacao) {
    this.dataCriacao = dataCriacao;
  }

  public String getDataUpdate() {
    return dataUpdate;
  }

  public void setDataUpdate(String dataUpdate) {
    this.dataUpdate = dataUpdate;
  }

}
