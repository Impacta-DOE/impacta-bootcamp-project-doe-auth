package com.impacta.bootcamp.project.doe.auth.repository.pessoa;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class PessoaServiceClient implements PessoaRepository {

    @Value("${url.api.pessoa}")
    private String baseURL;

    @Override
    public String buscaIdPessoaPorIdUsuario(String idUsuario) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<PessoaJSON> response = restTemplate.exchange(
                this.baseURL + "/pessoa/usuario/" + idUsuario + "/pessoa/id",
                HttpMethod.GET, null, PessoaJSON.class
        );

        if (response.getBody() != null) return response.getBody().getIdPessoa();
        return null;
    }
}
