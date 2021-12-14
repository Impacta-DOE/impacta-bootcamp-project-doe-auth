package com.impacta.bootcamp.project.doe.auth.controller.dto;

import com.impacta.bootcamp.project.doe.auth.model.User;

public class UserDto {
    private Long id;
    private String username;
    private String senha;

    public UserDto() {
    }

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.senha = user.getPassword();
    }

    public User converte() {
        User user = new User();
        user.setUserName(this.username);
        user.setPassword(this.senha);
        return user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
