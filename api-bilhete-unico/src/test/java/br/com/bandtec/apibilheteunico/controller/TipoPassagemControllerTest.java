package br.com.bandtec.apibilheteunico.controller;

import br.com.bandtec.apibilheteunico.entity.TipoPassagem;
import br.com.bandtec.apibilheteunico.repository.TipoPassagemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class TipoPassagemControllerTest {

    @Autowired
    private TipoPassagemController tipoPassagemController;

    @MockBean
    private TipoPassagemRepository passagemRepository;

    // 1° Metodo
    @Test
    @DisplayName("POST /tipo-passagem - Cadastrar passagem com mesma descricao, status 400")
    void postPassagemDescricaoIgual() {
        // Given
        TipoPassagem tipoPassagem1Teste = new TipoPassagem();
        tipoPassagem1Teste.setId(1);
        tipoPassagem1Teste.setDescricao("BOM");
        tipoPassagem1Teste.setValor(4.75);

        TipoPassagem tipoPassagem2Teste = new TipoPassagem();
        tipoPassagem2Teste.setId(2);
        tipoPassagem2Teste.setDescricao("BOM");
        tipoPassagem1Teste.setValor(6.80);

        // When
        Mockito.when(passagemRepository
                .findPassagemByDescricao(tipoPassagem2Teste.getDescricao()))
                .thenReturn(tipoPassagem1Teste);

        ResponseEntity resposta2 = tipoPassagemController.postPassagem(tipoPassagem2Teste);

        // Then
        Assertions.assertEquals(400, resposta2.getStatusCodeValue());
        Assertions.assertEquals("Este tipo de passagem já está cadastrado!", resposta2.getBody());
    }

    @Test
    @DisplayName("POST /tipo-passagem - Quando cadastrado com sucesso, status 201")
    void postPassagemCadastrada() {
        // Given
        TipoPassagem tipoPassagemTeste = new TipoPassagem();
        tipoPassagemTeste.setId(1);
        tipoPassagemTeste.setDescricao("BOM");
        tipoPassagemTeste.setValor(4.75);

        // When
        Mockito.when(passagemRepository.findPassagemByDescricao(tipoPassagemTeste.getDescricao())).thenReturn(null);
        ResponseEntity resposta = tipoPassagemController.postPassagem(tipoPassagemTeste);

        // Then
        Assertions.assertEquals(201, resposta.getStatusCodeValue());
    }
}