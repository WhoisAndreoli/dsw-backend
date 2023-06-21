package br.com.dswbackend.dtos;

import jakarta.validation.constraints.NotNull;

public record ListaRequest(@NotNull(message = "O campo titulo não deve ser nulo") String titulo) {

}
