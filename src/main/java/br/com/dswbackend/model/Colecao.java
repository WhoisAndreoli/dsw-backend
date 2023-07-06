package br.com.dswbackend.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;

public class Colecao {

  private String titulo;
  @DBRef
  private List<Quadro> quadros;

  public Colecao(String titulo, List<Quadro> quadros) {
    this.titulo = titulo;
    this.quadros = quadros;
  }

  public Colecao() {
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public List<Quadro> getQuadros() {
    return quadros;
  }

  public void setQuadros(List<Quadro> quadros) {
    this.quadros = quadros;
  }

}
