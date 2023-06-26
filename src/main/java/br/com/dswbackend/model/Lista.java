package br.com.dswbackend.model;

import java.util.ArrayList;
import java.util.List;

public class Lista {

  private String titulo;
  private List<Tarefa> tarefas;

  public Lista(String titulo) {
    this.titulo = titulo;
    this.tarefas = new ArrayList<>();
  }

  public Lista() {
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

}
