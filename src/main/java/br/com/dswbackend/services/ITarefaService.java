package br.com.dswbackend.services;

import br.com.dswbackend.dtos.TarefaRequest;
import br.com.dswbackend.dtos.TarefaResponse;

public interface ITarefaService {
  TarefaResponse create(String listaId, TarefaRequest tarefa);

  void delete(String id);

  TarefaResponse update(String id, TarefaRequest tarefa);
}
