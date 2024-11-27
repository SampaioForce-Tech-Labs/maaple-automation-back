package dev.danilobarreto.app.controller;

import dev.danilobarreto.app.model.Role;
import dev.danilobarreto.app.model.records.UserSummary;
import dev.danilobarreto.app.repository.UserRepository;
import dev.danilobarreto.app.security.CurrentUser;
import dev.danilobarreto.app.security.UserPrincipal;
import dev.danilobarreto.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository repository;

    private final UserService service;

    @Autowired
    public UserController(UserRepository repository, UserService service) {
        this.repository = repository;
        this.service = service;
    }


    @GetMapping("/me")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName(), currentUser.getEmail());
    }

    @GetMapping("/permissao")
    public Set<Role> buscarPermissao(@CurrentUser UserPrincipal currentUser) {
        return service.buscarPermissao(currentUser.getId());
    }
}
