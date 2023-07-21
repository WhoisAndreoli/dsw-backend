package br.com.dswbackend.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.dswbackend.dtos.ColecaoDTO;
import br.com.dswbackend.dtos.RecuperarSenha;
import br.com.dswbackend.dtos.TrocarSenha;
import br.com.dswbackend.dtos.UsuarioRequest;
import br.com.dswbackend.dtos.UsuarioResponse;
import br.com.dswbackend.exceptions.ErrorException;
import br.com.dswbackend.model.Colecao;
import br.com.dswbackend.model.Usuario;
import br.com.dswbackend.repositories.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

  @Mock
  private UsuarioRepository repository;

  @Mock
  private PasswordEncoder encoder;

  @Mock
  private Authentication auth;

  @Mock
  private SecurityContext context;

  @Mock
  private IEmailService emailService;

  @InjectMocks
  private UsuarioService service;

  @Test
  void should_CreateAUser(){
    when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
    when(repository.save(any(Usuario.class))).thenReturn(createUsuario());
    when(encoder.encode(anyString())).thenReturn("123");

    UsuarioResponse user = service.create(createUsuarioRequest());
    assertThat(user.id()).isEqualTo("1");
  }

  @Test
  void should_ThrowException_When_CreateAUser(){
    when(repository.findByEmail(anyString())).thenReturn(Optional.of(createUsuario()));

   UsuarioRequest UsuarioRequest = createUsuarioRequest();
    assertThrows(ErrorException.class, () -> service.create(UsuarioRequest));
  }

  @Test
  void should_Get_A_User(){
    when(repository.findByEmail(anyString())).thenReturn(Optional.of(createUsuario()));
    when(context.getAuthentication()).thenReturn(auth);
    when(auth.getPrincipal()).thenReturn("andre@email.com");

    SecurityContextHolder.setContext(context);
    UsuarioResponse usuarioResponse = service.get();
    assertThat(usuarioResponse.id()).isEqualTo("1");
  }

  @Test
  void should_ThrowException_When_Get_A_User(){
    when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
    when(context.getAuthentication()).thenReturn(auth);
    when(auth.getPrincipal()).thenReturn("andre@email.com");

    SecurityContextHolder.setContext(context);
    assertThrows(ErrorException.class, () -> service.get());
  }

  @Test
  void should_Load_UserByUsername(){
    when(repository.findByEmail(anyString())).thenReturn(Optional.of(createUsuario()));

    var user = service.loadUserByUsername("andre@email.com");
    assertThat(user).isNotNull();
  }

  @Test
  void should_ThrowException_When_Load_UserByUsername(){
    when(repository.findByEmail(anyString())).thenReturn(Optional.empty());

    assertThrows(ErrorException.class, () -> service.loadUserByUsername("andre@email.com"));    
  }

  @Test
  void should_DontThrowException_When_ForgotPassword(){
    when(repository.findByEmail(anyString())).thenReturn(Optional.of(createUsuario()));

    assertDoesNotThrow(() -> service.forgotPassword("andre@email.com"));
  }

  @Test
  void should_Create_NewPassword() {
    String expiracao = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).plusDays(1).toString().substring(0, 35);
    RecuperarSenha form = new RecuperarSenha("andre@email.com", "333", "333", expiracao);

    when(repository.findByEmail(anyString())).thenReturn(Optional.of(createUsuario()));
    when(repository.save(any())).thenReturn(createUsuario());

    assertDoesNotThrow(() -> service.createNewPassword(form));
  }

  @Test
  void should_Throw_UserNotFoundException_When_CreateNewPassword() {
    String expiracao = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).plusDays(1).toString().substring(0, 35);
    RecuperarSenha form = new RecuperarSenha("andre@email.com", "333", "333", expiracao);

    when(repository.findByEmail(anyString())).thenReturn(Optional.empty());

    assertThrows(ErrorException.class, () -> service.createNewPassword(form));
  }

  @Test
  void should_throw_ErrorException_when_CreateNewPassword_With_an_expired_link() {
    String expiracao = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).minusMinutes(25).toString().substring(0, 35);
    RecuperarSenha form = new RecuperarSenha("andre@email.com", "333", "333", expiracao);

    when(repository.findByEmail(anyString())).thenReturn(Optional.of(createUsuario()));

    assertThrows(ErrorException.class, () -> service.createNewPassword(form), "Link expirado");
  }

  @Test
  void should_throw_ErrorException_when_CreateNewPassword_With_differents_passwords() {
    String expiracao = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).plusDays(1).toString().substring(0, 35);
    RecuperarSenha form = new RecuperarSenha("andre@email.com", "333", "123", expiracao);

    when(repository.findByEmail(anyString())).thenReturn(Optional.of(createUsuario()));
    assertThrows(ErrorException.class, () -> service.createNewPassword(form));
  }

  @Test
  void should_Change_Password() {
    TrocarSenha form = new TrocarSenha("123", "333", "333");

    when(context.getAuthentication()).thenReturn(auth);
    when(auth.getPrincipal()).thenReturn("andre@email.com");
    when(repository.findByEmail(anyString())).thenReturn(Optional.of(createUsuario()));
    when(encoder.matches(anyString(), anyString())).thenReturn(true);
    when(repository.save(any())).thenReturn(createUsuario());

    SecurityContextHolder.setContext(context);
    assertDoesNotThrow(() -> service.changePassword(form));
  }

  @Test
  void should_throw_UserNotFoundException_When_Change_Password() {
    TrocarSenha form = new TrocarSenha("123", "333", "333");

    when(context.getAuthentication()).thenReturn(auth);
    when(auth.getPrincipal()).thenReturn("andre@email.com");
    when(repository.findByEmail(anyString())).thenReturn(Optional.empty());

    SecurityContextHolder.setContext(context);
    assertThrows(ErrorException.class, () -> service.changePassword(form));
  }

  @Test
  void should_throw_ErrorException_When_Change_Password_With_wrong_old_password() {
    TrocarSenha form = new TrocarSenha("321", "333", "333");

    when(context.getAuthentication()).thenReturn(auth);
    when(auth.getPrincipal()).thenReturn("andre@email.com");
    when(repository.findByEmail(anyString())).thenReturn(Optional.of(createUsuario()));
    when(encoder.matches(anyString(), anyString())).thenReturn(false);

    SecurityContextHolder.setContext(context);
    assertThrows(ErrorException.class, () -> service.changePassword(form));
  }

  @Test
  void should_throw_ErrorException_When_Change_Password_with_differents_passwords() {
    TrocarSenha form = new TrocarSenha("123", "333", "000");

    when(context.getAuthentication()).thenReturn(auth);
    when(auth.getPrincipal()).thenReturn("andre@email.com");
    when(repository.findByEmail(anyString())).thenReturn(Optional.of(createUsuario()));
    when(encoder.matches(anyString(), anyString())).thenReturn(true);

    SecurityContextHolder.setContext(context);
    assertThrows(ErrorException.class, () -> service.changePassword(form));
  }

  @Test
  void should_Create_A_Collection() {
    ColecaoDTO colecaoDTO = new ColecaoDTO(List.of(new Colecao("titulo", new ArrayList<>())));

    when(context.getAuthentication()).thenReturn(auth);
    when(auth.getPrincipal()).thenReturn("andre@email.com");
    when(repository.findByEmail(anyString())).thenReturn(Optional.of(createUsuario()));
    when(repository.save(any())).thenReturn(createUsuario());

    SecurityContextHolder.setContext(context);
    assertDoesNotThrow(() -> service.collection(colecaoDTO));
  }

  public Usuario createUsuario() {
    Usuario usuario = new Usuario();
    usuario.setId("1");
    usuario.setEmail("andre@email.com");
    usuario.setSenha("123");
    usuario.setNome("andre");
    usuario.setQuadros(new ArrayList<>());
    usuario.setFavoritos(new ArrayList<>());
    usuario.setCompartilhado(new ArrayList<>());
    usuario.setColecoes(new ArrayList<>());
    return usuario;
  }

  public UsuarioRequest createUsuarioRequest() {
    return new UsuarioRequest("andre", "andre@email.com", "123");
  }
}
