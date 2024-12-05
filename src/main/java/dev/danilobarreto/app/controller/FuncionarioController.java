package dev.danilobarreto.app.controller;

import dev.danilobarreto.app.model.Cargo;
import dev.danilobarreto.app.model.Funcionario;
import dev.danilobarreto.app.model.records.NewEmployee;
import dev.danilobarreto.app.service.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RequestMapping("/api/funcionario")
@RestController
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @GetMapping("/cargos")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Cargo> getCargos() {
        return Arrays.asList(Cargo.values());
    }

    @PostMapping("/cadastrar")
    @PreAuthorize("hasRole('ADMIN')")
    public String cadastrarFuncionario(@Valid @RequestBody NewEmployee newEmployee) {
        funcionarioService.cadastraFuncionario(newEmployee);
        return "Funcionário cadastrado com sucesso!";
    }

    @GetMapping("/listar")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Funcionario> listarFuncionarios() {
        return funcionarioService.listaTodos();
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Funcionario editarFuncionario(@PathVariable Long id){
        return funcionarioService.editarFuncionario(id);
    }

    @PutMapping("/alterar")
    @PreAuthorize("hasRole('ADMIN')")
    public Funcionario update(@RequestBody Funcionario funcionario){return funcionarioService.createOrUpdate(funcionario);}

    @DeleteMapping("/deletar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletarFuncionario(@PathVariable Long id){
        funcionarioService.deletarFuncionario(id);
    }
}
