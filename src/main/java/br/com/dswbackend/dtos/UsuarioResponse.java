package br.com.dswbackend.dtos;

import java.util.List;

import br.com.dswbackend.model.Colecao;
import br.com.dswbackend.model.Compartilhamento;
import br.com.dswbackend.model.Quadro;
import br.com.dswbackend.model.Usuario;

public record UsuarioResponse(String id, String nome, String email, String senha, List<Quadro> quadros,
    List<Quadro> favoritos, List<Compartilhamento> compartilhados, List<Colecao> colecoes) {

  public static UsuarioResponse of(Usuario usuario) {
    return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(),
        usuario.getQuadros(), usuario.getFavoritos(), usuario.getCompartilhado(), usuario.getColecoes());
  }
}
