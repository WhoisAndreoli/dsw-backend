package br.com.dswbackend.services;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.dswbackend.dtos.Login;
import br.com.dswbackend.dtos.RecuperarSenha;
import br.com.dswbackend.dtos.TrocarSenha;
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

  @Override
  public String forgotPassword(Login login) {
    if (repository.findByEmail(login.email()).isEmpty()) {
      throw new IllegalAccessError("usuario nao cadastrado");
    }
    String expiracao = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).plusDays(1).toString().substring(0, 35);

    return String.format("localhost:8080/api/v1/usuario/reset?expiration=%s&email=%s", expiracao, login.email());

    // TODO - enviar email para o usuario

  }

  @Override
  public UsuarioResponse createNewPassword(RecuperarSenha form) {
    Usuario user = repository.findByEmail(form.email())
        .orElseThrow(() -> new IllegalAccessError("usuario nao encontrado"));

    if (ZonedDateTime.parse(form.expiracao()).isBefore(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")))) {
      throw new IllegalAccessError("token expirado");
    }

    if (!form.senha().equals(form.senhaRepetida())) {
      throw new IllegalAccessError("as senhas são diferentes");
    }

    user.setSenha(encoder.encode(form.senhaRepetida()));
    return UsuarioResponse.of(repository.save(user));
  }

  @Override
  public UsuarioResponse changePassword(TrocarSenha form) {
    String principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

    Usuario user = repository.findByEmail(principal).orElseThrow(() -> new IllegalAccessError("erro"));

    if (!encoder.matches(form.senhaAntiga(), user.getSenha())) {
      throw new IllegalAccessError("senha antiga incorreta");
    }

    if (!form.senhaNova().equals(form.senhaRepetida())) {
      throw new IllegalAccessError("as senhas são diferentes");
    }

    user.setSenha(encoder.encode(form.senhaNova()));
    return UsuarioResponse.of(repository.save(user));

  }

}
