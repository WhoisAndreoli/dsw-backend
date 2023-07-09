package br.com.dswbackend.services;

import org.springframework.security.core.userdetails.UserDetailsService;

import br.com.dswbackend.dtos.ColecaoDTO;
import br.com.dswbackend.dtos.RecuperarSenha;
import br.com.dswbackend.dtos.TrocarSenha;
import br.com.dswbackend.dtos.UsuarioRequest;
import br.com.dswbackend.dtos.UsuarioResponse;
import br.com.dswbackend.model.Compartilhamento;
import br.com.dswbackend.model.Quadro;
import br.com.dswbackend.model.Usuario;

public interface IUsuarioService extends UserDetailsService {
  UsuarioResponse create(UsuarioRequest usuario);

  UsuarioResponse get();

  String forgotPassword(String email);

  UsuarioResponse createNewPassword(RecuperarSenha form);

  UsuarioResponse changePassword(TrocarSenha form);

  void addQuadro(Quadro quadro, Usuario usuario);

  Usuario findByEmail(String email);

  void addFavorite(Usuario usuario, Quadro quadro);

  void addShare(Usuario usuario, Compartilhamento comp);

  UsuarioResponse collection(ColecaoDTO colecao);

  void rmvFavorite(Usuario usuario, Quadro quadro);
}
