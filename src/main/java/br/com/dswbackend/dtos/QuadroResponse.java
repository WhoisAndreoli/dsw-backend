package br.com.dswbackend.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.dswbackend.model.Lista;
import br.com.dswbackend.model.Quadro;
import br.com.dswbackend.model.Usuario;

public record QuadroResponse(String id, String corFundo, String corTexto, String titulo,
    @JsonIgnoreProperties( {
        "quadros", "favoritos", "compartilhado" }) List<Usuario> usuarios,
    List<Lista> listas){
  public static QuadroResponse of(Quadro quadro) {
    return new QuadroResponse(quadro.getId(), quadro.getCorFundo(), quadro.getCorTexto(),
        quadro.getTitulo(), quadro.getUsuarios(), quadro.getListas());
  }
}
