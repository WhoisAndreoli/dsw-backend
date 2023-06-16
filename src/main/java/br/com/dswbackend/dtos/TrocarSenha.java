package br.com.dswbackend.dtos;

import jakarta.validation.constraints.NotBlank;

public record TrocarSenha(@NotBlank String senhaAntiga, @NotBlank String senhaNova, @NotBlank String senhaRepetida) {

}
