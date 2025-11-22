package com.joaoMendes.vendas_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joaoMendes.vendas_api.domain.exception.VendedorNotFoundException;
import com.joaoMendes.vendas_api.domain.service.VendedorService;
import com.joaoMendes.vendas_api.dto.request.VendedorRequest;
import com.joaoMendes.vendas_api.dto.response.VendedorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VendedorController.class)
class VendedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VendedorService vendedorService;

    @Autowired
    private ObjectMapper mapper;

    private Long ID_VENDEDOR;
    private Long ID_VENDEDOR_INEXISTENTE;
    private String NOME_VENDEDOR;
    private VendedorRequest vendedorRequest;
    private VendedorResponse vendedorResponse;

    @BeforeEach
    void setup() {
        ID_VENDEDOR = 1L;
        ID_VENDEDOR_INEXISTENTE = 20L;
        NOME_VENDEDOR ="NOME" ;
        vendedorResponse = new VendedorResponse(1L, NOME_VENDEDOR);
        vendedorRequest = new VendedorRequest(NOME_VENDEDOR);
    }

    @Test
    @DisplayName("Dado um VendedorRequest válido, Quando criar venda, Então retornar 201 com corpo correto")
    void dadoVendedorRequest_quandoCriarVendedor_entaoRetornar201() throws Exception {
        given(vendedorService.create(any(VendedorRequest.class)))
                .willReturn(vendedorResponse);

        ResultActions resposta = mockMvc.perform(
                post("/vendedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(vendedorRequest))
        );

        resposta.andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("NOME"));

        verify(vendedorService).create(any(VendedorRequest.class));
    }

    @Test
    @DisplayName("Dado um VendedorRequest inválido, Quando criar vendedor, Então retornar 400 e não chamar service")
    void dadoVendedorRequestInvalido_quandoCriarVendedor_entaoRetornar400() throws Exception {
        VendedorRequest vendedorRequestInvalido = new VendedorRequest("");

        ResultActions resposta = mockMvc.perform(
                post("/vendedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(vendedorRequestInvalido))
        );
                resposta.andExpect(status().isBadRequest());
                verify(vendedorService, never()).create(any(VendedorRequest.class));
    }

    @Test
    @DisplayName("Dado que existem vendedores, Quando listar vendedores, Então retornar 200 com lista preenchida")
    void dadoExistenciaDeVendedores_quandoListar_entaoRetornar200ComLista() throws Exception {
        given(vendedorService.getAll()).willReturn(List.of(vendedorResponse));

        ResultActions resposta = mockMvc.perform(get("/vendedores"));

        resposta.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("NOME"));

        verify(vendedorService).getAll();
    }

    @Test
    @DisplayName("Dado que não existem vendedores, Quando listar vendedores, Então retornar 200 com lista vazia")
    void dadoNenhumVendedor_quandoListar_entaoRetornar200ComListaVazia() throws Exception {
        given(vendedorService.getAll()).willReturn(List.of());

        ResultActions resposta = mockMvc.perform(get("/vendedores"));

        resposta.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(vendedorService).getAll();

    }
    @Test
    @DisplayName("Dado um ID existente, Quando buscar vendedor por ID, Então retornar 200 com corpo correto")
    void dadoIdExistente_quandoBuscarPorId_entaoRetornar200() throws Exception {

        given(vendedorService.getById(ID_VENDEDOR)).willReturn(vendedorResponse);

        ResultActions resposta = mockMvc.perform(get("/vendedores/{id}", ID_VENDEDOR));

        resposta.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID_VENDEDOR));

        verify(vendedorService).getById(ID_VENDEDOR);
    }

    @Test
    @DisplayName("Dado um ID inexistente, Quando buscar vendedor por ID, Então retornar VendedorNotFoundException")
    void dadoIdInexistente_quandoBuscarPorId_entaoRetornarVendedorNotFoundException() throws Exception {

        given(vendedorService.getById(ID_VENDEDOR_INEXISTENTE)).willThrow(new VendedorNotFoundException(ID_VENDEDOR_INEXISTENTE));

        ResultActions resposta = mockMvc.perform(get("/vendedores/{id}", ID_VENDEDOR_INEXISTENTE));

        resposta.andExpect(status().isNotFound());

        verify(vendedorService).getById(ID_VENDEDOR_INEXISTENTE);
    }

    @Test
    @DisplayName("Dado um ID existente, Quando for deletar por ID, Então retornar 204")
    void dadoIdExistente_quandoDeletarPorId_entaoRetornar204() throws Exception{

        ResultActions resposta = mockMvc.perform(
                delete("/vendedores/{id}", ID_VENDEDOR)
        );

        resposta.andExpect(status().isNoContent());
        verify(vendedorService).delete(ID_VENDEDOR);

    }

    @Test
    @DisplayName("Dado um ID inexistente, Quando for deletar por ID, Então retornar VendedorNotFoundException")
    void dadoIdInexistente_quandoDeletarPorId_entaoRetornarVendedorNotFoundException() throws Exception{

        doThrow(new VendedorNotFoundException(ID_VENDEDOR_INEXISTENTE)).when(vendedorService).delete(ID_VENDEDOR_INEXISTENTE);

        ResultActions resposta = mockMvc.perform(
                delete("/vendedores/{id}", ID_VENDEDOR_INEXISTENTE));

        resposta.andExpect(status().isNotFound());
        verify(vendedorService).delete(ID_VENDEDOR_INEXISTENTE);

    }

    @Test
    @DisplayName("Dado um VendedorRequest válido, Quando atualizar vendedor, Então retornar 200 com corpo atualizado")
    void dadoRequestValido_quandoAtualizarVendedor_entaoRetornar200() throws Exception {

        long id = 2L;
        VendedorRequest request = new VendedorRequest("NOVO_NOME");
        VendedorResponse response = new VendedorResponse(id, "NOVO_NOME");

        given(vendedorService.update(eq(id), any(VendedorRequest.class)))
                .willReturn(response);

        ResultActions resposta = mockMvc.perform(
                put("/vendedores/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
        );

        resposta.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("NOVO_NOME"));

        verify(vendedorService).update(eq(id), any(VendedorRequest.class));
    }

    @Test
    @DisplayName("Dado um VendedorRequest inválido, Quando atualizar vendedor, Então retornar 400")
    void dadoRequestInvalido_quandoAtualizar_entaoRetornar400() throws Exception {

        long id = 2L;
        VendedorRequest requestInvalido = new VendedorRequest("");

        ResultActions resposta = mockMvc.perform(
                put("/vendedores/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestInvalido))
        );

        resposta.andExpect(status().isBadRequest());

        verify(vendedorService, never()).update(anyLong(), any(VendedorRequest.class));
    }

    @Test
    @DisplayName("Dado um id inválido, Quando atualizar vendedor, Então retornar VendedorNotFoundException")
    void dadoIdInvalido_quandoAtualizar_entaoRetornarVendedorNotFoundException() throws Exception {
        VendedorRequest request = new VendedorRequest("NOVO_NOME");

        given(vendedorService.update(eq(ID_VENDEDOR_INEXISTENTE), any(VendedorRequest.class)))
                .willThrow(new VendedorNotFoundException(ID_VENDEDOR_INEXISTENTE));

        ResultActions resposta = mockMvc.perform(
                put("/vendedores/{id}", ID_VENDEDOR_INEXISTENTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
        );
        resposta.andExpect(status().isNotFound());
        verify(vendedorService, never()).update(ID_VENDEDOR_INEXISTENTE, request);

    }

}































