package br.com.dswbackend.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.dswbackend.dtos.TarefaRequest;
import br.com.dswbackend.dtos.TarefaResponse;
import br.com.dswbackend.exceptions.ErrorException;
import br.com.dswbackend.model.Lista;
import br.com.dswbackend.model.Tarefa;
import br.com.dswbackend.repositories.TarefaRepository;

@Service
@Transactional
public class TarefaService implements ITarefaService {

  private TarefaRepository repository;
  private IListaService listaService;

  public TarefaService(TarefaRepository repository, IListaService listaService) {
    this.repository = repository;
    this.listaService = listaService;
  }

  @Override
  public TarefaResponse create(String listaId, TarefaRequest tarefa) {
    Lista lista = listaService.findById(listaId);
    Tarefa newTarefa = new Tarefa(tarefa, lista);
    newTarefa = repository.save(newTarefa);
    listaService.addTarefa(newTarefa, lista);
    return TarefaResponse.of(newTarefa);
  }

  @Override
  public void delete(String id) {
    Tarefa tarefa = repository.findById(id).orElseThrow(() -> new ErrorException("Tarefa não cadastrada"));
    repository.delete(tarefa);
  }

  @Override
  public TarefaResponse update(String id, TarefaRequest newTarefa) {
    Tarefa tarefa = repository.findById(id).orElseThrow(() -> new ErrorException("Tarefa não cadastrada"));
    if (!newTarefa.conteudo().isBlank()) {
      tarefa.setConteudo(newTarefa.conteudo());
      tarefa.setDataUpdate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy, hh:mm:ss")));
    }
    return TarefaResponse.of(repository.save(tarefa));
  }

}
