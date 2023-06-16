package br.com.dswbackend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.dswbackend.dtos.Login;
import br.com.dswbackend.dtos.RecuperarSenha;
import br.com.dswbackend.dtos.TrocarSenha;
import br.com.dswbackend.dtos.UsuarioRequest;
import br.com.dswbackend.dtos.UsuarioResponse;
import br.com.dswbackend.services.IUsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/usuario")
public class UsuarioController {

  private IUsuarioService service;

  public UsuarioController(IUsuarioService service) {
    this.service = service;
  }

  @PostMapping("/create")
  public ResponseEntity<UsuarioResponse> create(@Valid @RequestBody UsuarioRequest usuario)
      throws IllegalAccessException {
    return new ResponseEntity<>(service.create(usuario), HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public void login() {
    // path para fazer login
  }

  @GetMapping("/get")
  public List<UsuarioResponse> get() {
    return service.get();
  }

  @PostMapping("/forgot")
  public String forgotPassword(@Valid @RequestBody Login login) {
    return service.forgotPassword(login);
  }

  @PostMapping("/reset")
  public UsuarioResponse createNewPassword(@Valid @RequestBody RecuperarSenha form) {
    return service.createNewPassword(form);
  }

  @PutMapping("/change")
  public UsuarioResponse changePassword(@RequestBody TrocarSenha form) {
    return service.changePassword(form);
  }
}
