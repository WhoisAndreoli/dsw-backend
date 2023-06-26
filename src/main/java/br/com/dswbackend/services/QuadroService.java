package br.com.dswbackend.services;

import java.util.List;
import java.util.Objects;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.dswbackend.dtos.Compartilhamento;
import br.com.dswbackend.dtos.QuadroRequest;
import br.com.dswbackend.dtos.QuadroResponse;
import br.com.dswbackend.exceptions.ErrorException;
import br.com.dswbackend.model.Quadro;
import br.com.dswbackend.model.Usuario;
import br.com.dswbackend.repositories.QuadroRepository;

@Service
@Transactional
public class QuadroService implements IQuadroService {

  private QuadroRepository repository;
  private IUsuarioService usuarioService;

  public QuadroService(QuadroRepository repository, IUsuarioService usuarioService) {
    this.repository = repository;
    this.usuarioService = usuarioService;
  }

  @Override
  public QuadroResponse create(QuadroRequest newQuadro) {
    String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Usuario usuario = usuarioService.findByEmail(principal);
    Quadro quadro = new Quadro(newQuadro);
    quadro.getUsuarios().add(usuario);
    quadro = repository.save(quadro);
    usuarioService.addQuadro(quadro, usuario);
    return QuadroResponse.of(quadro);
  }

  @Override
  public QuadroResponse update(QuadroRequest newQuadro, String id) {
    Quadro quadro = this.findById(id);
    verifyPermission(quadro);
    if (!newQuadro.corFundo().isBlank()) {
      quadro.setCorFundo(newQuadro.corFundo());
    }
    if (!newQuadro.corTexto().isBlank()) {
      quadro.setCorTexto(newQuadro.corTexto());
    }
    if (!newQuadro.titulo().isBlank()) {
      quadro.setTitulo(newQuadro.titulo());
    }
    if (newQuadro.listas() != null) {
      quadro.setListas(newQuadro.listas());
    }
    quadro = repository.save(quadro);
    return QuadroResponse.of(quadro);
  }

  @Override
  public void remove(String id) {
    Quadro quadro = this.findById(id);
    repository.delete(quadro);
  }

  @Override
  public List<QuadroResponse> get() {
    return repository.findAll().stream().map(QuadroResponse::of).toList();
  }

  @Override
  public void favorite(String id) {
    String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Usuario usuario = usuarioService.findByEmail(principal);
    Quadro quadro = this.findById(id);
    usuarioService.addFavorite(usuario, quadro);
  }

  @Override
  public Quadro findById(String id) {
    return repository.findById(id).orElseThrow(() -> new ErrorException("Quadro não cadastrado"));
  }

  @Override
  public void share(Compartilhamento comp) {
    Usuario usuario = usuarioService.findByEmail(comp.email());
    Quadro quadro = this.findById(comp.quadroID());
    if (comp.editavel()) {
      quadro.getUsuarios().add(usuario);
    }
    usuarioService.addShare(usuario, quadro);
    repository.save(quadro);
  }

  @Override
  public void verifyPermission(Quadro quadro) {
    String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    quadro.getUsuarios().stream().forEach(u -> {
      if (!Objects.equals(u.getEmail(), principal)) {
        throw new ErrorException("O usuario não tem permissão para editar o quatro");
      }
    });
  }

}
