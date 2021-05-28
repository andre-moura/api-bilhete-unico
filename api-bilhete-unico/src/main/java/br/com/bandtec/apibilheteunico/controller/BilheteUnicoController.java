package br.com.bandtec.apibilheteunico.controller;

import br.com.bandtec.apibilheteunico.entity.BilheteUnico;
import br.com.bandtec.apibilheteunico.entity.TipoPassagem;
import br.com.bandtec.apibilheteunico.repository.BilheteUnicoRepository;
import br.com.bandtec.apibilheteunico.repository.TipoPassagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/bilhete-unico")
public class BilheteUnicoController {

    @Autowired
    private BilheteUnicoRepository bilheteRepository;

    @Autowired
    private TipoPassagemRepository passagemRepository;

    @PostMapping
    public ResponseEntity postBilheteUnico(@RequestBody BilheteUnico novoBilheteUnico) {
        Optional<BilheteUnico> bilheteUnico = Optional.ofNullable
                (bilheteRepository.findBilheteUnicoByCpf(novoBilheteUnico.getCpf()));
        if (!bilheteUnico.isPresent()){
            bilheteRepository.save(novoBilheteUnico);
            return ResponseEntity.status(201).build();
        } else {
            return ResponseEntity.status(400).body("Este CPF já tem 2 BUs!");
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity getBilheteUnico(@PathVariable Integer id) {
        if (bilheteRepository.existsById(id)){
            return ResponseEntity.status(200).body(bilheteRepository.getById(id));
        } else {
            return ResponseEntity.status(204).build();
        }
    }

    @PostMapping("/{id}/recarga/{valorRecarga}")
    public ResponseEntity recarregarbilhete(@PathVariable Integer id,
                                            @PathVariable Double valorRecarga){
        Optional<BilheteUnico> bilheteUnico = bilheteRepository.findById(id);
        if (bilheteUnico.isPresent()){
            if (valorRecarga < 1.0) {
                return ResponseEntity.status(400).body("Valor da recarga deve ser a partir de R$1,00");
            } else {
                Double verificarSaldo = bilheteUnico.get().getSaldo() + valorRecarga;
                if (verificarSaldo > 230.0){
                    return ResponseEntity
                            .status(400)
                            .body("Recarga não efetuada! Passaria do limite de R$230,00. " +
                                    "Você ainda pode carregar até R$"+(bilheteUnico.get().getSaldo() - 230.0));
                } else {
                    bilheteUnico.get().setSaldo(verificarSaldo);
                    bilheteRepository.save(bilheteUnico.get());
                    return ResponseEntity.status(200).build();
                }
            }
        } else {
            return ResponseEntity.status(204).build();
        }
    }

    @PostMapping("/{id}/passagem/{idTipo}")
    public ResponseEntity passarBilheteUnico(@PathVariable Integer id, @PathVariable Integer idTipo){
        Optional<BilheteUnico> bilheteUnico = bilheteRepository.findById(id);
        Optional<TipoPassagem> tipoPassagem = passagemRepository.findById(id);

        if (bilheteUnico.isPresent()) {
            if (tipoPassagem.isPresent()){
                boolean conferirSaldo = tipoPassagem.get().getValor() > bilheteUnico.get().getSaldo();
                if (!conferirSaldo){
                    Double novoSaldo = (bilheteUnico.get().getSaldo()) - tipoPassagem.get().getValor();
                    bilheteUnico.get().setSaldo(novoSaldo);
                    bilheteRepository.save(bilheteUnico.get());
                    return ResponseEntity.status(200).build();
                } else {
                    return ResponseEntity
                            .status(400)
                            .body("Saldo atual R$"+bilheteUnico.get().getSaldo()+
                                    " insuficiente para esta passagem" );
                }
            } else {
                return ResponseEntity.status(400).body("Tipo de passagem não encontrada" );
            }
        } else {
            return ResponseEntity.status(404).body("BU não encontrado");
        }
    }
}
