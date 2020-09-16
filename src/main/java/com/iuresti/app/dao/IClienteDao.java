package com.iuresti.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iuresti.app.model.Cliente;

public interface IClienteDao extends JpaRepository<Cliente, Long> {
	
	// Usando left join fetch accedemos a clientes incluso cuando estos no tengan facturas asignadas o creadas
	@Query("select c from Cliente c left join fetch c.facturas f where c.id=?1")
	public Cliente fetchByIdWithFacturas(Long id);

}
