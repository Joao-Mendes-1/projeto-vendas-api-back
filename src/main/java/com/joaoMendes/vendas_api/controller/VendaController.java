package com.joaoMendes.vendas_api.controller;

import com.joaoMendes.vendas_api.domain.service.VendaService;
import com.joaoMendes.vendas_api.dto.request.MediaPorPeriodoRequest;
import com.joaoMendes.vendas_api.dto.request.VendaRequest;
import com.joaoMendes.vendas_api.dto.response.MediaPorPeriodoResponse;
import com.joaoMendes.vendas_api.dto.response.VendaResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @PostMapping
    public ResponseEntity<VendaResponse> create(@Valid @RequestBody VendaRequest request) {
        VendaResponse response = vendaService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<VendaResponse>> getAll() {
        List<VendaResponse> response = vendaService.getAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vendedor/{id}")
    public ResponseEntity<List<VendaResponse>> getVendasPorVendedorById(@PathVariable Long id) {
        List<VendaResponse> response = vendaService.getVendasPorVendedorById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaResponse> getById(@PathVariable Long id) {
        VendaResponse response = vendaService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendaResponse> update(@PathVariable Long id, @Valid @RequestBody VendaRequest request) {
        VendaResponse response = vendaService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vendaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{idVendedor}/estatistica")
    public ResponseEntity<MediaPorPeriodoResponse> calcularMediaPorPeriodo(
            @PathVariable Long idVendedor,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dataInicio,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dataFim) {

        return ResponseEntity.ok(vendaService.calcularMediaPorPeriodo(idVendedor, new MediaPorPeriodoRequest(dataInicio,dataFim)));
    }
}
