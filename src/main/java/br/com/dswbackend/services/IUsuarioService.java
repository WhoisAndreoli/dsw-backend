package br.com.dswbackend.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import br.com.dswbackend.dtos.UsuarioRequest;
import br.com.dswbackend.dtos.UsuarioResponse;

public interface IUsuarioService extends UserDetailsService {
  UsuarioResponse create(UsuarioRequest usuario) throws IllegalAccessException;

  List<UsuarioResponse> get();

}
