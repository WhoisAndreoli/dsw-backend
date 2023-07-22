package br.com.dswbackend.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.dswbackend.dtos.CompartilhamentoDTO;
import br.com.dswbackend.dtos.QuadroRequest;
import br.com.dswbackend.dtos.QuadroResponse;
import br.com.dswbackend.exceptions.ErrorException;
import br.com.dswbackend.model.Lista;
import br.com.dswbackend.model.Quadro;
import br.com.dswbackend.model.Usuario;
import br.com.dswbackend.repositories.QuadroRepository;

@ExtendWith(MockitoExtension.class)
class QuadroServiceTest {

  @Mock
  private QuadroRepository repository;
  @Mock
  private IUsuarioService usuarioService;
  @Mock
  private SecurityContext context;
  @Mock
  private Authentication auth;

  @InjectMocks
  private QuadroService quadroService;

  @AfterAll
  static void cleanContext() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void should_create_a_Quadro() {
    when(context.getAuthentication()).thenReturn(auth);
    when(auth.getPrincipal()).thenReturn("andre@email.com");
    when(usuarioService.findByEmail(anyString())).thenReturn(usuario());
    when(repository.save(any())).thenReturn(createQuadro());

    SecurityContextHolder.setContext(context);
    QuadroResponse quadro = quadroService.create(creaQuadroRequest());
    assertThat(quadro.id()).isEqualTo("1");
  }

  @Test
  void should_throw_UserNotFoundException_When_Create_a_quadro(){
    when(context.getAuthentication()).thenReturn(auth);
    when(auth.getPrincipal()).thenReturn("andre@email.com");
    when(usuarioService.findByEmail(anyString())).thenThrow(ErrorException.class);

    QuadroRequest request = creaQuadroRequest();
    SecurityContextHolder.setContext(context);
    assertThrows(ErrorException.class, () -> quadroService.create(request));
  }

  @Test
  void should_Update_a_Quadro(){
    when(repository.findById(anyString())).thenReturn(Optional.of(createQuadro()));
    when(repository.save(any())).thenReturn(createQuadro());

    QuadroRequest request = creaQuadroRequest();
    assertDoesNotThrow(() -> quadroService.update(request, "1"));
  }

  @Test
  void should_throw_ErrorException_When_Update_a_Quadro() {
    when(repository.findById(anyString())).thenReturn(Optional.empty());

    QuadroRequest request = creaQuadroRequest();
    assertThrows(ErrorException.class, () -> quadroService.update(request, "1"));

  }

  @Test
  void should_Remove_a_Quadro(){
    when(repository.findById(any())).thenReturn(Optional.of(createQuadro()));

    assertDoesNotThrow(() -> quadroService.remove("1"));
  }

  @Test
  void should_Throw_ErrorException_when_Remove_a_Quadro(){
    when(repository.findById(any())).thenReturn(Optional.empty());

    assertThrows(ErrorException.class, () -> quadroService.remove("1"));
  }

  @Test 
  void should_Get_A_List_Of_QuadroResponse(){
    when(repository.findAll()).thenReturn(List.of(createQuadro()));

    List<QuadroResponse> quadros = quadroService.get();
    assertThat(quadros).isNotEmpty();
  }

  @Test
  void should_favorite_a_Quadro(){
    when(context.getAuthentication()).thenReturn(auth);
    when(auth.getPrincipal()).thenReturn("andre@email.com");
    when(usuarioService.findByEmail(anyString())).thenReturn(usuario());
    when(repository.findById(anyString())).thenReturn(Optional.of(createQuadro()));

    SecurityContextHolder.setContext(context);
    assertDoesNotThrow(() -> quadroService.favorite("1"));
  }

  @Test
  void should_throw_UserNotFound_when_favorite_a_Quadro(){
    when(context.getAuthentication()).thenReturn(auth);
    when(auth.getPrincipal()).thenReturn("andre@email.com");
    when(usuarioService.findByEmail(anyString())).thenThrow(ErrorException.class);

    SecurityContextHolder.setContext(context);
    assertThrows(ErrorException.class, () -> quadroService.favorite("1"));
  }

  @Test
  void should_throw_QuadroNotFound_when_favorite_a_Quadro(){
    when(context.getAuthentication()).thenReturn(auth);
    when(auth.getPrincipal()).thenReturn("andre@email.com");
    when(usuarioService.findByEmail(anyString())).thenReturn(usuario());
    when(repository.findById(anyString())).thenReturn(Optional.empty());

    SecurityContextHolder.setContext(context);
    assertThrows(ErrorException.class, () -> quadroService.favorite("1"));
  }

  @Test
  void should_unfavorite_a_Quadro(){
    when(context.getAuthentication()).thenReturn(auth);
    when(auth.getPrincipal()).thenReturn("andre@email.com");
    when(usuarioService.findByEmail(anyString())).thenReturn(usuario());
    when(repository.findById(anyString())).thenReturn(Optional.of(createQuadro()));

    SecurityContextHolder.setContext(context);
    assertDoesNotThrow(() -> quadroService.unfavorite("1"));
  }

  @Test
  void should_share_a_Quadro(){
    when(usuarioService.findByEmail(anyString())).thenReturn(usuario());
    when(repository.findById(anyString())).thenReturn(Optional.of(createQuadro()));

    CompartilhamentoDTO comp = new CompartilhamentoDTO("1",true, "fulano@email.com");;
    assertDoesNotThrow(() -> quadroService.share(comp));
  }

  public Usuario usuario() {
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

  public Quadro createQuadro() {
    Quadro quadro = new Quadro();
    quadro.setId("1");
    quadro.setTitulo("titulo");
    quadro.setCorFundo("vermelho");
    quadro.setCorTexto("azul");
    quadro.setUsuario(new Usuario());
    quadro.setListas(List.of(new Lista()));
    return quadro;
  }

  public QuadroRequest creaQuadroRequest() {
    return new QuadroRequest("vermelho", "azul", "titulo", List.of(new Lista()));
  }
}
