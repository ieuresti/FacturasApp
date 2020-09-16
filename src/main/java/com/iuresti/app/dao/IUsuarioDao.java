package com.iuresti.app.dao;

import org.springframework.data.repository.CrudRepository;

import com.iuresti.app.model.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {
	
	public Usuario findByUsername(String username);

}
