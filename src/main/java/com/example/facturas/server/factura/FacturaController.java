package com.example.facturas.server.factura;

import com.example.facturas.server.partida.Partida;
import com.example.facturas.server.partida.PartidaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired
    private FacturaRepository repo;

    @Autowired
    private FacturaService service;

    @GetMapping
    public List<FacturaDTO> getAll() {

        List<Factura> facturas = repo.findAll();

        return facturas.stream()
                .map(factura -> toDTO(factura))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public FacturaDTO getById(@PathVariable Long id) {

        Factura entidad = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("No encontre la factura con el id " + id));

        return toDTO(entidad);
    }

    @PostMapping
    public FacturaDTO save(@RequestBody FacturaDTO facturaDTO) {
        Factura factura = toEntity(facturaDTO);
        Factura guardada = service.guardar(factura);
        return toDTO(guardada);
    }

    private FacturaDTO toDTO(Factura factura) {
        FacturaDTO dto = new FacturaDTO();
        dto.id = factura.getId();
        dto.folio = factura.getFolio();
        dto.fecha_expedicion = factura.getFechaExpedicion();
        dto.total = factura.getTotal();

        if (factura.getPartidas() != null) {

            dto.partidas = new ArrayList<>();

            for (Partida partida : factura.getPartidas()) {

                PartidaDTO pDTO = new PartidaDTO();
                pDTO.id = partida.getId();
                pDTO.cantidad = partida.getCantidad();
                pDTO.total = partida.getTotal();
                pDTO.nombre_articulo = partida.getNombreArticulo();
                pDTO.precio = partida.getPrecio();
                pDTO.factura_id = factura.getId();

                dto.partidas.add(pDTO);
            }
        }

        return dto;
    }

    private Factura toEntity(FacturaDTO dto) {
        Factura factura = new Factura();
        factura.setId(dto.id);
        factura.setTotal(dto.total);
        factura.setFechaExpedicion(dto.fecha_expedicion);
        factura.setFolio(dto.folio);

        if (dto.partidas != null) {

            factura.setPartidas(new ArrayList<>());

            for (PartidaDTO pDTO : dto.partidas) {

                Partida partida = new Partida();
                partida.setId(pDTO.id);
                partida.setTotal(pDTO.total);
                partida.setCantidad(pDTO.cantidad);
                partida.setPrecio(pDTO.precio);
                partida.setNombreArticulo(pDTO.nombre_articulo);

                factura.getPartidas().add(partida);
            }
        }

        return factura;
    }
}
