package br.com.bandtec.apibilheteunico.controller;

import br.com.bandtec.apibilheteunico.entity.TipoPassagem;
import br.com.bandtec.apibilheteunico.repository.TipoPassagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/tipo-passagem")
public class TipoPassagemController {

    @Autowired
    private TipoPassagemRepository tipoPassagemRepository;


    @PostMapping
    public ResponseEntity postPassagem(@RequestBody TipoPassagem novoTipoPassagem){
        Optional<TipoPassagem> passagem = Optional.ofNullable
                (tipoPassagemRepository.findPassagemByDescricao(novoTipoPassagem.getDescricao()));
        if (!passagem.isPresent()){
            tipoPassagemRepository.save(novoTipoPassagem);
            return ResponseEntity.status(201).build();
        } else {
            return ResponseEntity.status(400).body("Este tipo de passagem já está cadastrado!");
        }
    }
}
