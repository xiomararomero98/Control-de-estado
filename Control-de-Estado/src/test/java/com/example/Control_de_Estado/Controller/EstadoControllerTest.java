package com.example.Control_de_Estado.Controller;

import com.example.Control_de_Estado.Model.Estado;
import com.example.Control_de_Estado.Repository.EstadoRepository;
import com.example.Control_de_Estado.Service.EstadoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EstadoController.class)
public class EstadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EstadoRepository estadoRepository;

    @MockBean
    private EstadoService estadoService;

    @Autowired
    private ObjectMapper objectMapper;

    // --- Casos positivos ---

    @Test
    void obtenerEstados_devuelveListaEstados() throws Exception {
        Estado estado = new Estado(1L, "Disponible");
        when(estadoService.ObtenerEstados()).thenReturn(List.of(estado));

        mockMvc.perform(get("/api/v1/estados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Disponible"));
    }

    @Test
    void obtenerEstadoPorId_existente() throws Exception {
        Estado estado = new Estado(1L, "Agotado");
        when(estadoService.obtenerEstadoPorId(1L)).thenReturn(Optional.of(estado));

        mockMvc.perform(get("/api/v1/estados/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Agotado"));
    }

    @Test
    void crearEstado_exitoso() throws Exception {
        Estado estado = new Estado(null, "Nuevo");
        Estado guardado = new Estado(10L, "Nuevo");

        when(estadoService.crearEstado(any())).thenReturn(guardado);

        mockMvc.perform(post("/api/v1/estados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estado)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.nombre").value("Nuevo"));
    }

    @Test
    void actualizarEstado_exitoso() throws Exception {
        Estado actualizado = new Estado(1L, "Entregado");

        when(estadoService.actualizarEstado(eq(1L), any())).thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/estados/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Entregado"));
    }

    @Test
    void eliminarEstado_exitoso() throws Exception {
        doNothing().when(estadoService).eliminarEstado(1L);

        mockMvc.perform(delete("/api/v1/estados/1"))
                .andExpect(status().isNoContent());
    }

    // --- Casos negativos ---

    @Test
    void obtenerEstadoPorId_noExistente() throws Exception {
        when(estadoService.obtenerEstadoPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/estados/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearEstado_error() throws Exception {
        Estado estado = new Estado(null, "Error");
        when(estadoService.crearEstado(any())).thenThrow(new RuntimeException("Falla al guardar"));

        mockMvc.perform(post("/api/v1/estados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estado)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Error al crear")));
    }

    @Test
    void actualizarEstado_noExistente() throws Exception {
        Estado estado = new Estado(1L, "SinCambios");
        when(estadoService.actualizarEstado(eq(1L), any())).thenThrow(new RuntimeException("Estado no encontrado"));

        mockMvc.perform(put("/api/v1/estados/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estado)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Error al actualizar")));
    }

    @Test
    void eliminarEstado_noExistente() throws Exception {
    doThrow(new RuntimeException("No encontrado")).when(estadoService).eliminarEstado(999L);

    mockMvc.perform(delete("/api/v1/estados/999"))
            .andExpect(status().isNotFound())
            .andExpect(content().string(org.hamcrest.Matchers.containsString("Error al eliminar el estado: No encontrado")));
}

}