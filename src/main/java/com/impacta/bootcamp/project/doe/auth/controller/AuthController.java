package com.impacta.bootcamp.project.doe.auth.controller;

import com.impacta.bootcamp.project.doe.auth.repository.pessoa.PessoaRepository;
import com.impacta.bootcamp.project.doe.auth.repository.user.UserRepository;
import com.impacta.bootcamp.project.doe.auth.security.AccountCredentialsVO;
import com.impacta.bootcamp.project.doe.auth.security.AutorizeVO;
import com.impacta.bootcamp.project.doe.auth.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PessoaRepository pessoaRepository;

    @PostMapping(value = "/login", produces = {"application/json"},
            consumes = {"application/json"})
    public ResponseEntity login(@RequestBody AccountCredentialsVO data) {
        try {
            var username = data.getUsername();
            var password = data.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            var user = userRepository.findByUsername(username);

            String idPessoa = pessoaRepository.buscaIdPessoaPorIdUsuario(String.valueOf(user.getId()));

            var token = "";
            if (user != null) {
                token = jwtTokenProvider.createToken(username, user.getId(), user.getRoles());

            } else {
                throw new UsernameNotFoundException("usuario nao encontrado");
            }
            Map<Object, Object> model = new HashMap();
            model.put("userName", username);
            model.put("token", token);
            model.put("idPessoa", idPessoa);

            return (ResponseEntity) ResponseEntity.ok(model);

        } catch (Exception e) {
            throw new BadCredentialsException("Usuario ou senha invalidos");
        }
    }

    @PostMapping(value = "/authorize", produces = {"application/json"},
            consumes = {"application/json"})
    public ResponseEntity authorize(@RequestBody AutorizeVO data) {
        try {

            Map<Object, Object> model = new HashMap();
            if (data.getToken() != null && jwtTokenProvider.validateToken(data.getToken())) {
                Authentication auth = jwtTokenProvider.getAuthentication(data.getToken());
                if (auth != null) {
                    String username = jwtTokenProvider.getUserDetails(data.getToken()).getUsername();
                    var user = userRepository.findByUsername(username);
                    String idPessoa = pessoaRepository.buscaIdPessoaPorIdUsuario(String.valueOf(user.getId()));

                    model.put("userName", username);
                    model.put("idPessoa", idPessoa);
                }
            }


            return (ResponseEntity) ResponseEntity.ok(model);

        } catch (Exception e) {
            throw new BadCredentialsException("token invalidos");
        }
    }
}
