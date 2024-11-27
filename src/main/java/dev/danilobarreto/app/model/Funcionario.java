package dev.danilobarreto.app.model;

import dev.danilobarreto.app.model.audit.UserDateAudit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "funcionario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Funcionario extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String telefone;

    @Enumerated(EnumType.STRING)
    private Cargo funcao;

    

}
