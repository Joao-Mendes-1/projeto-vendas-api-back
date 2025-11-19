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

import static com.joaoMendes.vendas_api.utils.StringUtils.*;

@Service
public class VendedorService {

    @Autowired
    private VendedorRepository vendedorRepository;

    @Autowired
    private VendedorMapper vendedorMapper;

    private void validateNomeDuplicado(String nome, Long idAtual) {
        String nomeNormalizado = normalizeString(nome);
        Optional<Vendedor> existente = vendedorRepository.findByNomeIgnoreCase(nomeNormalizado);

        if (existente.isPresent() && !existente.get().getId().equals(idAtual)) {
            throw new IllegalArgumentException("Já existe um vendedor com esse nome");
        }
    }

    private Vendedor findVendedorOrThrow(Long id) {
        return vendedorRepository.findById(id)
                .orElseThrow(() -> new VendedorNotFoundException(id));
    }

    public VendedorResponse create(VendedorRequest request){
        request.setNome(cleanStringForSave(request.getNome()));
        validateNomeDuplicado(request.getNome(), null);

        Vendedor vendedor = vendedorMapper.toEntity(request);

        return vendedorMapper.toResponse(vendedorRepository.save(vendedor));
    }

    public void delete(Long id){
        Vendedor vendedor = findVendedorOrThrow(id);

        vendedorRepository.delete(vendedor);
    }

    public VendedorResponse getById(Long id){
        Vendedor vendedor = findVendedorOrThrow(id);

        return vendedorMapper.toResponse(vendedor);
    }

    public List<VendedorResponse> getAll(){
        return vendedorMapper.toResponseList(vendedorRepository.findAll());
    }

    public VendedorResponse update(Long id, VendedorRequest request){
        request.setNome(cleanStringForSave(request.getNome()));
        validateNomeDuplicado(request.getNome(), id);
        Vendedor vendedorExistente = findVendedorOrThrow(id);

        vendedorExistente.updateFrom(vendedorMapper.toEntity(request)) ;

        return vendedorMapper.toResponse(vendedorRepository.save(vendedorExistente));
    }
}
