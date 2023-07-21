package br.com.dswbackend.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.dswbackend.config.Security;
import br.com.dswbackend.dtos.UsuarioRequest;
import br.com.dswbackend.dtos.UsuarioResponse;
import br.com.dswbackend.exceptions.ErrorException;
import br.com.dswbackend.model.Colecao;
import br.com.dswbackend.model.Compartilhamento;
import br.com.dswbackend.model.Quadro;
import br.com.dswbackend.services.UsuarioService;

@WebMvcTest(UsuarioController.class)
@Import(Security.class)
class UsuarioControllerTest {

  @MockBean
  UsuarioService service;

  @Autowired
  MockMvc mockMvc;

  private String token;

  @BeforeEach
  void setUp() {
    token = JWT.create()
        .withSubject("andre@email.com")
        .withIssuedAt(Instant.now())
        .withExpiresAt(Instant.now().plus(30, ChronoUnit.MINUTES))
        .sign(Algorithm.HMAC512("secret"));
  }

  @Test
  void createQuadro_ReturnUsuario() throws Exception {
    when(service.create(any(UsuarioRequest.class))).thenReturn(creatUsuario());

    mockMvc.perform(post("/api/v1/usuario/create")
        .contentType(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .content("""
            {
              "nome": "andre",
              "email": "andre@email.com",
              "senha": "123"
            }
            """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value("1"));
  }

  @Test
  void getUsuario_ReturnUsuario() throws Exception{
    when(service.get()).thenReturn(creatUsuario());

    mockMvc.perform(get("/api/v1/usuario/get")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").exists());
  }

  @Test
  void getUsuario_ThrowException400() throws Exception {
    doThrow(ErrorException.class).when(service).get();

    mockMvc.perform(get("/api/v1/usuario/get")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
        .andExpect(status().isBadRequest());
  }

  @Test
  void forgotPassword() throws Exception {
    doNothing().when(service).forgotPassword(anyString());

    mockMvc.perform(post("/api/v1/usuario/forgot/{}", eq("andre@email.com"))
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
        .andExpect(status().isOk());
  }

  @Test
  void forgotPassword_ThrowException400() throws Exception {
    doThrow(ErrorException.class).when(service).forgotPassword(anyString());

    mockMvc.perform(post("/api/v1/usuario/forgot/{}", eq("andre@email.com"))
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
        .andExpect(status().isBadRequest());
  }

  @Test
  void resetPassword() throws Exception{
    when(service.createNewPassword(any())).thenReturn(creatUsuario());
    String expiracao = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).plusDays(1).toString().substring(0, 35);
    mockMvc.perform(post("/api/v1/usuario/reset")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "email": "andre@email.com",
              "senha": "123",
              "senhaRepetida": "123",
              "expiracao": "$expiracao"
            }
            """.replace("$expiracao", expiracao)))
        .andExpect(status().isOk());
  }

  @Test
  void resetPassword_ThrowException400() throws Exception {
    doThrow(ErrorException.class).when(service).createNewPassword(any());
    String expiracao = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).plusDays(1).toString().substring(0, 35);
    mockMvc.perform(post("/api/v1/usuario/reset")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "email": "andre@email.com",
              "senha": "123",
              "senhaRepetida": "123",
              "expiracao": "$expiracao"
            }
            """.replace("$expiracao", expiracao)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void changePassword() throws Exception{
    when(service.changePassword(any())).thenReturn(creatUsuario());

    mockMvc.perform(put("/api/v1/usuario/change")
        .header(HttpHeaders.AUTHORIZATION,"Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "senhaAntiga": "123",
              "senhaNova": "333",
              "senhaRepetida": "333"
            }
            """))
        .andExpect(status().isOk());
  }

  @Test
  void changePassword_ThrowException400() throws Exception {
    doThrow(ErrorException.class).when(service).changePassword(any());

    mockMvc.perform(put("/api/v1/usuario/change")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "senhaAntiga": "123",
              "senhaNova": "333",
              "senhaRepetida": "333"
            }
            """))
        .andExpect(status().isBadRequest());
  }

  @Test
  void collection() throws Exception{
    when(service.collection(any())).thenReturn(creatUsuario());

    mockMvc.perform(post("/api/v1/usuario/collection")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "colecoes": [
                {
                  "titulo": "uma colecao",
                  "quadros": []
                }
              ]
            }
            """))
        .andExpect(status().isOk());
  }

  @Test
  void collection_ThrowEception400() throws Exception {
    doThrow(ErrorException.class).when(service).collection(any());

    mockMvc.perform(post("/api/v1/usuario/collection")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "colecoes": [
                {
                  "titulo": "uma colecao",
                  "quadros": []
                }
              ]
            }
            """))
        .andExpect(status().isBadRequest());
  }

  public UsuarioResponse creatUsuario() {
    return new UsuarioResponse(
        "1",
        "andre",
        "andre@email.com",
        "123",
        List.of(new Quadro()),
        List.of(new Quadro()),
        List.of(new Compartilhamento()),
        List.of(new Colecao()));
  }
}
