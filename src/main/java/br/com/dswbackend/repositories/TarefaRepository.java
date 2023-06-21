package br.com.dswbackend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.dswbackend.model.Tarefa;

public interface TarefaRepository extends MongoRepository<Tarefa, String> {

}
