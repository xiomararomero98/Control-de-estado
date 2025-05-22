package com.example.Control_de_Estado.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Control_de_Estado.Model.Estado;
import com.example.Control_de_Estado.Repository.EstadoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;


//metodo para obtener estados
    public List<Estado>ObtenerEstados(){
        return estadoRepository.findAll();
    }

    //metodo para obtener estados por id
    public Optional <Estado> obtenerEstadoPorId(Long id){
        return estadoRepository.findById(id);
    }

    //metodo para crear estado
    public Estado crearEstado(Estado estado){
        return estadoRepository.save(estado);
    }

    //metodo para actualizar estado
    public Estado actualizarEstado(Long id, Estado estadoActualizado){
        Optional<Estado> optionalEstado = estadoRepository.findById(id);

        if (optionalEstado.isPresent()) {
            
            Estado estadoExistente= optionalEstado.get();
            estadoExistente.setNombre(estadoActualizado.getNombre());
            return estadoRepository.save(estadoExistente);
            
        } else {
            throw new RuntimeException("Estado no encontrado con ID"+ id);
            
        }
    }

    //metodo para eliminar estado
    public void eliminarEstado(Long id){
        estadoRepository.deleteById(id);
    }


}
