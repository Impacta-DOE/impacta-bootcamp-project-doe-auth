package com.impacta.bootcamp.project.doe.auth.exceptions;

public class UsernameJaExisteException extends RuntimeException {
    public UsernameJaExisteException() {
        super("Usuário já existe");
    }
}