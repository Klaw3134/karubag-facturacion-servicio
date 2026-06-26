package cl.karubag.facturacion.controller;

import cl.karubag.facturacion.dto.FacturaDTO;
import cl.karubag.facturacion.model.EstadoFactura;
import cl.karubag.facturacion.service.FacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "Facturas", description = "Gestión de facturación Karübag")
@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @Operation(summary = "Listar todas las facturas", description = "Retorna la lista completa de facturas")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<FacturaDTO>> listarTodos() {
        return ResponseEntity.ok(facturaService.listarTodos());
    }

    @Operation(summary = "Listar por cliente", description = "Retorna facturas de un cliente específico")
    @ApiResponse(responseCode = "200", description = "Lista de facturas del cliente")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<FacturaDTO>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(facturaService.listarPorCliente(clienteId));
    }

    @Operation(summary = "Listar por estado", description = "Retorna facturas filtradas por estado")
    @ApiResponse(responseCode = "200", description = "Lista de facturas por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<FacturaDTO>> listarPorEstado(@PathVariable EstadoFactura estado) {
        return ResponseEntity.ok(facturaService.listarPorEstado(estado));
    }

    @Operation(summary = "Listar por periodo", description = "Retorna facturas de un periodo específico")
    @ApiResponse(responseCode = "200", description = "Lista de facturas por periodo")
    @GetMapping("/periodo")
    public ResponseEntity<List<FacturaDTO>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(facturaService.listarPorPeriodo(inicio, fin));
    }

    @Operation(summary = "Total pagado por cliente", description = "Retorna el total pagado por un cliente")
    @ApiResponse(responseCode = "200", description = "Total pagado calculado")
    @GetMapping("/cliente/{clienteId}/total-pagado")
    public ResponseEntity<Double> obtenerTotalPagado(@PathVariable Long clienteId) {
        return ResponseEntity.ok(facturaService.obtenerTotalPagadoPorCliente(clienteId));
    }

    @Operation(summary = "Obtener factura por ID", description = "Busca una factura por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Factura encontrada"),
        @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FacturaDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.obtenerPorId(id));
    }

    @Operation(summary = "Crear factura", description = "Genera una factura verificando retiro via WebClient")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Factura creada exitosamente",
            content = @Content(schema = @Schema(implementation = FacturaDTO.class),
            examples = @ExampleObject(value = "{\"clienteId\": 1, \"retiroId\": 1, \"total\": 525.0, \"estado\": \"PENDIENTE\"}"))),
        @ApiResponse(responseCode = "404", description = "Retiro no encontrado")
    })
    @PostMapping
    public ResponseEntity<FacturaDTO> crear(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de la factura a crear",
            required = true,
            content = @Content(examples = @ExampleObject(value = "{\"clienteId\": 1, \"retiroId\": 1, \"total\": 525.0, \"estado\": \"PENDIENTE\"}")))
        @Valid @RequestBody FacturaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facturaService.crear(dto));
    }

    @Operation(summary = "Actualizar factura", description = "Actualiza los datos de una factura")
    @ApiResponse(responseCode = "200", description = "Factura actualizada exitosamente")
    @PutMapping("/{id}")
    public ResponseEntity<FacturaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody FacturaDTO dto) {
        return ResponseEntity.ok(facturaService.actualizar(id, dto));
    }

    @Operation(summary = "Pagar factura", description = "Cambia el estado de la factura a PAGADA")
    @ApiResponse(responseCode = "200", description = "Factura pagada exitosamente")
    @PutMapping("/{id}/pagar")
    public ResponseEntity<FacturaDTO> pagar(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.pagar(id));
    }

    @Operation(summary = "Anular factura", description = "Cambia el estado de la factura a ANULADA")
    @ApiResponse(responseCode = "200", description = "Factura anulada exitosamente")
    @PutMapping("/{id}/anular")
    public ResponseEntity<FacturaDTO> anular(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.anular(id));
    }

    @Operation(summary = "Eliminar factura", description = "Elimina una factura por su ID")
    @ApiResponse(responseCode = "204", description = "Factura eliminada exitosamente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        facturaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
