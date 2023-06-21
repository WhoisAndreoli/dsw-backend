package br.com.dswbackend.dtos;

import jakarta.validation.constraints.NotNull;

public record TarefaRequest(@NotNull(message = "A campo conteudo não deve ser nula") String conteudo) {

}
