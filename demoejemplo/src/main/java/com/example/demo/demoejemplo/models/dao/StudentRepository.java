package com.example.demo.demoejemplo.models.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.demoejemplo.models.entity.ProductoBD;


@Repository
public interface StudentRepository extends CrudRepository<ProductoBD, Long> {
    
    List<ProductoBD> findByNombre(String name);
    
}
