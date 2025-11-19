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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class VendaService {
    @Autowired
    private VendaRepository vendaRepository;
    @Autowired
    private VendedorRepository vendedorRepository;
    @Autowired
    private VendaMapper mapper;

    private Venda findVendaOrThrow(Long id) {
        return vendaRepository.findById(id)
                .orElseThrow(() -> new VendaNotFoundException(id));
    }

    private Vendedor findVendedorOrThrow(Long id) {
        return vendedorRepository.findById(id)
                .orElseThrow(() -> new VendedorNotFoundException(id));
    }

    public VendaResponse create(VendaRequest request){
        Vendedor vendedor = findVendedorOrThrow(request.getIdVendedor());

        Venda venda = mapper.toEntity(request, vendedor);
        return mapper.toResponse(vendaRepository.save(venda));
    }

    public void delete(Long id){
        Venda venda = findVendaOrThrow(id);

        vendaRepository.delete(venda);
    }

    public VendaResponse getById(Long id){
        Venda venda = findVendaOrThrow(id);
        return mapper.toResponse(venda);
    }

    public List<VendaResponse> getVendasPorVendedorById(Long id){
        Vendedor vendedor = findVendedorOrThrow(id);
        List<Venda> vendas = vendaRepository.findByVendedor(vendedor);
        return vendas.stream()
                .map(mapper::toResponse)
                .toList();
    }

    public List<VendaResponse> getAll(){
        return mapper.toResponseList(vendaRepository.findAll());
    }

    public VendaResponse update(Long id, VendaRequest request){
        Venda vendaExistente = findVendaOrThrow(id);

        Vendedor vendedor = findVendedorOrThrow(request.getIdVendedor());

        vendaExistente.updateFrom(mapper.toEntity(request, vendedor)) ;

        return mapper.toResponse(vendaRepository.save(vendaExistente));
    }

    public MediaPorPeriodoResponse calcularMediaDiaria(Long idVendedor, MediaPorPeriodoRequest periodoRequest){

        Vendedor vendedor = findVendedorOrThrow(idVendedor);

        LocalDate inicio = periodoRequest.getDataInicio();
        LocalDate fim = periodoRequest.getDataFim();
        if (fim.isBefore(inicio)) {
            throw new PeriodoInvalidoException(inicio,fim);
        }

        List<Venda> vendas = vendaRepository.findByVendedorAndDataVendaBetween(
                vendedor,
                inicio,
                fim
        );

        BigDecimal totalVendido = vendas.stream()
                .map(Venda::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long quantidadeVendas = vendas.stream().count();


        long dias = ChronoUnit.DAYS.between(inicio, fim) + 1;
        BigDecimal mediaDiaria = dias > 0 ? totalVendido.divide(BigDecimal.valueOf(dias), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        return new MediaPorPeriodoResponse(
                vendedor.getId(),
                vendedor.getNome(),
                totalVendido,
                mediaDiaria,
                inicio,
                fim,
                dias,
                quantidadeVendas

        );
    }

}
