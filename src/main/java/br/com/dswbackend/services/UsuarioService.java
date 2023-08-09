package br.com.dswbackend.services;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.dswbackend.dtos.ColecaoDTO;
import br.com.dswbackend.dtos.RecuperarSenha;
import br.com.dswbackend.dtos.TrocarSenha;
import br.com.dswbackend.dtos.UsuarioRequest;
import br.com.dswbackend.dtos.UsuarioResponse;
import br.com.dswbackend.exceptions.ErrorException;
import br.com.dswbackend.model.Compartilhamento;
import br.com.dswbackend.model.Quadro;
import br.com.dswbackend.model.Usuario;
import br.com.dswbackend.repositories.UsuarioRepository;

@Service
@Transactional
public class UsuarioService implements IUsuarioService {

  private UsuarioRepository repository;
  private PasswordEncoder encoder;
  private IEmailService emailService;

  public UsuarioService(UsuarioRepository repository, PasswordEncoder encoder, IEmailService emailService) {
    this.repository = repository;
    this.encoder = encoder;
    this.emailService = emailService;
  }

  @Override
  public UsuarioResponse create(UsuarioRequest usuario) {
    if (repository.findByEmail(usuario.email()).isPresent()) {
      throw new ErrorException("O email já está sendo utilizado");
    }
    Usuario newUsuario = new Usuario(usuario);
    newUsuario.setSenha(encoder.encode(newUsuario.getSenha()));
    return UsuarioResponse.of(repository.save(newUsuario));
  }

  @Override
  public UsuarioResponse get() {
    String principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    return UsuarioResponse.of(this.findByEmail(principal));
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    Usuario user = this.findByEmail(username);
    return new User(user.getEmail(), user.getSenha(), AuthorityUtils.NO_AUTHORITIES);
  }

  @Override
  public void forgotPassword(String email) {
    if (repository.findByEmail(email).isEmpty()) {
      throw new ErrorException("Usuario não cadastrado");
    }
    String expiracao = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).plusDays(1).toString().substring(0, 35);

    String text = String.format("http://localhost:4200/reset?expiration=%s&email=%s", expiracao, email);
    emailService.sendEmail(email, text);
  }

  @Override
  public UsuarioResponse createNewPassword(RecuperarSenha form) {
    Usuario user = this.findByEmail(form.email());

    if (ZonedDateTime.parse(form.expiracao()).isBefore(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")))) {
      throw new ErrorException("Link expirado");
    }

    if (!form.senha().equals(form.senhaRepetida())) {
      throw new ErrorException("As senhas precisam ser iguais");
    }

    user.setSenha(encoder.encode(form.senhaRepetida()));
    return UsuarioResponse.of(repository.save(user));
  }

  @Override
  public UsuarioResponse changePassword(TrocarSenha form) {
    String principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

    Usuario user = this.findByEmail(principal);

    if (!encoder.matches(form.senhaAntiga(), user.getSenha())) {
      throw new ErrorException("Senha antiga incorreta");
    }

    if (!form.senhaNova().equals(form.senhaRepetida())) {
      throw new ErrorException("As senhas precisam ser iguais");
    }

    user.setSenha(encoder.encode(form.senhaNova()));
    return UsuarioResponse.of(repository.save(user));
  }

  @Override
  public void addQuadro(Quadro quadro, Usuario usuario) {
    usuario.getQuadros().add(quadro);
    repository.save(usuario);
  }

  public Usuario findByEmail(String email) {
    return repository.findByEmail(email).orElseThrow(() -> new ErrorException("Usuario não cadastrado"));
  }

  @Override
  public void addFavorite(Usuario usuario, Quadro quadro) {
    usuario.getFavoritos().add(quadro);
    repository.save(usuario);
  }

  @Override
  public void rmvFavorite(Usuario usuario, Quadro quadro) {
    for (Quadro q : usuario.getFavoritos()) {
      if (Objects.equals(q.getId(), quadro.getId())) {
        usuario.getFavoritos().remove(q);
        break;
      }
    }
    repository.save(usuario);
  }

  @Override
  public void addShare(Usuario usuario, Compartilhamento comp) {
    usuario.getCompartilhado().add(comp);
    repository.save(usuario);
  }

  @Override
  public UsuarioResponse collection(ColecaoDTO colecao) {
    String principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    Usuario user = this.findByEmail(principal);
    user.setColecoes(colecao.colecoes());
    return UsuarioResponse.of(repository.save(user));
  }

}
