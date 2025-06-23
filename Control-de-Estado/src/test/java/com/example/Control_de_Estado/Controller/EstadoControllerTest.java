package com.example.Control_de_Estado.Controller;

import com.example.Control_de_Estado.Model.Estado;
import com.example.Control_de_Estado.Service.EstadoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EstadoController.class)
public class EstadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstadoService estadoService;

    @Autowired
    private ObjectMapper objectMapper;

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
                .andExpect(jsonPath("$.id").value(10));
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
}