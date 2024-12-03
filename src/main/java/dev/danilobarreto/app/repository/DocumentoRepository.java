package dev.danilobarreto.app.repository;

import dev.danilobarreto.app.model.mongoDB.Documento;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DocumentoRepository extends MongoRepository<Documento, String> {
    Documento findByNomeDocumento(String nome);

    List<Documento> findByRazaoSocialAss(String razaoSocialAss);
}
