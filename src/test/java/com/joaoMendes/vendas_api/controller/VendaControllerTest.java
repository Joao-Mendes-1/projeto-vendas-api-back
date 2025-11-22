package com.joaoMendes.vendas_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joaoMendes.vendas_api.domain.service.VendaService;
import com.joaoMendes.vendas_api.dto.request.MediaPorPeriodoRequest;
import com.joaoMendes.vendas_api.dto.request.VendaRequest;
import com.joaoMendes.vendas_api.dto.response.MediaPorPeriodoResponse;
import com.joaoMendes.vendas_api.dto.response.VendaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VendaController.class)
class VendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VendaService vendaService;

    @Autowired
    private ObjectMapper mapper;

    private Long ID_VENDA;
    private Long ID_VENDEDOR;
    private BigDecimal VALOR;

    private VendaRequest vendaRequest;
    private VendaResponse vendaResponse;
    private MediaPorPeriodoResponse mediaResponse;

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

        mediaResponse = new MediaPorPeriodoResponse(
                ID_VENDEDOR,
                "vendedorNome",
                new BigDecimal("205.00"),
                new BigDecimal("20.50"),
                LocalDate.of(2025, 11, 1),
                LocalDate.of(2025, 11, 20),
                20L,
                10L
        );
    }

    @Test
    @DisplayName("Dado um VendaRequest válido, Quando criar venda, Então retornar 201 com corpo correto")
    void dadoVendaRequest_quandoCriarVenda_entaoRetornar201() throws Exception {

        given(vendaService.create(any(VendaRequest.class)))
                .willReturn(vendaResponse);

        ResultActions resposta = mockMvc.perform(
                post("/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(vendaRequest))
        );

        resposta.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ID_VENDA))
                .andExpect(jsonPath("$.idVendedor").value(ID_VENDEDOR))
                .andExpect(jsonPath("$.valor").value(VALOR.doubleValue()))
                .andExpect(jsonPath("$.nomeVendedor").value("vendedorNome"));

        verify(vendaService).create(any(VendaRequest.class));
    }

    @Test
    @DisplayName("Dado que existem vendas, Quando listar, Então retornar lista preenchida")
    void dadoExistemVendas_quandoListar_entaoRetornarListaPreenchida() throws Exception {
        given(vendaService.getAll()).willReturn(List.of(vendaResponse));

        ResultActions resposta = mockMvc.perform(get("/vendas"));

        resposta.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(ID_VENDA));

        verify(vendaService).getAll();
    }

    @Test
    @DisplayName("Dado que não existem vendas, Quando listar, Então retornar lista vazia")
    void dadoNenhumaVenda_quandoListar_entaoRetornarListaVazia() throws Exception {
        given(vendaService.getAll()).willReturn(List.of());

        ResultActions resposta = mockMvc.perform(get("/vendas"));

        resposta.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(vendaService).getAll();
    }

    @Test
    @DisplayName("Dado id do vendedor, Quando buscar vendas, Então retornar lista do vendedor")
    void dadoIdVendedor_quandoBuscarVendas_entaoRetornarLista() throws Exception {
        given(vendaService.getVendasPorVendedorById(ID_VENDEDOR))
                .willReturn(List.of(vendaResponse));

        ResultActions resposta = mockMvc.perform(
                get("/vendas/vendedor/{id}", ID_VENDEDOR)
        );

        resposta.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].idVendedor").value(ID_VENDEDOR));

        verify(vendaService).getVendasPorVendedorById(ID_VENDEDOR);
    }

    @Test
    @DisplayName("Dado id válido, Quando buscar venda, Então retornar venda")
    void dadoIdValido_quandoBuscarPorId_entaoRetornarVenda() throws Exception {
        given(vendaService.getById(ID_VENDA)).willReturn(vendaResponse);

        ResultActions resposta = mockMvc.perform(
                get("/vendas/{id}", ID_VENDA)
        );

        resposta.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID_VENDA));

        verify(vendaService).getById(ID_VENDA);
    }

    @Test
    @DisplayName("Dado dados atualizados, Quando atualizar venda, Então retornar venda atualizada")
    void dadoUpdateRequest_quandoAtualizar_entaoRetornarVendaAtualizada() throws Exception {

        VendaResponse atualizada = new VendaResponse(
                ID_VENDA,
                vendaRequest.getDataVenda(),
                new BigDecimal("999.00"),
                ID_VENDEDOR,
                "vendedorEditado"
        );

        given(vendaService.update(eq(ID_VENDA), any(VendaRequest.class)))
                .willReturn(atualizada);

        ResultActions resposta = mockMvc.perform(
                put("/vendas/{id}", ID_VENDA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(vendaRequest))
        );

        resposta.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID_VENDA))
                .andExpect(jsonPath("$.nomeVendedor").value("vendedorEditado"))
                .andExpect(jsonPath("$.valor").value(999.00));

        verify(vendaService).update(eq(ID_VENDA), any(VendaRequest.class));
    }

    @Test
    @DisplayName("Dado id válido, Quando deletar venda, Então retornar 204")
    void dadoIdValido_quandoDeletar_entao204() throws Exception {

        ResultActions resposta = mockMvc.perform(
                delete("/vendas/{id}", ID_VENDA)
        );

        resposta.andExpect(status().isNoContent());

        verify(vendaService).delete(ID_VENDA);
    }

    @Test
    @DisplayName("Dado idVendedor e filtro válido, Quando calcular estatística, Então retornar dados de média")
    void dadoFiltro_quandoCalcularEstatistica_entaoRetornarMedia() throws Exception {

        given(vendaService.calcularMediaDiaria(eq(ID_VENDEDOR), any(MediaPorPeriodoRequest.class)))
                .willReturn(mediaResponse);

        String filtroJson = """
                {
                  "dataInicio": "01/11/2025",
                  "dataFim": "20/11/2025"
                }
                """;

        ResultActions resposta = mockMvc.perform(
                post("/vendas/{idVendedor}/estatistica", ID_VENDEDOR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filtroJson)
        );

        resposta.andExpect(status().isOk())
                .andExpect(jsonPath("$.idVendedor").value(ID_VENDEDOR))
                .andExpect(jsonPath("$.nomeVendedor").value("vendedorNome"))
                .andExpect(jsonPath("$.totalVendido").value(205.00))
                .andExpect(jsonPath("$.mediaDiaria").value(20.50))
                .andExpect(jsonPath("$.dias").value(20))
                .andExpect(jsonPath("$.quantidadeVendas").value(10));

        verify(vendaService).calcularMediaDiaria(eq(ID_VENDEDOR), any(MediaPorPeriodoRequest.class));
    }
}