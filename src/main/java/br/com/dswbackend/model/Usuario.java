package br.com.dswbackend.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.com.dswbackend.dtos.UsuarioRequest;

@Document
public class Usuario {

  @Id
  private String id;
  private String email;
  private String senha;
  private String nome;
  @DBRef
  @JsonManagedReference
  private List<Quadro> quadros;
  @DBRef
  @JsonManagedReference
  private List<Quadro> favoritos;

  public Usuario(UsuarioRequest usuario) {
    this.email = usuario.email();
    this.senha = usuario.senha();
    this.nome = usuario.nome();
    this.quadros = new ArrayList<>();
    this.favoritos = new ArrayList<>();
  }

  public Usuario() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public List<Quadro> getQuadros() {
    return quadros;
  }

  public void setQuadros(List<Quadro> quadros) {
    this.quadros = quadros;
  }

  public List<Quadro> getFavoritos() {
    return favoritos;
  }

  public void setFavoritos(List<Quadro> favoritos) {
    this.favoritos = favoritos;
  }

}
