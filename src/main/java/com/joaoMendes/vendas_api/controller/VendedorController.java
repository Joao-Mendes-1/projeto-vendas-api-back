package com.joaoMendes.vendas_api.controller;

import com.joaoMendes.vendas_api.domain.service.VendedorService;
import com.joaoMendes.vendas_api.dto.request.VendedorRequest;
import com.joaoMendes.vendas_api.dto.response.VendedorResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendedores")
public class VendedorController {

    @Autowired
    private VendedorService vendedorService;

    @PostMapping
    public ResponseEntity<VendedorResponse> create(@Valid @RequestBody VendedorRequest request) {
        VendedorResponse response = vendedorService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vendedorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendedorResponse> getById(@PathVariable Long id) {
        VendedorResponse response = vendedorService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<VendedorResponse>> getAll() {
        List<VendedorResponse> response = vendedorService.getAll();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendedorResponse> update(@PathVariable Long id, @Valid @RequestBody VendedorRequest request) {
        VendedorResponse response = vendedorService.update(id, request);
        return ResponseEntity.ok(response);
    }
}