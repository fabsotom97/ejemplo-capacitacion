package com.example.facturas.server.factura;

import com.example.facturas.server.partida.PartidaDTO;

import java.util.Date;
import java.util.List;

public class FacturaDTO {

    public Long id;
    public String folio;
    public Double total;
    public Date fecha_expedicion;
    public List<PartidaDTO> partidas;
}
