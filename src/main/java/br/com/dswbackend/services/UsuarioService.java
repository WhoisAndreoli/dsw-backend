package br.com.dswbackend.services;

import java.util.List;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.dswbackend.dtos.UsuarioRequest;
import br.com.dswbackend.dtos.UsuarioResponse;
import br.com.dswbackend.model.Usuario;
import br.com.dswbackend.repositories.UsuarioRepository;

@Service
public class UsuarioService implements IUsuarioService {

  private UsuarioRepository repository;
  private PasswordEncoder encoder;

  public UsuarioService(UsuarioRepository repository, PasswordEncoder encoder) {
    this.repository = repository;
    this.encoder = encoder;
  }

  @Override
  public UsuarioResponse create(UsuarioRequest usuario) throws IllegalAccessException {
    if (repository.findByEmail(usuario.email()).isPresent()) {
      throw new IllegalAccessException("email ja utilizado!");
    }
    Usuario newUsuario = new Usuario(usuario);
    newUsuario.setSenha(encoder.encode(newUsuario.getSenha()));
    return UsuarioResponse.of(repository.save(newUsuario));
  }

  @Override
  public List<UsuarioResponse> get() {
    return repository.findAll().stream().map(UsuarioResponse::of).toList();
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Usuario user = repository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("usuario nao encontrado"));
    return new User(user.getEmail(), user.getSenha(), AuthorityUtils.NO_AUTHORITIES);
  }

}
