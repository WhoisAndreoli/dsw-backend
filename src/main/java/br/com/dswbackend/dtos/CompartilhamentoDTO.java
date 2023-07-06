package br.com.dswbackend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CompartilhamentoDTO(
        @NotNull(message = "O campo quadro id deve ser preenchido") String quadroID,
        @NotNull(message = "O campo editavel deve ser preenchido") boolean editavel,
        @NotNull(message = "O campo email deve ser preenchido") @Email(message = "O campo email esta com o formato inválido") String email) {

}
