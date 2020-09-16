package com.iuresti.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iuresti.app.dao.IUsuarioDao;
import com.iuresti.app.model.Role;
import com.iuresti.app.model.Usuario;

@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService {
	
	@Autowired
	private IUsuarioDao usuarioDao;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioDao.findByUsername(username); // Obtenemos el usuario
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(); // Obtener sus roles 1 x 1 y registrar estos roles dentro de una lista
		for (Role role : usuario.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}
		// Retornamos el obj UserDetails (una interfaz, su impl)
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
	}

}
