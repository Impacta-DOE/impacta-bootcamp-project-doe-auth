package com.impacta.bootcamp.project.doe.auth.security;

import java.io.Serializable;
import java.util.Objects;

public class AutorizeVO implements Serializable {
    public static final long serialVersionUID = 1L;
    public String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AutorizeVO that = (AutorizeVO) o;
        return Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
