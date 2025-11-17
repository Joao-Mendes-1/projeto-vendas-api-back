package com.joaoMendes.vendas_api.domain.service;

import com.joaoMendes.vendas_api.domain.repository.VendedorRepository;
import com.joaoMendes.vendas_api.mapper.VendedorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VendedorService {

    @Autowired
    private VendedorRepository repository;

    @Autowired
    private VendedorMapper mapper;
}
