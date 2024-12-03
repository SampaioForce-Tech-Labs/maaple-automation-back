package dev.danilobarreto.app.model.mongoDB;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Documento {

    @Id
    private String id;
    private String nomeDocumento;
    private String razaoSocialAss;
    private String tipo;
    private byte[] conteudo;

    // Construtores, getters e setters
    public Documento(String nome, String tipo, byte[] conteudo) {
        this.nomeDocumento = nome;
        this.tipo = tipo;
        this.conteudo = conteudo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeDocumento() {
        return nomeDocumento;
    }

    public void setNomeDocumento(String nomeDocumento) {
        this.nomeDocumento = nomeDocumento;
    }

    public String getRazaoSocialAss() {
        return razaoSocialAss;
    }

    public void setRazaoSocialAss(String razaoSocialAss) {
        this.razaoSocialAss = razaoSocialAss;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public byte[] getConteudo() {
        return conteudo;
    }

    public void setConteudo(byte[] conteudo) {
        this.conteudo = conteudo;
    }
}
