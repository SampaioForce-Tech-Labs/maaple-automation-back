package dev.danilobarreto.app.service;

import dev.danilobarreto.app.model.Role;
import dev.danilobarreto.app.model.User;
import dev.danilobarreto.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {


    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Set<Role> buscarPermissao (Long id){
        User user = repository.findById(id).get();
        return user.getRoles();
    }


}
