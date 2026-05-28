package cl.karubag.facturacion.controller;

import cl.karubag.facturacion.dto.FacturaDTO;
import cl.karubag.facturacion.model.EstadoFactura;
import cl.karubag.facturacion.service.FacturaService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @GetMapping
    public ResponseEntity<List<FacturaDTO>> listarTodos() {
        return ResponseEntity.ok(facturaService.listarTodos());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<FacturaDTO>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(facturaService.listarPorCliente(clienteId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<FacturaDTO>> listarPorEstado(@PathVariable EstadoFactura estado) {
        return ResponseEntity.ok(facturaService.listarPorEstado(estado));
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<FacturaDTO>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(facturaService.listarPorPeriodo(inicio, fin));
    }

    @GetMapping("/cliente/{clienteId}/total-pagado")
    public ResponseEntity<Double> obtenerTotalPagado(@PathVariable Long clienteId) {
        return ResponseEntity.ok(facturaService.obtenerTotalPagadoPorCliente(clienteId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<FacturaDTO> crear(@Valid @RequestBody FacturaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facturaService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacturaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody FacturaDTO dto) {
        return ResponseEntity.ok(facturaService.actualizar(id, dto));
    }

    @PutMapping("/{id}/pagar")
    public ResponseEntity<FacturaDTO> pagar(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.pagar(id));
    }

    @PutMapping("/{id}/anular")
    public ResponseEntity<FacturaDTO> anular(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.anular(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        facturaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
