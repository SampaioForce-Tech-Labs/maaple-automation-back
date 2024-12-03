package dev.danilobarreto.app.repository;

import dev.danilobarreto.app.model.mongoDB.Documento;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentoRepository extends MongoRepository<Documento, String> {
    Documento findByNome(String nome);
}
