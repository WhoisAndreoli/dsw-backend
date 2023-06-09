package br.com.dswbackend.dtos;

import br.com.dswbackend.model.Usuario;

public record UsuarioResponse(String id, String nome, String email, String senha) {

  public static UsuarioResponse of(Usuario usuario) {
    return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getSenha());
  }
}
