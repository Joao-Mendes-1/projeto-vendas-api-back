package com.joaoMendes.vendas_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joaoMendes.vendas_api.domain.service.VendaService;
import com.joaoMendes.vendas_api.dto.request.VendaRequest;
import com.joaoMendes.vendas_api.dto.response.VendaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

@WebMvcTest(VendaController.class)
class VendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VendaService service;

    @Autowired
    private ObjectMapper mapper;

    private Long ID_VENDA;
    private Long ID_VENDEDOR;
    private BigDecimal VALOR;

    private VendaRequest vendaRequest;
    private VendaResponse vendaResponse;

    @BeforeEach
    void setup() {
        ID_VENDA = 1L;
        ID_VENDEDOR = 10L;
        VALOR = new BigDecimal("500.00");

        vendaRequest = new VendaRequest(
                LocalDate.of(2025, 11, 20),
                VALOR,
                ID_VENDEDOR
        );

        vendaResponse = new VendaResponse(
                ID_VENDA,
                vendaRequest.getDataVenda(),
                vendaRequest.getValor(),
                ID_VENDEDOR,
                "vendedorNome"
        );
    }

}
