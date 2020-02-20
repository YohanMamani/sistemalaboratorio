package com.example.demo.demoejemplo.models.dao;


import org.springframework.data.repository.CrudRepository;

import com.example.demo.demoejemplo.models.entity.ProductoBD;

public interface IProductoBDDao  extends CrudRepository<ProductoBD, Long> {

}
