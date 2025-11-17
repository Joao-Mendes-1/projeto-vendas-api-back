package com.joaoMendes.vendas_api.domain.repository;

import com.joaoMendes.vendas_api.domain.entities.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaRepository extends JpaRepository<Venda, Long> {

}
