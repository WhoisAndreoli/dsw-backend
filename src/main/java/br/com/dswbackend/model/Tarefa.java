package br.com.dswbackend.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.com.dswbackend.dtos.TarefaRequest;

@Document
public class Tarefa {

  @Id
  private String id;
  private String conteudo;
  private String dataCriacao;
  private String dataUpdate;
  @DBRef
  @JsonBackReference
  private Lista lista;

  public Tarefa(TarefaRequest tarefa, Lista lista) {
    this.conteudo = tarefa.conteudo();
    this.lista = lista;
    this.dataCriacao = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy, hh:mm:ss"));
    this.dataUpdate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy, hh:mm:ss"));
  }

  public Tarefa() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public Lista getLista() {
    return lista;
  }

  public void setLista(Lista lista) {
    this.lista = lista;
  }

}
