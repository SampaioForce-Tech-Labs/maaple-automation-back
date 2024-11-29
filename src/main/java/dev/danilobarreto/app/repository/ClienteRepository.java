package dev.danilobarreto.app.repository;


import dev.danilobarreto.app.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByRazaoSocial(String razaoSocial);

    List<Cliente> findByNomeContainingIgnoreCase(String nome);

}
