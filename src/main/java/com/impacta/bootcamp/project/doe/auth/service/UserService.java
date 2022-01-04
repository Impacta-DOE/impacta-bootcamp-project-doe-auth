package com.impacta.bootcamp.project.doe.auth.service;

import com.impacta.bootcamp.project.doe.auth.controller.dto.UserDto;
import com.impacta.bootcamp.project.doe.auth.model.User;
import com.impacta.bootcamp.project.doe.auth.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = repository.findByUsername(username);
        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("UserName" + username + "not found");
        }
    }

    public UserDto salva(UserDto dto) {
        User user = dto.converte();
        user.encriptografaSenha();
        user.setAccountNonLocked(true);
        user.setEnabled(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonExpired(true);
        User userSalved = repository.save(user);
        return new UserDto(userSalved);
    }
}
