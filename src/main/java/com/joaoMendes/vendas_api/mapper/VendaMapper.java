package com.joaoMendes.vendas_api.mapper;

import com.joaoMendes.vendas_api.domain.entities.Venda;
import com.joaoMendes.vendas_api.domain.entities.Vendedor;
import com.joaoMendes.vendas_api.dto.request.VendaRequest;
import com.joaoMendes.vendas_api.dto.response.VendaResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VendaMapper {

    public Venda toEntity(VendaRequest request, Vendedor vendedor){
        Venda venda = new Venda();
        venda.setDataVenda(request.getDataVenda());
        venda.setValor(request.getValor());
        venda.setVendedor(vendedor);

        return venda;
    }

    public VendaResponse toResponse(Venda venda){
        return new VendaResponse(
                venda.getId(),
                venda.getDataVenda(),
                venda.getValor(),
                venda.getVendedor().getId(),
                venda.getVendedor().getNome()
                );
    }

    public List<VendaResponse> toResponseList(List<Venda> vendas) {
        return vendas.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
