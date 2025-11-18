package com.joaoMendes.vendas_api.domain.repository;

import com.joaoMendes.vendas_api.domain.entities.Venda;
import com.joaoMendes.vendas_api.domain.entities.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findByVendedorAndDataVendaBetween(Vendedor vendedor, LocalDateTime dataInicio, LocalDateTime dataFim);
}
