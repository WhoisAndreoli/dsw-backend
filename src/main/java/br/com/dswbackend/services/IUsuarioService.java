package br.com.dswbackend.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import br.com.dswbackend.dtos.RecuperarSenha;
import br.com.dswbackend.dtos.TrocarSenha;
import br.com.dswbackend.dtos.UsuarioRequest;
import br.com.dswbackend.dtos.UsuarioResponse;
import br.com.dswbackend.model.Quadro;
import br.com.dswbackend.model.Usuario;

public interface IUsuarioService extends UserDetailsService {
  UsuarioResponse create(UsuarioRequest usuario);

  List<UsuarioResponse> get();

  String forgotPassword(String email);

  UsuarioResponse createNewPassword(RecuperarSenha form);

  UsuarioResponse changePassword(TrocarSenha form);

  void addQuadro(Quadro quadro, Usuario usuario);

  Usuario findByEmail(String email);
}
