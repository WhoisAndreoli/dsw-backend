package br.com.dswbackend.dtos;

import jakarta.validation.constraints.NotNull;

public record QuadroRequest(
    @NotNull(message = "A campo cor de fundo não deve ser nulo") String corFundo,
    @NotNull(message = "A campo cor do texto não deve ser nulo") String corTexto,
    @NotNull(message = "A campo titulo não deve ser nulo") String titulo) {

}
