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
import org.springframework.dao.DataIntegrityViolationException;

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
    private Venda vendaSalva;
    private VendaResponse vendaResponse;

    private VendaRequest vendaRequest;

    private final Long ID_VENDA = 1L;
    private final Long ID_VENDEDOR = 10L;
    private final BigDecimal VALOR = new BigDecimal("500.00");

    private static final LocalDate DATA_INICIO_VALIDO = LocalDate.of(2025, 11, 10);
    private static final LocalDate DATA_FIM_VALIDO = LocalDate.of(2025, 11, 12);

    @BeforeEach
    void setup() {

        vendedor = new Vendedor(ID_VENDEDOR, "Nome");

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

    @Test
    void createQuandoRequestValidaRetornaVendaResponse() {
        when(vendedorRepository.findById(ID_VENDEDOR))
                .thenReturn(Optional.of(vendedor));
        when(mapper.toEntity(vendaRequest, vendedor))
                .thenReturn(vendaEntity);
        when(vendaRepository.save(vendaEntity))
                .thenReturn(vendaSalva);
        when(mapper.toResponse(vendaSalva))
                .thenReturn(vendaResponse);

        VendaResponse result = vendaService.create(vendaRequest);

        assertNotNull(result);
        assertEquals(ID_VENDA, result.id());
        assertEquals(vendaRequest.getValor(), result.valor());

        verify(vendedorRepository).findById(ID_VENDEDOR);
        verify(mapper).toEntity(vendaRequest, vendedor);
        verify(vendaRepository).save(vendaEntity);
        verify(mapper).toResponse(vendaSalva);
    }

    @Test
    void createQuandoVendedorNaoExisteLancaVendedorNotFoundException() {
        when(vendedorRepository.findById(ID_VENDEDOR))
                .thenReturn(Optional.empty());

        assertThrows(VendedorNotFoundException.class,
                () -> vendaService.create(vendaRequest));

        verify(mapper, never()).toEntity(any(), any());
        verify(vendaRepository, never()).save(any());
    }

    @Test
    void createQuandoSaveFalhaLancaDataIntegrityViolationException() {
        when(vendedorRepository.findById(ID_VENDEDOR))
                .thenReturn(Optional.of(vendedor));
        when(mapper.toEntity(any(), any()))
                .thenReturn(vendaEntity);
        when(vendaRepository.save(any()))
                .thenThrow(new DataIntegrityViolationException("Erro"));

        assertThrows(DataIntegrityViolationException.class,
                () -> vendaService.create(vendaRequest));

        verify(mapper, never()).toResponse(any());
    }

    @Test
    void createQuandoMapperToEntityFalhaLancaRuntimeException() {
        when(vendedorRepository.findById(ID_VENDEDOR)).thenReturn(Optional.of(vendedor));
        when(mapper.toEntity(any(), any()))
                .thenThrow(new RuntimeException("Erro mapper"));

        assertThrows(RuntimeException.class,
                () -> vendaService.create(vendaRequest));

        verify(vendaRepository, never()).save(any());
        verify(mapper, never()).toResponse(any());
    }

    @Test
    void createQuandoMapperToResponseFalhaLancaRuntimeException() {
        when(vendedorRepository.findById(ID_VENDEDOR))
                .thenReturn(Optional.of(vendedor));
        when(mapper.toEntity(any(), any()))
                .thenReturn(vendaEntity);
        when(vendaRepository.save(any()))
                .thenReturn(vendaSalva);
        when(mapper.toResponse(any()))
                .thenThrow(new RuntimeException("Erro response"));

        assertThrows(RuntimeException.class,
                () -> vendaService.create(vendaRequest));
    }

    @Test
    void deleteQuandoIdExisteDeletaComSucesso() {
        when(vendaRepository.findById(ID_VENDA)).thenReturn(Optional.of(vendaSalva));

        assertDoesNotThrow(() -> vendaService.delete(ID_VENDA));

        verify(vendaRepository).findById(ID_VENDA);
        verify(vendaRepository).delete(vendaSalva);
    }

    @Test
    void deleteQuandoIdInexistenteLancaVendaNotFoundException() {
        when(vendaRepository.findById(ID_VENDA)).thenReturn(Optional.empty());

        assertThrows(VendaNotFoundException.class,
                () -> vendaService.delete(ID_VENDA));

        verify(vendaRepository, never()).delete(any());
    }

    @Test
    void deleteQuandoDeleteFalhaLancaDataIntegrityViolationException() {
        when(vendaRepository.findById(ID_VENDA))
                .thenReturn(Optional.of(vendaSalva));
        doThrow(new DataIntegrityViolationException("Erro delete"))
                .when(vendaRepository).delete(vendaSalva);

        assertThrows(DataIntegrityViolationException.class,
                () -> vendaService.delete(ID_VENDA));
    }

    @Test
    void getByIdQuandoIdExisteRetornaResponse() {
        when(vendaRepository.findById(ID_VENDA)).thenReturn(Optional.of(vendaSalva));
        when(mapper.toResponse(vendaSalva)).thenReturn(vendaResponse);

        VendaResponse result = vendaService.getById(ID_VENDA);

        assertNotNull(result);
        assertEquals(vendaResponse.id(), result.id());
        assertEquals(vendaResponse.valor(), result.valor());

        verify(vendaRepository).findById(ID_VENDA);
        verify(mapper).toResponse(vendaSalva);
    }

    @Test
    void getByIdQuandoFindByIdFalhaPropagaExcecao() {
        when(vendaRepository.findById(any()))
                .thenThrow(new RuntimeException("Erro repo"));

        assertThrows(RuntimeException.class,
                () -> vendaService.getById(ID_VENDA));

        verify(mapper, never()).toResponse(any());
    }

    @Test
    void getByIdQuandoIdInexistenteLancaVendaNotFoundException() {
        when(vendaRepository.findById(ID_VENDA)).thenReturn(Optional.empty());

        assertThrows(VendaNotFoundException.class,
                () -> vendaService.getById(ID_VENDA));

        verify(mapper, never()).toResponse(any());
    }

    @Test
    void getVendasPorVendedorByIdQuandoIdExisteRetornaListaDeVendas() {
        List<Venda> listaVendas = List.of(vendaSalva);

        when(vendedorRepository.findById(ID_VENDEDOR)).thenReturn(Optional.of(vendedor));
        when(vendaRepository.findByVendedor(vendedor)).thenReturn(listaVendas);
        when(mapper.toResponse(vendaSalva)).thenReturn(vendaResponse);

        List<VendaResponse> result = vendaService.getVendasPorVendedorById(ID_VENDEDOR);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(vendaResponse.id(), result.get(0).id());

        verify(vendedorRepository).findById(ID_VENDEDOR);
        verify(vendaRepository).findByVendedor(vendedor);
        verify(mapper).toResponse(vendaSalva);
    }

    @Test
    void getVendasPorVendedorByIdQuandoVendedorNaoExisteLancaVendedorNotFoundException() {
        when(vendedorRepository.findById(ID_VENDEDOR)).thenReturn(Optional.empty());

        assertThrows(VendedorNotFoundException.class,
                () -> vendaService.getVendasPorVendedorById(ID_VENDEDOR));

        verify(vendaRepository, never()).findByVendedor(any());
        verify(mapper, never()).toResponse(any());
    }

    @Test
    void getVendasPorVendedorByIdQuandoRepositoryRetornaVazioRetornaListaVazia() {
        when(vendedorRepository.findById(ID_VENDEDOR)).thenReturn(Optional.of(vendedor));
        when(vendaRepository.findByVendedor(vendedor)).thenReturn(List.of());

        List<VendaResponse> result = vendaService.getVendasPorVendedorById(ID_VENDEDOR);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(vendedorRepository).findById(ID_VENDEDOR);
        verify(vendaRepository).findByVendedor(vendedor);
        verify(mapper, never()).toResponse(any());
    }

    @Test
    void getVendasPorVendedorByIdQuandoRepositoryFalhaLancaExcecao() {
        when(vendedorRepository.findById(ID_VENDEDOR))
                .thenReturn(Optional.of(vendedor));
        when(vendaRepository.findByVendedor(vendedor))
                .thenThrow(new RuntimeException("Erro repository"));

        assertThrows(RuntimeException.class,
                () -> vendaService.getVendasPorVendedorById(ID_VENDEDOR));

        verify(mapper, never()).toResponse(any());
    }

    @Test
    void getAllRetornaListaDeVendasResponse() {
        List<Venda> listaVendas = List.of(vendaSalva);
        List<VendaResponse> listaResponse = List.of(vendaResponse);

        when(vendaRepository.findAll()).thenReturn(listaVendas);
        when(mapper.toResponseList(listaVendas)).thenReturn(listaResponse);

        List<VendaResponse> result = vendaService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(vendaResponse.id(), result.get(0).id());

        verify(vendaRepository).findAll();
        verify(mapper).toResponseList(listaVendas);
    }

    @Test
    void getAllQuandoRepositoryRetornaVazioRetornaListaVazia() {
        when(vendaRepository.findAll()).thenReturn(List.of());
        when(mapper.toResponseList(anyList())).thenReturn(List.of());

        List<VendaResponse> result = vendaService.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(vendaRepository).findAll();
        verify(mapper).toResponseList(anyList());
    }

    @Test
    void getAllQuandoRepositoryFalhaLancaExcecao() {
        when(vendaRepository.findAll())
                .thenThrow(new RuntimeException("Erro repository"));

        assertThrows(RuntimeException.class,
                () -> vendaService.getAll());

        verify(mapper, never()).toResponseList(anyList());
    }


    @Test
    void updateQuandoDadosValidosRetornaVendaResponse() {
        Venda vendaExistente = vendaSalva;
        when(vendaRepository.findById(ID_VENDA)).thenReturn(Optional.of(vendaExistente));
        when(vendedorRepository.findById(ID_VENDEDOR)).thenReturn(Optional.of(vendedor));
        when(mapper.toEntity(vendaRequest, vendedor)).thenReturn(vendaEntity);
        when(vendaRepository.save(vendaExistente)).thenReturn(vendaSalva);
        when(mapper.toResponse(vendaSalva)).thenReturn(vendaResponse);

        VendaResponse result = vendaService.update(ID_VENDA, vendaRequest);

        assertEquals(ID_VENDA, result.id());
        assertEquals(VALOR, result.valor());

        verify(vendaRepository).findById(ID_VENDA);
        verify(vendedorRepository).findById(ID_VENDEDOR);
        verify(mapper).toEntity(vendaRequest, vendedor);
        verify(vendaRepository).save(vendaExistente);
        verify(mapper).toResponse(vendaSalva);
    }

    @Test
    void updateQuandoVendaNaoExisteLancaVendaNotFoundException() {
        when(vendaRepository.findById(ID_VENDA)).thenReturn(Optional.empty());

        assertThrows(VendaNotFoundException.class,
                () -> vendaService.update(ID_VENDA, vendaRequest));

        verify(vendedorRepository, never()).findById(any());
        verify(mapper, never()).toEntity(any(), any());
        verify(vendaRepository, never()).save(any());
    }

    @Test
    void updateQuandoVendedorNaoExisteLancaVendedorNotFoundException() {
        when(vendaRepository.findById(ID_VENDA)).thenReturn(Optional.of(vendaSalva));
        when(vendedorRepository.findById(ID_VENDEDOR)).thenReturn(Optional.empty());

        assertThrows(VendedorNotFoundException.class,
                () -> vendaService.update(ID_VENDA, vendaRequest));

        verify(mapper, never()).toEntity(any(), any());
        verify(vendaRepository, never()).save(any());
    }

    @Test
    void updateQuandoUpdateFromFalhaLancaRuntimeException() {
        Venda vendaMock = mock(Venda.class);

        when(vendaRepository.findById(ID_VENDA)).thenReturn(Optional.of(vendaMock));
        when(vendedorRepository.findById(ID_VENDEDOR)).thenReturn(Optional.of(vendedor));
        when(mapper.toEntity(any(), any())).thenReturn(vendaEntity);

        doThrow(new RuntimeException("Erro updateFrom"))
                .when(vendaMock).updateFrom(vendaEntity);

        assertThrows(RuntimeException.class,
                () -> vendaService.update(ID_VENDA, vendaRequest));

        verify(vendaRepository, never()).save(any());
    }

    @Test
    void updateQuandoMapperToEntityFalhaLancaRuntimeException() {
        when(vendaRepository.findById(ID_VENDA))
                .thenReturn(Optional.of(vendaSalva));
        when(vendedorRepository.findById(ID_VENDEDOR))
                .thenReturn(Optional.of(vendedor));
        when(mapper.toEntity(any(), any()))
                .thenThrow(new RuntimeException("Erro mapper"));

        assertThrows(RuntimeException.class,
                () -> vendaService.update(ID_VENDA, vendaRequest));

        verify(vendaRepository, never()).save(any());
        verify(mapper, never()).toResponse(any());
    }

    @Test
    void updateQuandoSaveFalhaLancaDataIntegrityViolationException() {
        when(vendaRepository.findById(ID_VENDA))
                .thenReturn(Optional.of(vendaSalva));
        when(vendedorRepository.findById(ID_VENDEDOR))
                .thenReturn(Optional.of(vendedor));
        when(mapper.toEntity(any(), any()))
                .thenReturn(vendaEntity);
        when(vendaRepository.save(any()))
                .thenThrow(new DataIntegrityViolationException("Erro"));

        assertThrows(DataIntegrityViolationException.class,
                () -> vendaService.update(ID_VENDA, vendaRequest));

        verify(mapper, never()).toResponse(any());
    }

    @Test
    void updateQuandoMapperToResponseFalhaLancaRuntimeException() {
        when(vendaRepository.findById(ID_VENDA))
                .thenReturn(Optional.of(vendaSalva));
        when(vendedorRepository.findById(ID_VENDEDOR))
                .thenReturn(Optional.of(vendedor));
        when(mapper.toEntity(any(), any()))
                .thenReturn(vendaEntity);
        when(vendaRepository.save(any()))
                .thenReturn(vendaSalva);
        when(mapper.toResponse(any()))
                .thenThrow(new RuntimeException("Erro response"));

        assertThrows(RuntimeException.class,
                () -> vendaService.update(ID_VENDA, vendaRequest));
    }


    @Test
    void calcularMediaDiariaComVendasCalculaTotalQuantidadeDiasEMediaCorretamente() {
        MediaPorPeriodoRequest request = new MediaPorPeriodoRequest(DATA_INICIO_VALIDO, DATA_FIM_VALIDO);

        Venda v1 = new Venda(null, LocalDate.of(2025,11,10), new BigDecimal("100.50"), vendedor);
        Venda v2 = new Venda(null, LocalDate.of(2025,11,12), new BigDecimal("200.25"), vendedor);

        when(vendedorRepository.findById(ID_VENDEDOR)).thenReturn(Optional.of(vendedor));
        when(vendaRepository.findByVendedorAndDataVendaBetween(vendedor, DATA_INICIO_VALIDO, DATA_FIM_VALIDO))
                .thenReturn(List.of(v1, v2));

        MediaPorPeriodoResponse resp = vendaService.calcularMediaDiaria(ID_VENDEDOR, request);

        assertEquals(0, resp.totalVendido().compareTo(new BigDecimal("300.75")));
        assertEquals(0, resp.mediaDiaria().compareTo(new BigDecimal("100.25")));
        assertEquals(3L, resp.dias());
        assertEquals(2L, resp.quantidadeVendas());

        verify(vendedorRepository).findById(ID_VENDEDOR);
        verify(vendaRepository).findByVendedorAndDataVendaBetween(vendedor, DATA_INICIO_VALIDO, DATA_FIM_VALIDO);
    }

    @Test
    void calcularMediaDiariaQuandoPeriodoInvalidoLancaPeriodoInvalidoException() {
        MediaPorPeriodoRequest request = new MediaPorPeriodoRequest(DATA_INICIO_VALIDO, DATA_INICIO_VALIDO.minusDays(1));

        when(vendedorRepository.findById(ID_VENDEDOR)).thenReturn(Optional.of(vendedor));

        assertThrows(PeriodoInvalidoException.class,
                () -> vendaService.calcularMediaDiaria(ID_VENDEDOR, request));

        verify(vendedorRepository).findById(ID_VENDEDOR);
        verifyNoMoreInteractions(vendaRepository);
    }

    @Test
    void calcularMediaDiariaQuandoVendedorNaoExisteLancaVendedorNotFoundException() {
        MediaPorPeriodoRequest request = new MediaPorPeriodoRequest(DATA_INICIO_VALIDO, DATA_FIM_VALIDO);

        when(vendedorRepository.findById(ID_VENDEDOR)).thenReturn(Optional.empty());

        assertThrows(VendedorNotFoundException.class,
                () -> vendaService.calcularMediaDiaria(ID_VENDEDOR, request));

        verify(vendedorRepository).findById(ID_VENDEDOR);
        verifyNoInteractions(vendaRepository);
    }

    @Test
    void calcularMediaDiariaQuandoRepositoryFalhaPropagaExcecao() {
        MediaPorPeriodoRequest request = new MediaPorPeriodoRequest(DATA_INICIO_VALIDO, DATA_FIM_VALIDO);

        when(vendedorRepository.findById(ID_VENDEDOR)).thenReturn(Optional.of(vendedor));
        when(vendaRepository.findByVendedorAndDataVendaBetween(vendedor, DATA_INICIO_VALIDO, DATA_FIM_VALIDO))
                .thenThrow(new RuntimeException("Erro DB"));

        assertThrows(RuntimeException.class,
                () -> vendaService.calcularMediaDiaria(ID_VENDEDOR, request));

        verify(vendedorRepository).findById(ID_VENDEDOR);
        verify(vendaRepository).findByVendedorAndDataVendaBetween(vendedor, DATA_INICIO_VALIDO, DATA_FIM_VALIDO);
    }

    @Test
    void calcularMediaDiariaQuandoNaoHaVendasRetornaZerosECorretoDiasQuantidade() {
        MediaPorPeriodoRequest request = new MediaPorPeriodoRequest(DATA_INICIO_VALIDO, DATA_FIM_VALIDO);

        when(vendedorRepository.findById(ID_VENDEDOR)).thenReturn(Optional.of(vendedor));
        when(vendaRepository.findByVendedorAndDataVendaBetween(vendedor, DATA_INICIO_VALIDO, DATA_FIM_VALIDO))
                .thenReturn(List.of());

        MediaPorPeriodoResponse resp = vendaService.calcularMediaDiaria(ID_VENDEDOR, request);

        assertEquals(0, resp.totalVendido().compareTo(BigDecimal.ZERO));
        assertEquals(0, resp.mediaDiaria().compareTo(BigDecimal.ZERO));
        assertEquals(3L, resp.dias());
        assertEquals(0L, resp.quantidadeVendas());

        verify(vendedorRepository).findById(ID_VENDEDOR);
        verify(vendaRepository).findByVendedorAndDataVendaBetween(vendedor, DATA_INICIO_VALIDO, DATA_FIM_VALIDO);
    }

    @Test
    void calcularMediaDiariaInicioIgualFimContaUmDia() {
        LocalDate dia = LocalDate.of(2025, 11, 20);
        MediaPorPeriodoRequest request = new MediaPorPeriodoRequest(dia, dia);

        Venda v = new Venda(null, dia, new BigDecimal("50.00"), vendedor);

        when(vendedorRepository.findById(ID_VENDEDOR)).thenReturn(Optional.of(vendedor));
        when(vendaRepository.findByVendedorAndDataVendaBetween(vendedor, dia, dia))
                .thenReturn(List.of(v));

        MediaPorPeriodoResponse resp = vendaService.calcularMediaDiaria(ID_VENDEDOR, request);

        assertEquals(0, resp.totalVendido().compareTo(new BigDecimal("50.00")));
        assertEquals(0, resp.mediaDiaria().compareTo(new BigDecimal("50.00")));
        assertEquals(1L, resp.dias());
        assertEquals(1L, resp.quantidadeVendas());

        verify(vendedorRepository).findById(ID_VENDEDOR);
        verify(vendaRepository).findByVendedorAndDataVendaBetween(vendedor, dia, dia);
    }

}
