package br.com.dswbackend.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.com.dswbackend.dtos.ListaRequest;

@Document
public class Lista {
  @Id
  private String id;
  private String titulo;
  @DBRef
  @JsonManagedReference
  private List<Tarefa> tarefas;
  @DBRef
  @JsonBackReference
  private Quadro quadro;

  public Lista(ListaRequest lista, Quadro quadro) {
    this.titulo = lista.titulo();
    this.tarefas = new ArrayList<>();
    this.quadro = quadro;
  }

  public Lista() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public List<Tarefa> getTarefas() {
    return tarefas;
  }

  public void setTarefas(List<Tarefa> tarefas) {
    this.tarefas = tarefas;
  }

  public Quadro getQuadro() {
    return quadro;
  }

  public void setQuadro(Quadro quadro) {
    this.quadro = quadro;
  }

}
