package com.example.demo.demoejemplo.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.demoejemplo.models.entity.Pedido;

public interface IPedidocrudDao extends CrudRepository<Pedido, Long>{

}
