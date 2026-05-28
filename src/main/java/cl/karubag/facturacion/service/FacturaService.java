package cl.karubag.facturacion.service;

import cl.karubag.facturacion.dto.FacturaDTO;
import cl.karubag.facturacion.model.EstadoFactura;
import cl.karubag.facturacion.model.Factura;
import cl.karubag.facturacion.repository.FacturaRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacturaService {

    private final FacturaRepository facturaRepository;

    public FacturaService(FacturaRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    public List<FacturaDTO> listarTodos() {
        return facturaRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<FacturaDTO> listarPorCliente(Long clienteId) {
        return facturaRepository.findByClienteId(clienteId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<FacturaDTO> listarPorEstado(EstadoFactura estado) {
        return facturaRepository.findByEstado(estado)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<FacturaDTO> listarPorPeriodo(LocalDate inicio, LocalDate fin) {
        return facturaRepository.findByPeriodoInicioBetween(inicio, fin)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Double obtenerTotalPagadoPorCliente(Long clienteId) {
        Double total = facturaRepository.sumTotalPagadoByClienteId(clienteId);
        return total != null ? total : 0.0;
    }

    public FacturaDTO obtenerPorId(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada con id: " + id));
        return toDTO(factura);
    }

    public FacturaDTO crear(FacturaDTO dto) {
        Factura factura = toEntity(dto);
        if (factura.getFechaEmision() == null) {
            factura.setFechaEmision(LocalDate.now());
        }
        return toDTO(facturaRepository.save(factura));
    }

    public FacturaDTO actualizar(Long id, FacturaDTO dto) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada con id: " + id));
        factura.setClienteId(dto.getClienteId());
        factura.setRetiroId(dto.getRetiroId());
        factura.setFechaEmision(dto.getFechaEmision());
        factura.setPeriodoInicio(dto.getPeriodoInicio());
        factura.setPeriodoFin(dto.getPeriodoFin());
        factura.setTotal(dto.getTotal());
        factura.setEstado(dto.getEstado());
        factura.setObservacion(dto.getObservacion());
        return toDTO(facturaRepository.save(factura));
    }

    public FacturaDTO pagar(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada con id: " + id));
        factura.setEstado(EstadoFactura.PAGADA);
        return toDTO(facturaRepository.save(factura));
    }

    public FacturaDTO anular(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada con id: " + id));
        factura.setEstado(EstadoFactura.ANULADA);
        return toDTO(facturaRepository.save(factura));
    }

    public void eliminar(Long id) {
        facturaRepository.deleteById(id);
    }

    private FacturaDTO toDTO(Factura f) {
        FacturaDTO dto = new FacturaDTO();
        dto.setId(f.getId());
        dto.setClienteId(f.getClienteId());
        dto.setRetiroId(f.getRetiroId());
        dto.setFechaEmision(f.getFechaEmision());
        dto.setPeriodoInicio(f.getPeriodoInicio());
        dto.setPeriodoFin(f.getPeriodoFin());
        dto.setTotal(f.getTotal());
        dto.setEstado(f.getEstado());
        dto.setObservacion(f.getObservacion());
        return dto;
    }

    private Factura toEntity(FacturaDTO dto) {
        Factura f = new Factura();
        f.setClienteId(dto.getClienteId());
        f.setRetiroId(dto.getRetiroId());
        f.setFechaEmision(dto.getFechaEmision());
        f.setPeriodoInicio(dto.getPeriodoInicio());
        f.setPeriodoFin(dto.getPeriodoFin());
        f.setTotal(dto.getTotal());
        f.setEstado(dto.getEstado() != null ? dto.getEstado() : EstadoFactura.PENDIENTE);
        f.setObservacion(dto.getObservacion());
        return f;
    }
}