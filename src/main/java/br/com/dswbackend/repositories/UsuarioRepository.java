package br.com.dswbackend.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.dswbackend.model.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {

  Optional<Usuario> findByEmail(String email);
}
