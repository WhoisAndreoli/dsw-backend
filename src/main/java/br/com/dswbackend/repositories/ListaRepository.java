package br.com.dswbackend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.dswbackend.model.Lista;

public interface ListaRepository extends MongoRepository<Lista, String> {

}
