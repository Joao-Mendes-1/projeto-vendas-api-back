package com.joaoMendes.vendas_api.domain.service;

import com.joaoMendes.vendas_api.domain.entities.Vendedor;
import com.joaoMendes.vendas_api.domain.exception.VendedorNotFoundException;
import com.joaoMendes.vendas_api.domain.repository.VendedorRepository;
import com.joaoMendes.vendas_api.dto.request.VendedorRequest;
import com.joaoMendes.vendas_api.dto.response.VendedorResponse;
import com.joaoMendes.vendas_api.mapper.VendedorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VendedorService {

    @Autowired
    private VendedorRepository repository;

    @Autowired
    private VendedorMapper mapper;

    public VendedorResponse create(VendedorRequest request){
        Optional<Vendedor> existente = repository.findByNome(request.getNome());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Já existe um vendedor com esse nome");
        }

        Vendedor vendedor = mapper.toEntity(request);

        return mapper.toResponse(repository.save(vendedor));
    }

    public void delete(Long id){
        Vendedor vendedor = repository.findById(id)
                .orElseThrow(() -> new VendedorNotFoundException(id));

        repository.delete(vendedor);
    }

    public VendedorResponse getById(Long id){
        Vendedor vendedor = repository.findById(id)
                .orElseThrow(() -> new VendedorNotFoundException(id));

        return mapper.toResponse(vendedor);
    }

    public List<VendedorResponse> getAll(){
        return mapper.toResponseList(repository.findAll());
    }

    public VendedorResponse update(Long id, VendedorRequest request){
        Vendedor vendedorExistente = repository.findById(id)
                .orElseThrow(() -> new VendedorNotFoundException(id));
        Vendedor atualizar = mapper.toEntity(request);
        vendedorExistente.updateFrom(atualizar);

        return mapper.toResponse(repository.save(vendedorExistente));
    }
}
