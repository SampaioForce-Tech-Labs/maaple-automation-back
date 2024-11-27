package dev.danilobarreto.app.model.records;

import dev.danilobarreto.app.model.Cargo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewEmployee (
        @NotBlank
        String nome,
        @NotBlank
        String username,
        @NotBlank
        @Email
        String email,

        @NotBlank
        String telefone,

        @NotBlank
        String senha,
        @NotNull
        Cargo funcao
){
}
