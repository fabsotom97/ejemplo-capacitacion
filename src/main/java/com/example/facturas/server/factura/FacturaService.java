package com.example.facturas.server.factura;

import com.example.facturas.server.partida.Partida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository repo;

    public Factura guardar(Factura factura) {

        if (factura.getFolio() == null || factura.getFolio().isEmpty()) {
            throw new RuntimeException("No se indico el folio de la factura");
        }

        factura.setFechaExpedicion(new Date());

        Double totalFactura = 0.0;
        for (Partida partida : factura.getPartidas()) {

            partida.setFactura(factura);

            Integer cantidad = partida.getCantidad();

            if (cantidad < 1) {
                throw new RuntimeException("Las cantidades deben ser mayores o iguales a uno");
            }

            Double precio = partida.getPrecio();

            if (precio < 0.1) {
                throw new RuntimeException("El precio del articulo no puede ser menor a 0.1");
            }

            Double totalPartida = (cantidad * precio);
            partida.setTotal(totalPartida);

            totalFactura += partida.getTotal();
        }
        factura.setTotal(totalFactura);

        if (factura.getTotal() < 2.0) {
            throw new RuntimeException("El total fue menor a 2.0");
        }

        return repo.save(factura);
    }

}
