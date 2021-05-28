package br.com.bandtec.apibilheteunico.repository;

import br.com.bandtec.apibilheteunico.entity.BilheteUnico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BilheteUnicoRepository extends JpaRepository<BilheteUnico, Integer> {

    BilheteUnico findBilheteUnicoByCpf(String cpf);
}
