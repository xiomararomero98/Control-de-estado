package com.example.Control_de_Estado.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Control_de_Estado.Model.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado,Long> {

}
