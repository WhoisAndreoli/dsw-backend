package br.com.dswbackend.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.com.dswbackend.dtos.QuadroRequest;

@Document
public class Quadro {

  @Id
  private String id;
  private String titulo;
  private String corFundo;
  private String corTexto;
  @DBRef
  @JsonBackReference
  private Usuario usuario;
  @DBRef
  @JsonManagedReference
  private List<Lista> listas;

  public Quadro(QuadroRequest quadro, Usuario usuario) {
    this.titulo = quadro.titulo();
    this.corFundo = quadro.corFundo();
    this.corTexto = quadro.corTexto();
    this.usuario = usuario;
    this.listas = new ArrayList<>();
  }

  public Quadro() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getCorFundo() {
    return corFundo;
  }

  public void setCorFundo(String corFundo) {
    this.corFundo = corFundo;
  }

  public String getCorTexto() {
    return corTexto;
  }

  public void setCorTexto(String corTexto) {
    this.corTexto = corTexto;
  }

  public List<Lista> getListas() {
    return listas;
  }

  public void setListas(List<Lista> listas) {
    this.listas = listas;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

}
