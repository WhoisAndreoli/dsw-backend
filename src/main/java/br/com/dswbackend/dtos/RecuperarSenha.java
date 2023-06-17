package br.com.dswbackend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RecuperarSenha(
        @NotBlank(message = "O campo email deve ser preenchido") @Email(message = "Email com formato inválido") String email,
        @NotBlank(message = "O campo senha deve ser preenchido") String senha,
        @NotBlank(message = "O campo senha repetida deve ser preenchido") String senhaRepetida,
        @NotBlank(message = "O campo expiração deve ser preenchido") String expiracao) {

}
