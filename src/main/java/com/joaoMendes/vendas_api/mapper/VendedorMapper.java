package com.joaoMendes.vendas_api.mapper;

import com.joaoMendes.vendas_api.domain.entities.Vendedor;
import com.joaoMendes.vendas_api.dto.request.VendedorRequest;
import com.joaoMendes.vendas_api.dto.response.VendedorResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VendedorMapper {

    public Vendedor toEntity(VendedorRequest request){
        Vendedor vendedor = new Vendedor();
        vendedor.setNome(request.getNome());
        return vendedor;
    }

    public VendedorResponse toResponse(Vendedor vendedor){
        return new VendedorResponse(vendedor.getId(), vendedor.getNome());
    }

    public List<VendedorResponse> toResponseList(List<Vendedor> vendedores) {
        return vendedores.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
