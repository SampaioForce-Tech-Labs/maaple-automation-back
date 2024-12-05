package dev.danilobarreto.app.service;

import dev.danilobarreto.app.model.Funcionario;
import dev.danilobarreto.app.model.records.NewEmployee;
import dev.danilobarreto.app.model.records.SignUpRequest;
import dev.danilobarreto.app.repository.FuncionarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final AuthService authService;

    @Autowired
    public FuncionarioService(FuncionarioRepository funcionarioRepository, AuthService authService) {
        this.funcionarioRepository = funcionarioRepository;
        this.authService = authService;
    }

    public List<Funcionario> listaTodos() {
        return funcionarioRepository.findAll();
    }

    public Funcionario editarFuncionario(Long id) {
        return funcionarioRepository.findById(id).orElseThrow( () -> new RuntimeException("Funcionário não encontrado"));
    }

    @Transactional
    public void cadastraFuncionario(NewEmployee newEmployee) {
        Funcionario funcionario = new Funcionario(null,
                newEmployee.nome(),
                newEmployee.telefone(),
                newEmployee.funcao()
        );
        funcionarioRepository.save(funcionario);
        SignUpRequest signUpRequest = new SignUpRequest(
                newEmployee.nome(),
                newEmployee.username(),
                newEmployee.email(),
                newEmployee.senha()
        );
        authService.cadastro(signUpRequest);
    }

    public Funcionario createOrUpdate(Funcionario funcionario) {
        return funcionarioRepository.save(funcionario);
    }

    public void deletarFuncionario(Long id) {
        funcionarioRepository.deleteById(id);
    }

}
