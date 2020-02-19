package com.example.demo.demoejemplo.models.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.demoejemplo.models.entity.Cliente;
import com.example.demo.demoejemplo.models.entity.Pedido;

@Repository
public interface IPedidoDao extends  PagingAndSortingRepository<Pedido, Long> {
	
	@Query("select c from Pedido c where c.id=?1")
	public Pedido fetchByIdWithFacturas(Long id);
	
    @Modifying
    @Query("Update Pedido t SET t.estado=:estado WHERE t.id=:id")
    public void updateTitle(@Param("id") Long id, @Param("estado") String estado);
	
}
