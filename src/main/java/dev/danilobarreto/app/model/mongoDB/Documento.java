package dev.danilobarreto.app.model.mongoDB;

import jakarta.persistence.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "documentos")
public class Documento {

    @Id
    private ObjectId id;
    private String nomeArquivo;
    private String tipo;
    private long tamanho;

    public Documento(ObjectId id, String nomeArquivo, String tipo, long tamanho) {
        this.id = id;
        this.nomeArquivo = nomeArquivo;
        this.tipo = tipo;
        this.tamanho = tamanho;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public long getTamanho() {
        return tamanho;
    }

    public void setTamanho(long tamanho) {
        this.tamanho = tamanho;
    }
}
