package com.joaoMendes.vendas_api.domain.service;

import com.joaoMendes.vendas_api.domain.entities.Venda;
import com.joaoMendes.vendas_api.domain.entities.Vendedor;
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

    public VendaResponse create(VendaRequest request){
        Vendedor vendedor = vendedorRepository.findById(request.getIdVendedor())
                .orElseThrow(() -> new VendedorNotFoundException(request.getIdVendedor()));

        Venda venda = mapper.toEntity(request, vendedor);
        return mapper.toResponse(vendaRepository.save(venda));
    }

    public void delete(Long id){
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new VendaNotFoundException(id));

        vendaRepository.delete(venda);
    }

    public VendaResponse getById(Long id){
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new VendaNotFoundException(id));
        return mapper.toResponse(venda);
    }

    public List<VendaResponse> getAll(){
        return mapper.toResponseList(vendaRepository.findAll());
    }

    public VendaResponse update(Long id, VendaRequest request){
        vendaRepository.findById(id)
                .orElseThrow(() -> new VendaNotFoundException(id));
        Venda vendaExistente;

        Vendedor vendedor = vendedorRepository.findById(id)
                .orElseThrow(() -> new VendedorNotFoundException(request.getIdVendedor()));

        vendaExistente = mapper.toEntity(request, vendedor);

        return mapper.toResponse(vendaRepository.save(vendaExistente));
    }

    public MediaPorPeriodoResponse calcularEstatistica(Long idVendedor, MediaPorPeriodoRequest periodoRequest){
        Vendedor vendedor = vendedorRepository.findById(idVendedor)
                .orElseThrow(() -> new VendedorNotFoundException(idVendedor));

        LocalDate inicio = periodoRequest.getDataInicio();
        LocalDate fim = periodoRequest.getDataFim();

        List<Venda> vendas = vendaRepository.findByVendedorAndDataVendaBetween(
                vendedor,
                inicio.atStartOfDay(),
                fim.atTime(23, 59, 59)
        );

        BigDecimal totalVendido = vendas.stream()
                .map(Venda::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long dias = ChronoUnit.DAYS.between(inicio, fim) + 1;
        BigDecimal mediaDiaria = dias > 0 ? totalVendido.divide(BigDecimal.valueOf(dias), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        return new MediaPorPeriodoResponse(
                vendedor.getId(),
                vendedor.getNome(),
                totalVendido,
                mediaDiaria,
                inicio,
                fim
        );
    }

}
