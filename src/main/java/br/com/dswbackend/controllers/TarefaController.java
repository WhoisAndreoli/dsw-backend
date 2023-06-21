package br.com.dswbackend.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.dswbackend.dtos.TarefaRequest;
import br.com.dswbackend.dtos.TarefaResponse;
import br.com.dswbackend.services.ITarefaService;

@RestController
@RequestMapping("api/v1/tarefa")
public class TarefaController {

  private ITarefaService service;

  public TarefaController(ITarefaService service) {
    this.service = service;
  }

  @PostMapping("/create/{listaId}")
  public TarefaResponse create(@PathVariable String listaId, @RequestBody TarefaRequest tarefa) {
    return service.create(listaId, tarefa);
  }

  @DeleteMapping("/delete/{id}")
  public void delete(@PathVariable String id) {
    service.delete(id);
  }

  @PutMapping("/update/{id}")
  public TarefaResponse update(@PathVariable String id, @RequestBody TarefaRequest tarefa) {
    return service.update(id, tarefa);
  }
}
