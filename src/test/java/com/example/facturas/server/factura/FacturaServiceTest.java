package com.example.facturas.server.factura;

import com.example.facturas.server.partida.Partida;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FacturaServiceTest {

    @Mock
    private FacturaRepository facturaRepository;

    @InjectMocks
    private FacturaService facturaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void IntentaGuardar_PeroFalla() {

        Mockito.when(facturaRepository.save(Mockito.any(Factura.class)))
                .thenThrow(new RuntimeException("No se pudo guardar"));

        Factura factura = new Factura();
        factura.setFolio("F1");

        List<Partida> partidas = new ArrayList<>();
        Partida p1 = new Partida();
        p1.setPrecio(18.0);
        p1.setCantidad(2);
        p1.setNombreArticulo("Mi articulo");
        partidas.add(p1);

        factura.setPartidas(partidas);

        assertThrows(RuntimeException.class, () -> facturaService.guardar(factura));
    }

    @Test
    public void IntentaGuardar_Exitoso() {

        Factura factura = new Factura();
        factura.setFolio("F1");

        List<Partida> partidas = new ArrayList<>();
        Partida p1 = new Partida();
        p1.setPrecio(18.0);
        p1.setCantidad(2);
        p1.setNombreArticulo("Mi articulo");
        partidas.add(p1);

        factura.setPartidas(partidas);

        assertDoesNotThrow(() -> facturaService.guardar(factura));
    }


    @Test
    public void IntentaGuardar_CalculosCorrectos() {

        Factura factura = new Factura();
        factura.setFolio("F1");

        List<Partida> partidas = new ArrayList<>();
        Partida p1 = new Partida();
        p1.setPrecio(18.0);
        p1.setCantidad(2);
        p1.setNombreArticulo("Mi articulo");
        partidas.add(p1);

        factura.setPartidas(partidas);

        Mockito.when(facturaRepository.save(Mockito.any(Factura.class)))
                .thenAnswer((iom) -> {

                    Factura facturaDB = (Factura) iom.getArguments()[0];
                    facturaDB.setId(20L);

                    return facturaDB;
                });

        Factura facturaGuardada = facturaService.guardar(factura);

        assert facturaGuardada != null;
        assert factura.getId().equals(20L);
        assert facturaGuardada.getTotal().equals(36.0);
    }
}