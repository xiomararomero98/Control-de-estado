package com.example.Control_de_Estado.Service;

import com.example.Control_de_Estado.Model.Estado;
import com.example.Control_de_Estado.Repository.EstadoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
public class EstadoServiceTest {

    @InjectMocks
    private EstadoService estadoService;

    @Mock
    private EstadoRepository estadoRepository;

    private Estado estado;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        estado = new Estado(1L, "Disponible");
    }

    // ----------- Positivos -----------

    @Test
    void testObtenerEstados() {
        when(estadoRepository.findAll()).thenReturn(List.of(estado));
        List<Estado> estados = estadoService.ObtenerEstados();
        assertThat(estados).isNotEmpty();
        assertThat(estados.get(0).getNombre()).isEqualTo("Disponible");
    }

    @Test
    void testObtenerEstadoPorId() {
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado));
        Optional<Estado> encontrado = estadoService.obtenerEstadoPorId(1L);
        assertThat(encontrado).isPresent().contains(estado);
    }

    @Test
    void testCrearEstado() {
        when(estadoRepository.save(estado)).thenReturn(estado);
        Estado creado = estadoService.crearEstado(estado);
        assertThat(creado.getNombre()).isEqualTo("Disponible");
    }

    @Test
    void testActualizarEstado() {
        Estado actualizado = new Estado(1L, "Entregado");
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado));
        when(estadoRepository.save(any())).thenReturn(actualizado);

        Estado result = estadoService.actualizarEstado(1L, actualizado);
        assertThat(result.getNombre()).isEqualTo("Entregado");
    }

    @Test
    void testEliminarEstado() {
        doNothing().when(estadoRepository).deleteById(1L);
        estadoService.eliminarEstado(1L);
        verify(estadoRepository, times(1)).deleteById(1L);
    }

    // ----------- Negativos -----------

    @Test
    void testActualizarEstado_NoEncontrado_lanzaExcepcion() {
        when(estadoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = catchThrowableOfType(() -> {
            estadoService.actualizarEstado(99L, new Estado(99L, "No existe"));
        }, RuntimeException.class);

        assertThat(ex).hasMessageContaining("Estado no encontrado con ID");
    }

    @Test
    void testObtenerEstadoPorId_NoExistente() {
        when(estadoRepository.findById(100L)).thenReturn(Optional.empty());

        Optional<Estado> resultado = estadoService.obtenerEstadoPorId(100L);
        assertThat(resultado).isEmpty();
    }

    @Test
void testEliminarEstado_NoExistente_lanzaExcepcion() {
    doThrow(new RuntimeException("Estado no encontrado")).when(estadoRepository).deleteById(999L);

    RuntimeException ex = catchThrowableOfType(() -> {
        estadoService.eliminarEstado(999L);
    }, RuntimeException.class);

    assertThat(ex).hasMessageContaining("Estado no encontrado");
}
}