package br.com.dswbackend.services;

import java.util.List;

import br.com.dswbackend.dtos.Compartilhamento;
import br.com.dswbackend.dtos.QuadroRequest;
import br.com.dswbackend.dtos.QuadroResponse;
import br.com.dswbackend.model.Quadro;

public interface IQuadroService {

  QuadroResponse create(QuadroRequest newQuadro);

  QuadroResponse update(QuadroRequest newQuadro, String id);

  void remove(String id);

  List<QuadroResponse> get();

  Quadro findById(String id);

  void favorite(String id);

  void share(Compartilhamento comp);

  void verifyPermission(Quadro quadro);
}
