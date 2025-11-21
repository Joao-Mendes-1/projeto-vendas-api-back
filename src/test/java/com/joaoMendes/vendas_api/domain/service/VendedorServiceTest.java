package com.joaoMendes.vendas_api.domain.service;

import com.joaoMendes.vendas_api.domain.entities.Vendedor;
import com.joaoMendes.vendas_api.domain.exception.VendedorNotFoundException;
import com.joaoMendes.vendas_api.domain.repository.VendedorRepository;
import com.joaoMendes.vendas_api.dto.request.VendedorRequest;
import com.joaoMendes.vendas_api.dto.response.VendedorResponse;
import com.joaoMendes.vendas_api.mapper.VendedorMapper;
import com.joaoMendes.vendas_api.utils.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendedorServiceTest {

    @Mock
    private VendedorRepository vendedorRepository;

    @Mock
    private VendedorMapper vendedorMapper;

    @InjectMocks
    private VendedorService vendedorService;

    private final String NOME = "vendedorNome";
    private final String nomeNormalizado = StringUtils.normalizeString(NOME);
    private VendedorRequest requestValido;
    private Vendedor vendedorEntity;
    private Vendedor vendedorSalvo;
    private VendedorResponse vendedorResponse;

    @BeforeEach
    void setup() {
        requestValido = new VendedorRequest(NOME);
        vendedorEntity = new Vendedor(null, NOME);
        vendedorSalvo = new Vendedor(1L, NOME);
        vendedorResponse = new VendedorResponse(1L, NOME);
    }

    @Test
    void createQuandoRequestValidoRetornaVendedorResponse() {
        when(vendedorRepository.findByNomeIgnoreCase(nomeNormalizado)).thenReturn(Optional.empty());
        when(vendedorMapper.toEntity(requestValido)).thenReturn(vendedorEntity);
        when(vendedorRepository.save(vendedorEntity)).thenReturn(vendedorSalvo);
        when(vendedorMapper.toResponse(vendedorSalvo)).thenReturn(vendedorResponse);

        VendedorResponse result = vendedorService.create(requestValido);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(NOME, result.nome());

        verify(vendedorRepository).findByNomeIgnoreCase(nomeNormalizado);
        verify(vendedorMapper).toEntity(any());
        verify(vendedorRepository).save(any());
        verify(vendedorMapper).toResponse(any());
    }

    @Test
    void createQuandoNomeJaExisteLancaIllegalArgumentException() {
        when(vendedorRepository.findByNomeIgnoreCase(nomeNormalizado))
                .thenReturn(Optional.of(vendedorSalvo));

        assertThrows(IllegalArgumentException.class,
                () -> vendedorService.create(requestValido));

        verify(vendedorMapper, never()).toEntity(any());
        verify(vendedorRepository, never()).save(any());
    }

    @Test
    void createQuandoSaveFalhaLancaDataIntegrityViolationException() {
        when(vendedorRepository.findByNomeIgnoreCase(nomeNormalizado)).thenReturn(Optional.empty());
        when(vendedorMapper.toEntity(any())).thenReturn(vendedorEntity);
        when(vendedorRepository.save(any()))
                .thenThrow(new DataIntegrityViolationException("Erro"));

        assertThrows(DataIntegrityViolationException.class,
                () -> vendedorService.create(requestValido));

        verify(vendedorMapper, never()).toResponse(any());
    }

    @Test
    void createQuandoMapperToEntityFalhaLancaRuntimeException() {
        when(vendedorRepository.findByNomeIgnoreCase(nomeNormalizado)).thenReturn(Optional.empty());
        when(vendedorMapper.toEntity(any())).thenThrow(new RuntimeException("Erro mapper"));

        assertThrows(RuntimeException.class,
                () -> vendedorService.create(requestValido));

        verify(vendedorRepository, never()).save(any());
        verify(vendedorMapper, never()).toResponse(any());
    }

    @Test
    void createQuandoMapperToResponseFalhaLancaRuntimeException() {
        when(vendedorRepository.findByNomeIgnoreCase(nomeNormalizado)).thenReturn(Optional.empty());
        when(vendedorMapper.toEntity(any())).thenReturn(vendedorEntity);
        when(vendedorRepository.save(any())).thenReturn(vendedorSalvo);
        when(vendedorMapper.toResponse(any()))
                .thenThrow(new RuntimeException("Erro response"));

        assertThrows(RuntimeException.class,
                () -> vendedorService.create(requestValido));
    }

    @Test
    void deleteQuandoIdExisteDeletaComSucesso() {
        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedorSalvo));

        assertDoesNotThrow(() -> vendedorService.delete(1L));

        verify(vendedorRepository).findById(1L);
        verify(vendedorRepository).delete(vendedorSalvo);
    }

    @Test
    void deleteQuandoIdNaoExisteLancaVendedorNotFoundException() {
        when(vendedorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(VendedorNotFoundException.class,
                () -> vendedorService.delete(1L));

        verify(vendedorRepository, never()).delete(any());
    }

    @Test
    void deleteQuandoDeleteFalhaPropagaExcecao() {
        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedorSalvo));
        doThrow(new DataIntegrityViolationException("Erro"))
                .when(vendedorRepository).delete(any());

        assertThrows(DataIntegrityViolationException.class,
                () -> vendedorService.delete(1L));
    }

    @Test
    void getByIdQuandoIdExisteRetornaVendedorResponse() {
        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedorSalvo));
        when(vendedorMapper.toResponse(any())).thenReturn(vendedorResponse);

        VendedorResponse result = vendedorService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(NOME, result.nome());
    }

    @Test
    void getByIdQuandoIdNaoExisteLancaVendedorNotFoundException() {
        when(vendedorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(VendedorNotFoundException.class,
                () -> vendedorService.getById(1L));

        verify(vendedorMapper, never()).toResponse(any());
    }

    @Test
    void getByIdQuandoFindByIdFalhaPropagaExcecao() {
        when(vendedorRepository.findById(any()))
                .thenThrow(new RuntimeException("Erro repo"));

        assertThrows(RuntimeException.class,
                () -> vendedorService.getById(1L));

        verify(vendedorMapper, never()).toResponse(any());
    }

    @Test
    void getAllQuandoExistemVendedoresRetornaListaDeResponses() {
        List<Vendedor> lista = List.of(vendedorSalvo);
        List<VendedorResponse> listaResponse = List.of(vendedorResponse);

        when(vendedorRepository.findAll()).thenReturn(lista);
        when(vendedorMapper.toResponseList(lista)).thenReturn(listaResponse);

        List<VendedorResponse> result = vendedorService.getAll();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());
    }

    @Test
    void getAllQuandoNaoHaVendedoresRetornaListaVazia() {
        when(vendedorRepository.findAll()).thenReturn(Collections.emptyList());
        when(vendedorMapper.toResponseList(any())).thenReturn(Collections.emptyList());

        List<VendedorResponse> result = vendedorService.getAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllQuandoRepositoryFalhaLancaExcecao() {
        when(vendedorRepository.findAll()).thenThrow(new RuntimeException("Erro repository"));

        assertThrows(RuntimeException.class,
                () -> vendedorService.getAll());

        verify(vendedorMapper, never()).toResponseList(any());
    }

    @Test
    void updateQuandoRequestValidoRetornaVendedorResponse() {
        Vendedor vendedorExistente = new Vendedor(1L, NOME);
        Vendedor atualizado = new Vendedor(null, NOME);
        String nomeNormalizado = StringUtils.normalizeString(NOME);

        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedorExistente));
        when(vendedorRepository.findByNomeIgnoreCase(nomeNormalizado)).thenReturn(Optional.empty());
        when(vendedorMapper.toEntity(requestValido)).thenReturn(atualizado);
        when(vendedorRepository.save(vendedorExistente)).thenReturn(vendedorSalvo);
        when(vendedorMapper.toResponse(vendedorSalvo)).thenReturn(vendedorResponse);

        VendedorResponse result = vendedorService.update(1L, requestValido);

        assertEquals(1L, result.id());
        assertEquals(NOME, result.nome());

        verify(vendedorRepository).findById(1L);
        verify(vendedorRepository).findByNomeIgnoreCase(nomeNormalizado);
        verify(vendedorMapper).toEntity(requestValido);
        verify(vendedorRepository).save(vendedorExistente);
        verify(vendedorMapper).toResponse(vendedorSalvo);
    }

    @Test
    void updateQuandoIdInexistenteLancaVendedorNotFoundException() {
        when(vendedorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                VendedorNotFoundException.class,
                () -> vendedorService.update(1L, requestValido)
        );

        verify(vendedorRepository).findById(1L);
        verify(vendedorRepository, never()).findByNomeIgnoreCase(any());
        verify(vendedorRepository, never()).save(any());
        verify(vendedorMapper, never()).toEntity(any());
        verify(vendedorMapper, never()).toResponse(any());
    }


    @Test
    void updateQuandoNomeJaExisteEmOutroVendedorLancaIllegalArgumentException() {
        Vendedor existente = new Vendedor(1L, NOME);
        Vendedor outro = new Vendedor(2L, NOME);

        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(vendedorRepository.findByNomeIgnoreCase(nomeNormalizado)).thenReturn(Optional.of(outro));

        assertThrows(IllegalArgumentException.class,
                () -> vendedorService.update(1L, requestValido));

        verify(vendedorRepository).findById(1L);
        verify(vendedorRepository).findByNomeIgnoreCase(nomeNormalizado);
        verify(vendedorMapper, never()).toEntity(any());
        verify(vendedorRepository, never()).save(any());
    }


    @Test
    void updateQuandoMapperToEntityFalhaLancaRuntimeException() {
        when(vendedorRepository.findByNomeIgnoreCase(nomeNormalizado)).thenReturn(Optional.empty());
        when(vendedorRepository.findById(any())).thenReturn(Optional.of(vendedorSalvo));
        when(vendedorMapper.toEntity(any())).thenThrow(new RuntimeException("Erro"));

        assertThrows(RuntimeException.class,
                () -> vendedorService.update(1L, requestValido));

        verify(vendedorRepository, never()).save(any());
    }

    @Test
    void updateQuandoUpdateFromFalhaLancaRuntimeException() {
        Vendedor vendedorMock = mock(Vendedor.class);
        Vendedor novo = new Vendedor(null, NOME);

        when(vendedorRepository.findByNomeIgnoreCase(nomeNormalizado)).thenReturn(Optional.empty());
        when(vendedorRepository.findById(any())).thenReturn(Optional.of(vendedorMock));
        when(vendedorMapper.toEntity(any())).thenReturn(novo);

        doThrow(new RuntimeException("Erro updateFrom"))
                .when(vendedorMock).updateFrom(novo);

        assertThrows(RuntimeException.class,
                () -> vendedorService.update(1L, requestValido));

        verify(vendedorRepository, never()).save(any());
    }

    @Test
    void updateQuandoNomeJaExisteMasEhMesmoVendedorNaoLancaExcecao() {
        Vendedor existente = new Vendedor(1L, NOME);

        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(vendedorRepository.findByNomeIgnoreCase(nomeNormalizado))
                .thenReturn(Optional.of(existente));
        when(vendedorMapper.toEntity(requestValido)).thenReturn(new Vendedor(null, NOME));
        when(vendedorRepository.save(existente)).thenReturn(vendedorSalvo);
        when(vendedorMapper.toResponse(vendedorSalvo)).thenReturn(vendedorResponse);

        VendedorResponse result = vendedorService.update(1L, requestValido);

        assertEquals(1L, result.id());
        assertEquals(NOME, result.nome());
    }


    @Test
    void updateQuandoSaveFalhaLancaDataIntegrityViolationException() {
        Vendedor vendedorExistente = new Vendedor(1L, NOME);
        Vendedor novo = new Vendedor(null, NOME);

        when(vendedorRepository.findByNomeIgnoreCase(nomeNormalizado)).thenReturn(Optional.empty());
        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedorExistente));
        when(vendedorMapper.toEntity(any())).thenReturn(novo);
        when(vendedorRepository.save(any()))
                .thenThrow(new DataIntegrityViolationException("Erro"));

        assertThrows(DataIntegrityViolationException.class,
                () -> vendedorService.update(1L, requestValido));
    }
}
