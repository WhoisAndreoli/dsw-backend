package br.com.dswbackend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioRequest(
    @NotBlank(message = "O campo nome deve ser preenchido") String nome,
    @NotBlank(message = "O campo email deve ser preenchido") @Email(message = "Email com o formato inválido") String email,
    @NotBlank(message = "A campo senha deve ser preenchida") String senha) {
}
