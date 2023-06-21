package br.com.dswbackend.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.dswbackend.dtos.ListaRequest;
import br.com.dswbackend.dtos.ListaResponse;
import br.com.dswbackend.exceptions.ErrorException;
import br.com.dswbackend.model.Lista;
import br.com.dswbackend.model.Quadro;
import br.com.dswbackend.model.Tarefa;
import br.com.dswbackend.repositories.ListaRepository;

@Service
@Transactional
public class ListaService implements IListaService {

  private ListaRepository repository;
  private IQuadroService quadroService;

  public ListaService(ListaRepository repository, IQuadroService quadroService) {
    this.repository = repository;
    this.quadroService = quadroService;
  }

  @Override
  public ListaResponse create(String quadroId, ListaRequest lista) {
    Quadro quadro = quadroService.findById(quadroId);
    Lista newLista = new Lista(lista, quadro);
    newLista = repository.save(newLista);
    quadroService.addLista(newLista, quadro);
    return ListaResponse.of(newLista);
  }

  @Override
  public List<ListaResponse> get() {
    return repository.findAll().stream().map(ListaResponse::of).toList();
  }

  @Override
  public void delete(String id) {
    Lista lista = this.findById(id);
    repository.delete(lista);
  }

  @Override
  public ListaResponse update(String id, ListaRequest lista) {
    Lista newLista = this.findById(id);
    if (!lista.titulo().isBlank()) {
      newLista.setTitulo(lista.titulo());
    }
    return ListaResponse.of(repository.save(newLista));
  }

  @Override
  public Lista findById(String listaId) {
    return repository.findById(listaId).orElseThrow(() -> new ErrorException("Lista não cadastrada"));
  }

  @Override
  public void addTarefa(Tarefa newTarefa, Lista lista) {
    lista.getTarefas().add(newTarefa);
    repository.save(lista);
  }

}
