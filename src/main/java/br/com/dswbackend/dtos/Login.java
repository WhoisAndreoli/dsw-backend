package br.com.dswbackend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record Login(@NotBlank @Email String email, String senha) {

}
