package com.joaoMendes.vendas_api.controller;

import com.joaoMendes.vendas_api.domain.service.VendaService;
import com.joaoMendes.vendas_api.dto.request.MediaPorPeriodoRequest;
import com.joaoMendes.vendas_api.dto.request.VendaRequest;
import com.joaoMendes.vendas_api.dto.response.MediaPorPeriodoResponse;
import com.joaoMendes.vendas_api.dto.response.VendaResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    @Autowired
    private VendaService service;

    @PostMapping
    public ResponseEntity<VendaResponse> create(@Valid @RequestBody VendaRequest request) {
        VendaResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<VendaResponse>> getAll() {
        List<VendaResponse> response = service.getAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaResponse> getById(@PathVariable Long id) {
        VendaResponse response = service.getById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendaResponse> update(@PathVariable Long id, @Valid @RequestBody VendaRequest request) {
        VendaResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{idVendedor}/estatistica")
    public ResponseEntity<MediaPorPeriodoResponse> calcularEstatistica(
            @PathVariable Long idVendedor,
            @Valid @RequestBody MediaPorPeriodoRequest filtro) {

        MediaPorPeriodoResponse response = service.calcularEstatistica(idVendedor, filtro);
        return ResponseEntity.ok(response);
    }
}
