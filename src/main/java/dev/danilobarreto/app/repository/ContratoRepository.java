package dev.danilobarreto.app.repository;

import dev.danilobarreto.app.model.mongoDB.Contrato;
import dev.danilobarreto.app.model.mongoDB.Documento;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ContratoRepository extends MongoRepository<Contrato, String> {
    Contrato findByNomeArquivo(String nomeArquivo);

    List<Contrato> findByRazaoSocialAss(String razaoSocialAss);
}
