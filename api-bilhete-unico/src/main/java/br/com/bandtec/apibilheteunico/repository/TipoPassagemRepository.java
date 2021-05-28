package br.com.bandtec.apibilheteunico.repository;

import br.com.bandtec.apibilheteunico.entity.TipoPassagem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoPassagemRepository extends JpaRepository<TipoPassagem, Integer> {

    TipoPassagem findPassagemByDescricao(String descricao);
}
