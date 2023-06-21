package br.com.dswbackend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.dswbackend.dtos.ListaRequest;
import br.com.dswbackend.dtos.ListaResponse;
import br.com.dswbackend.services.IListaService;

@RestController
@RequestMapping("api/v1/lista")
public class ListaController {

  private IListaService service;

  public ListaController(IListaService service) {
    this.service = service;
  }

  @PostMapping("/create/{quadroId}")
  public ListaResponse create(@PathVariable String quadroId, @RequestBody ListaRequest lista) {
    return service.create(quadroId, lista);
  }

  @GetMapping("/get")
  public List<ListaResponse> get() {
    return service.get();
  }

  @DeleteMapping("/delete/{id}")
  public void delete(@PathVariable String id) {
    service.delete(id);
  }

  @PutMapping("/update/{id}")
  public ListaResponse update(@PathVariable String id, ListaRequest lista) {
    return service.update(id, lista);
  }
}
