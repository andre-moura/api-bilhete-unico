package br.com.bandtec.apibilheteunico.controller;

import br.com.bandtec.apibilheteunico.entity.BilheteUnico;
import br.com.bandtec.apibilheteunico.entity.TipoPassagem;
import br.com.bandtec.apibilheteunico.repository.BilheteUnicoRepository;
import br.com.bandtec.apibilheteunico.repository.TipoPassagemRepository;
import javafx.beans.binding.When;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BilheteUnicoControllerTest {

    @Autowired
    private BilheteUnicoController bilheteUnicoController;

    @MockBean
    private BilheteUnicoRepository bilheteUnicoRepository;

    @MockBean
    private TipoPassagemRepository tipoPassagemRepository;

     // 1° Metodo
    @Test
    @DisplayName("POST /bilhete-unico - Quando cadastrar com sucesso, status 201")
    void postBilheteUnicoCadastrado() {
        // Given
        BilheteUnico bilheteUnico = new BilheteUnico();
        bilheteUnico.setId(1);
        bilheteUnico.setPassageiro("Joãozinho");
        bilheteUnico.setNascimento(LocalDate.now());
        bilheteUnico.setCpf("52418634604");
        bilheteUnico.setSaldo(130.0);

        // When
        Mockito.when(bilheteUnicoRepository.findBilheteUnicoByCpf(bilheteUnico.getCpf())).thenReturn(null);
        ResponseEntity resposta = bilheteUnicoController.postBilheteUnico(bilheteUnico);

        // Then
        Assertions.assertEquals(201, resposta.getStatusCodeValue());
    }

    @Test
    @DisplayName("POST /bilhete-unico - Quando cadastrar com Cpf existente, status 400")
    void postBilheteCpfExistente() {
        // Given
        BilheteUnico bilheteUnico = new BilheteUnico();
        bilheteUnico.setId(1);
        bilheteUnico.setPassageiro("Joãozinho");
        bilheteUnico.setNascimento(LocalDate.now());
        bilheteUnico.setCpf("52418634604");
        bilheteUnico.setSaldo(130.0);

        BilheteUnico bilheteUnico2 = new BilheteUnico();
        bilheteUnico2.setId(1);
        bilheteUnico2.setPassageiro("Joãozinho");
        bilheteUnico2.setNascimento(LocalDate.now());
        bilheteUnico2.setCpf("52418634604");
        bilheteUnico2.setSaldo(55.0);

        // When
        Mockito.when(bilheteUnicoRepository.findBilheteUnicoByCpf
                (bilheteUnico.getCpf())).thenReturn(bilheteUnico2);
        ResponseEntity resposta = bilheteUnicoController.postBilheteUnico(bilheteUnico);

        // Then
        Assertions.assertEquals(400, resposta.getStatusCodeValue());
        Assertions.assertEquals("Este CPF já tem 2 BUs!", resposta.getBody());
    }

    // 2° Metodo
    @Test
    @DisplayName("GET /bilhete-unico/{id} - Quando existir bilhete, status 200")
    void getBilheteUnicoSucesso() {
        // Given
        BilheteUnico bilheteUnico = new BilheteUnico();
        bilheteUnico.setId(1);
        bilheteUnico.setPassageiro("Joãozinho");
        bilheteUnico.setNascimento(LocalDate.now());
        bilheteUnico.setCpf("52418634604");
        bilheteUnico.setSaldo(130.0);

        // When
        Mockito.when(bilheteUnicoRepository.existsById(bilheteUnico.getId())).thenReturn(true);
        Mockito.when(bilheteUnicoRepository
                .findById(bilheteUnico.getId()))
                .thenReturn(java.util.Optional.of(bilheteUnico));

        ResponseEntity resposta = bilheteUnicoController.getBilheteUnico(bilheteUnico.getId());

        // Then
        Assertions.assertEquals(200, resposta.getStatusCodeValue());
        Assertions.assertEquals(bilheteUnico, resposta.getBody());
    }

    @Test
    @DisplayName("GET /bilhete-unico/{id} - Quando existir bilhete, status 204")
    void getBilheteUnicoNaoCadastrado() {
        // Given
        int idTeste = 1;

        // When
        ResponseEntity resposta = bilheteUnicoController.getBilheteUnico(idTeste);

        // Then
        Assertions.assertEquals(204, resposta.getStatusCodeValue());
    }


    // 3° Metodo
    @Test
    @DisplayName("POST /bilhete-unico/{id}/recarga/{valorRecarga} - Quando recarregar, status 200")
    void recarregarbilheteSucesso() {
        // Given
        BilheteUnico bilheteUnico = new BilheteUnico();
        bilheteUnico.setId(1);
        bilheteUnico.setPassageiro("Joãozinho");
        bilheteUnico.setNascimento(LocalDate.now());
        bilheteUnico.setCpf("52418634604");
        bilheteUnico.setSaldo(130.0);

        // When
        Mockito.when(bilheteUnicoRepository.
                findById(bilheteUnico.getId()))
                .thenReturn(java.util.Optional.of(bilheteUnico));

        ResponseEntity resposta = bilheteUnicoController.recarregarbilhete
                (bilheteUnico.getId(), 20.0);

        // Then
        Assertions.assertEquals(200, resposta.getStatusCodeValue());
    }

    @Test
    @DisplayName("POST /bilhete-unico/{id}/recarga/{valorRecarga} - Quando passar do saldo permitido, status 400")
    void recarregarbilhetePassouDaCarga() {
        // Given
        BilheteUnico bilheteUnico = new BilheteUnico();
        bilheteUnico.setId(1);
        bilheteUnico.setPassageiro("Joãozinho");
        bilheteUnico.setNascimento(LocalDate.now());
        bilheteUnico.setCpf("52418634604");
        bilheteUnico.setSaldo(130.0);

        // When
        Mockito.when(bilheteUnicoRepository.
                findById(bilheteUnico.getId()))
                .thenReturn(java.util.Optional.of(bilheteUnico));

        ResponseEntity resposta = bilheteUnicoController.recarregarbilhete
                (bilheteUnico.getId(), 200.0);

        // Then
        Assertions.assertEquals(400, resposta.getStatusCodeValue());
    }

    // 4° Metodo
    @Test
    @DisplayName("POST /bilhete-unico/{id}/passagem/{idTipo} - Quando passar o bilhete, status 200")
    void passarBilheteUnico() {
        // Given
        BilheteUnico bilheteUnico = new BilheteUnico();
        bilheteUnico.setId(1);
        bilheteUnico.setPassageiro("Joãozinho");
        bilheteUnico.setNascimento(LocalDate.now());
        bilheteUnico.setCpf("52418634604");
        bilheteUnico.setSaldo(130.0);

        TipoPassagem tipoPassagemTeste = new TipoPassagem();
        tipoPassagemTeste.setId(1);
        tipoPassagemTeste.setDescricao("BOM");
        tipoPassagemTeste.setValor(4.75);

        // When
        Mockito.when(bilheteUnicoRepository.
                findById(bilheteUnico.getId()))
                .thenReturn(java.util.Optional.of(bilheteUnico));

        Mockito.when(tipoPassagemRepository
                .findById(tipoPassagemTeste.getId()))
                .thenReturn(java.util.Optional.of(tipoPassagemTeste));

        ResponseEntity resposta = bilheteUnicoController
                .passarBilheteUnico(bilheteUnico.getId(), tipoPassagemTeste.getId());

        // Then
        Assertions.assertEquals(200, resposta.getStatusCodeValue());
    }

    @Test
    @DisplayName("POST /bilhete-unico/{id}/passagem/{idTipo} - Quando bilhete não tem saldo suficiente, status 400")
    void passarBilheteUnicoSaldoInsuficiente() {
        // Given
        BilheteUnico bilheteUnico = new BilheteUnico();
        bilheteUnico.setId(1);
        bilheteUnico.setPassageiro("Joãozinho");
        bilheteUnico.setNascimento(LocalDate.now());
        bilheteUnico.setCpf("52418634604");
        bilheteUnico.setSaldo(3.75);

        TipoPassagem tipoPassagemTeste = new TipoPassagem();
        tipoPassagemTeste.setId(1);
        tipoPassagemTeste.setDescricao("BOM");
        tipoPassagemTeste.setValor(4.75);

        // When
        Mockito.when(bilheteUnicoRepository.
                findById(bilheteUnico.getId()))
                .thenReturn(java.util.Optional.of(bilheteUnico));

        Mockito.when(tipoPassagemRepository
                .findById(tipoPassagemTeste.getId()))
                .thenReturn(java.util.Optional.of(tipoPassagemTeste));

        ResponseEntity resposta = bilheteUnicoController
                .passarBilheteUnico(bilheteUnico.getId(), tipoPassagemTeste.getId());

        // Then
        Assertions.assertEquals(400, resposta.getStatusCodeValue());

        Assertions.assertEquals("Saldo atual R$"+bilheteUnico.getSaldo()+
                " insuficiente para esta passagem", resposta.getBody());
    }
}