package com.example.Control_de_Estado.Config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.Control_de_Estado.Model.Estado;
import com.example.Control_de_Estado.Repository.EstadoRepository;

@Configuration
public class LoadDataBaseEstado {

    @Bean
    CommandLineRunner initDataBase(EstadoRepository estadoRepository){
        return args ->{
            if (estadoRepository.count()==0) {
                estadoRepository.save(new Estado(null, "Disponible"));
                estadoRepository.save(new Estado(null, "Agotado"));
                estadoRepository.save(new Estado(null, "En Proceso"));
                estadoRepository.save(new Estado(null, "Entregado"));
                estadoRepository.save(new Estado(null, "Cancelado"));
                System.out.println("Estados cargados correctamente");
                
            } else {
                System.out.println("Estados ya fueron creados,no se cargaron nuevos.");
                
            }
        };
    }

}
