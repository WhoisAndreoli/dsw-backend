package br.com.dswbackend.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.dswbackend.config.Security;
import br.com.dswbackend.dtos.QuadroRequest;
import br.com.dswbackend.dtos.QuadroResponse;
import br.com.dswbackend.exceptions.ErrorException;
import br.com.dswbackend.model.Lista;
import br.com.dswbackend.model.Usuario;
import br.com.dswbackend.services.QuadroService;

@WebMvcTest(QuadroController.class)
@Import(Security.class)
class QuadroControllerTest {

  @MockBean
  private QuadroService service;

  @Autowired
  private MockMvc mockMvc;

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
  void createQuadro_ReturnsQuadro() throws Exception {
    when(service.create(any())).thenReturn(createResponse());
    
    mockMvc.perform(post("/api/v1/quadro/create")
        .contentType(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .content(
          """
              {
                "corFundo": "vermelho",
                "corTexto": "azul",
                "titulo": "titulo"
              }
              """))
              .andExpect(status().isCreated())
              .andExpect(jsonPath("$.corFundo").value("vermelho"))
              .andExpect(jsonPath("$.id").value("1"));

  }

  @Test
  void createQuadro_ThrowException400() throws Exception {
    mockMvc.perform(post("/api/v1/quadro/create")
        .contentType(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .content(
            """
                {}
                """))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateQuadro_returnQuadro() throws Exception {
    when(service.update(any(QuadroRequest.class), eq("1"))).thenReturn(createResponse());

    mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/quadro/update/{id}", "1")
        .contentType(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .content(
          """
              {
                "corFundo": "",
                "corTexto": "",
                "titulo": "",
                "listas": []
              }
              """))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.listas").isArray());
  }

  @Test
  void updateQuadro_throwException400() throws Exception {

    mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/quadro/update/{id}", "1")
        .contentType(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .content(
            """
                {}
                """))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void deleteQuadro() throws Exception {
    doNothing().when(service).remove(anyString());

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/quadro/delete/{}", eq("1"))
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void deleteQuadro_throwException400() throws Exception {
    doThrow(ErrorException.class).when(service).remove(anyString());

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/quadro/delete/{}", eq("1"))
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void getQuadros() throws Exception{
    when(service.get()).thenReturn(List.of(createResponse()));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/quadro/get")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
  }

  @Test
  void favoriteQuadro() throws Exception {
    doNothing().when(service).favorite(anyString());

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/quadro/favorite/{}", eq("1"))
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void favoriteQuadro_throwException400() throws Exception {
    doThrow(ErrorException.class).when(service).favorite(anyString());

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/quadro/favorite/{}", eq("1"))
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void unfavoriteQuadro() throws Exception {
    doNothing().when(service).unfavorite(anyString());

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/quadro/favorite/{}", eq("1"))
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void unfavoriteQuadro_throwException400() throws Exception {
    doThrow(ErrorException.class).when(service).unfavorite(anyString());

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/quadro/favorite/{}", eq("1"))
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void shareQuadro() throws Exception {
    doNothing().when(service).share(any());

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/quadro/share")
        .contentType(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .content("""
            {
              "quadroID": "1",
              "editavel": true,
              "email": "fulano@email.com"
            }
            """))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void shareQuadro_throwException400() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/quadro/share")
        .contentType(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .content("""
            {}
            """))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void shareQuadro_throwException400byservice() throws Exception {
    doThrow(ErrorException.class).when(service).share(any());

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/quadro/share")
        .contentType(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .content("""
            {
              "quadroID": "1",
              "editavel": true,
              "email": "fulano@email.com"
            }
            """))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  public QuadroResponse createResponse() {
    return new QuadroResponse("1", "vermelho", "azul", "titulo", new Usuario(), List.of(new Lista()));
  }
}
