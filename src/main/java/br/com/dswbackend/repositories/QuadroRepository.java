package br.com.dswbackend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.dswbackend.model.Quadro;

public interface QuadroRepository extends MongoRepository<Quadro, String> {

}
