package br.com.dswbackend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.dswbackend.model.Lista;
import br.com.dswbackend.model.Tarefa;

public record TarefaResponse(String id, String conteudo, String dataCriacao, String dataUpdate, @JsonIgnoreProperties( {
    "tarefas" }) Lista lista){
  public static TarefaResponse of(Tarefa tarefa) {
    return new TarefaResponse(tarefa.getId(), tarefa.getConteudo(), tarefa.getDataCriacao(),
        tarefa.getDataUpdate(), tarefa.getLista());
  }
}
