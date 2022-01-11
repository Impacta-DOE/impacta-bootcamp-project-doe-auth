package com.impacta.bootcamp.project.doe.auth.controller;

import com.impacta.bootcamp.project.doe.auth.controller.dto.UserDto;
import com.impacta.bootcamp.project.doe.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/usuario")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping
    public UserDto salva(@RequestBody UserDto dto) {
        return userService.salva(dto);
    }

    @GetMapping("/username/{id}")
    public String buscaUsernamePorIdDoUsuario(@PathVariable String id){
        return userService.buscaUsernamePor(id);
    }

}
