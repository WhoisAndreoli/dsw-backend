package br.com.dswbackend.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.dswbackend.model.Lista;
import br.com.dswbackend.model.Quadro;
import br.com.dswbackend.model.Tarefa;

public record ListaResponse(String id, String titulo, List<Tarefa> tarefas, @JsonIgnoreProperties( {
    "listas" }) Quadro quadro){

  public static ListaResponse of(Lista lista) {
    return new ListaResponse(lista.getId(), lista.getTitulo(), lista.getTarefas(), lista.getQuadro());
  }
}
