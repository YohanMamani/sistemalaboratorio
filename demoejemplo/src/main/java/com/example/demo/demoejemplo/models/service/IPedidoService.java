package com.example.demo.demoejemplo.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.demoejemplo.models.entity.Pedido;

public interface IPedidoService {
	
	public void saved(String pedidoJson);

	public List<Pedido> findAll();
	
	public Page<Pedido> findAll(Pageable pageable);
	
	public Page<Pedido> findAll2(Pageable pageable);


	public Pedido fetchByIdWithFacturas(Long id);
	
	public Pedido findOne(Long id);
	
	public void delete(Long id);
	
	public String confirmar(Long id);	

}
