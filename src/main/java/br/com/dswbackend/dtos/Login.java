package br.com.dswbackend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record Login(
    @NotBlank(message = "O campo email deve ser preenchido") @Email(message = "Email com o formato inválido") String email,
    String senha) {

}
