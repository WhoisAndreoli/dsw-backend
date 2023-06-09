package br.com.dswbackend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioRequest(
    @NotBlank String nome,
    @NotBlank @Email String email,
    @NotBlank String senha) {
}
