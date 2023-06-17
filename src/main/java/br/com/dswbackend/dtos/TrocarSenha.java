package br.com.dswbackend.dtos;

import jakarta.validation.constraints.NotBlank;

public record TrocarSenha(
    @NotBlank(message = "O campo senha antiga deve ser preenchido") String senhaAntiga,
    @NotBlank(message = "O campo nova senha deve ser preenchido") String senhaNova,
    @NotBlank(message = "O campo senha repetida deve ser preenchido") String senhaRepetida) {

}
