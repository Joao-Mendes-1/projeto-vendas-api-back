package com.joaoMendes.vendas_api.domain.repository;

import com.joaoMendes.vendas_api.domain.entities.Venda;
import com.joaoMendes.vendas_api.domain.entities.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findByVendedor(Vendedor vendedor);

    List<Venda> findByVendedorAndDataVendaBetween(Vendedor vendedor, LocalDate inicio, LocalDate fim);
}
