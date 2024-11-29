package dev.danilobarreto.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "cliente")
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cnpjCpf;
    private String razaoSocial;
    private String bairro;
    private String cidade;
    private String estado;
    private String telefone;
    private String email;
    private String cep;

    @Column(name = "data_inclusao")
    @Temporal(TemporalType.DATE)
    private LocalDate dataInclusao;

    private String endereco;
}
