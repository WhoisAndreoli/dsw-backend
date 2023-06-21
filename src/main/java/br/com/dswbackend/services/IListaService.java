package br.com.dswbackend.services;

import java.util.List;

import br.com.dswbackend.dtos.ListaRequest;
import br.com.dswbackend.dtos.ListaResponse;
import br.com.dswbackend.model.Lista;
import br.com.dswbackend.model.Tarefa;

public interface IListaService {

  ListaResponse create(String quadroId, ListaRequest lista);

  List<ListaResponse> get();

  void delete(String id);

  ListaResponse update(String id, ListaRequest lista);

  Lista findById(String listaId);

  void addTarefa(Tarefa newTarefa, Lista lista);
}
