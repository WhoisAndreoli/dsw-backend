package br.com.dswbackend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.dswbackend.dtos.CompartilhamentoDTO;
import br.com.dswbackend.dtos.QuadroRequest;
import br.com.dswbackend.dtos.QuadroResponse;
import br.com.dswbackend.services.IQuadroService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/quadro")
public class QuadroController {

  private IQuadroService service;

  public QuadroController(IQuadroService service) {
    this.service = service;
  }

  @PostMapping("/create")
  @ResponseStatus(HttpStatus.CREATED)
  public QuadroResponse create(@Valid @RequestBody QuadroRequest quadro) {
    return service.create(quadro);
  }

  @PutMapping("/update/{id}")
  public QuadroResponse update(@PathVariable String id, @Valid @RequestBody QuadroRequest quadro) {
    return service.update(quadro, id);
  }

  @DeleteMapping("/delete/{id}")
  public void delete(@PathVariable("id") String id) {
    service.remove(id);
  }

  @GetMapping("/get")
  public List<QuadroResponse> get() {
    return service.get();
  }

  @PostMapping("/favorite/{id}")
  public void favorite(@PathVariable String id) {
    service.favorite(id);
  }

  @DeleteMapping("/favorite/{id}")
  public void unfavorite(@PathVariable String id) {
    service.unfavorite(id);
  }

  @PostMapping("/share")
  public void share(@Valid @RequestBody CompartilhamentoDTO comp) {
    service.share(comp);
  }

}
