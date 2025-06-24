package com.example.Control_de_Estado.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Control_de_Estado.Model.Estado;
import com.example.Control_de_Estado.Service.EstadoService;

@RestController
@RequestMapping("/api/v1/estados")
public class EstadoController {
    
    @Autowired
    private EstadoService estadoService;
//obtener todos los estados
    @GetMapping
    public ResponseEntity<List<Estado>>obtenerEstados(){
        List <Estado> estados = estadoService.ObtenerEstados();
        if (estados.isEmpty()) {
            return ResponseEntity.noContent().build();      
        }
        return ResponseEntity.ok(estados);
    }

    //obtener estados por Id
    @GetMapping("/{id}")
    public ResponseEntity<Estado> obtenerEstadoPorId(@PathVariable Long id){
        Optional <Estado> estado =estadoService.obtenerEstadoPorId(id);
        if (estado.isEmpty()) {
            return ResponseEntity.notFound().build();
            
        }
        return ResponseEntity.ok(estado.get());
    }

    //crear un nuevo estado
    @PostMapping
    public ResponseEntity<?> crearEstado(@RequestBody Estado estado){
        try {
            Estado nuevo= estadoService.crearEstado(estado);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el estado:"+ e.getMessage());

        }

    }
    //Actualizar un estado por ID

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEstadoPorId(@PathVariable Long id, @RequestBody Estado estadoActualizado){
    try {
        Estado estado = estadoService.actualizarEstado(id, estadoActualizado);
        return ResponseEntity.ok(estado);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar el estado: " + e.getMessage());
    }
}


    //Eliminar un estado por id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEstadoporId(@PathVariable Long id){
        try {
            estadoService.eliminarEstado(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error al eliminar el estado"+ e.getMessage());
        }
    }
    



    


}
