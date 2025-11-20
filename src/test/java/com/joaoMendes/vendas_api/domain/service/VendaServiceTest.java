package com.joaoMendes.vendas_api.domain.service;

import com.joaoMendes.vendas_api.domain.entities.Venda;
import com.joaoMendes.vendas_api.domain.entities.Vendedor;
import com.joaoMendes.vendas_api.domain.exception.PeriodoInvalidoException;
import com.joaoMendes.vendas_api.domain.exception.VendaNotFoundException;
import com.joaoMendes.vendas_api.domain.exception.VendedorNotFoundException;
import com.joaoMendes.vendas_api.domain.repository.VendaRepository;
import com.joaoMendes.vendas_api.domain.repository.VendedorRepository;
import com.joaoMendes.vendas_api.dto.request.MediaPorPeriodoRequest;
import com.joaoMendes.vendas_api.dto.request.VendaRequest;
import com.joaoMendes.vendas_api.dto.response.MediaPorPeriodoResponse;
import com.joaoMendes.vendas_api.dto.response.VendaResponse;
import com.joaoMendes.vendas_api.mapper.VendaMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendaServiceTest {

    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private VendedorRepository vendedorRepository;

    @Mock
    private VendaMapper mapper;

    @InjectMocks
    private VendaService vendaService;

    private Vendedor vendedor;
    private Venda vendaEntity;
    private Venda vendaAtualizada;
    private Venda vendaSalva;
    private VendaResponse vendaResponse;

    private VendaRequest vendaRequest;

    private final Long ID_VENDA = 1L;
    private final Long ID_VENDEDOR = 10L;
    private final BigDecimal VALOR = new BigDecimal("500.00");

    @BeforeEach
    void setup() {

        vendedor = new Vendedor(ID_VENDEDOR, "Roberto");

        vendaRequest = new VendaRequest(
                LocalDate.of(2025, 11, 20),
                VALOR,
                ID_VENDEDOR
        );

        vendaEntity = new Venda(
                null,
                vendaRequest.getDataVenda(),
                vendaRequest.getValor(),
                vendedor
        );

        vendaAtualizada = new Venda(
                null,
                LocalDate.of(2025, 11, 21),
                new BigDecimal("999.99"),
                vendedor
        );

        vendaSalva = new Venda(
                ID_VENDA,
                vendaRequest.getDataVenda(),
                vendaRequest.getValor(),
                vendedor
        );

        vendaResponse = new VendaResponse(
                ID_VENDA,
                vendaSalva.getDataVenda(),
                vendaSalva.getValor(),
                ID_VENDEDOR,
                vendedor.getNome()
        );
    }



    
}
