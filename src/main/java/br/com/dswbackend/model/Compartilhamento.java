package br.com.dswbackend.model;

import org.springframework.data.mongodb.core.mapping.DBRef;

public class Compartilhamento {
  @DBRef
  private Quadro quadro;
  private boolean editavel;

  public Compartilhamento(Quadro quadro, boolean editavel) {
    this.quadro = quadro;
    this.editavel = editavel;
  }

  public Compartilhamento() {
  }

  public Quadro getQuadro() {
    return quadro;
  }

  public void setQuadro(Quadro quadro) {
    this.quadro = quadro;
  }

  public boolean isEditavel() {
    return editavel;
  }

  public void setEditavel(boolean editavel) {
    this.editavel = editavel;
  }

}
