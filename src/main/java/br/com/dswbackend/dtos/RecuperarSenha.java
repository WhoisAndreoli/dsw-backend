package br.com.dswbackend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RecuperarSenha(@NotBlank @Email String email, @NotBlank String senha, @NotBlank String senhaRepetida,
    @NotBlank String expiracao) {

}
