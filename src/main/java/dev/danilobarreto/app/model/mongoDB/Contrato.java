package dev.danilobarreto.app.model.mongoDB;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "contratos")
public class Contrato {
    @Id
    private String id;

    @Setter
    @Getter
    private String razaoSocialAss;

    @Setter
    @Getter
    private String nomeArquivo;

    @Setter
    @Getter
    private byte[] conteudoPdf;

}
