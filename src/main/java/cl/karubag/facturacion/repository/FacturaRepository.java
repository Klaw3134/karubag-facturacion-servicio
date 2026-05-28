package cl.karubag.facturacion.repository;

import cl.karubag.facturacion.model.Factura;
import cl.karubag.facturacion.model.EstadoFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    List<Factura> findByClienteId(Long clienteId);

    List<Factura> findByEstado(EstadoFactura estado);

    List<Factura> findByClienteIdAndEstado(Long clienteId, EstadoFactura estado);

    List<Factura> findByPeriodoInicioBetween(LocalDate inicio, LocalDate fin);

    @Query("SELECT SUM(f.total) FROM Factura f WHERE f.clienteId = :clienteId AND f.estado = 'PAGADA'")
    Double sumTotalPagadoByClienteId(Long clienteId);
}
