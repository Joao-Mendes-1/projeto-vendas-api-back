package com.joaoMendes.vendas_api.domain.repository;

import com.joaoMendes.vendas_api.domain.entities.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendedorRepository extends JpaRepository<Vendedor, Long> {

    Optional<Vendedor> findByNome(String nome);
}
