package br.com.dswbackend.model;

public class Tarefa {

  private String conteudo;

  public Tarefa(String conteudo) {
    this.conteudo = conteudo;
  }

  public Tarefa() {
  }

  public String getConteudo() {
    return conteudo;
  }

  public void setConteudo(String conteudo) {
    this.conteudo = conteudo;
  }
}
