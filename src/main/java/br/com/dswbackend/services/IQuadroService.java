package br.com.dswbackend.services;

import java.util.List;

import br.com.dswbackend.dtos.QuadroRequest;
import br.com.dswbackend.dtos.QuadroResponse;
import br.com.dswbackend.model.Lista;
import br.com.dswbackend.model.Quadro;

public interface IQuadroService {

  QuadroResponse create(QuadroRequest newQuadro);

  QuadroResponse update(QuadroRequest newQuadro, String id);

  void remove(String id);

  List<QuadroResponse> get();

  void addLista(Lista newLista, Quadro quadro);

  Quadro findById(String id);

  void favorite(String id);
}
