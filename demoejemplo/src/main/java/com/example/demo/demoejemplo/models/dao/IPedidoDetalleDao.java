package com.example.demo.demoejemplo.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.demoejemplo.models.entity.PedidoDetalle;

public interface IPedidoDetalleDao  extends CrudRepository<PedidoDetalle, Long>{

	
	@Query("select p from PedidoDetalle p where p.pedido.id =?1")
	public List<PedidoDetalle> findByidpedido(Long id); 
	
	@Query("delete PedidoDetalle p where p.pedido.id =?1")
	public void eliminarByidpedido(Long id); 
	
	
}
