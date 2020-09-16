package com.iuresti.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

import com.iuresti.app.auth.handler.LoginSuccessHandler;
import com.iuresti.app.service.JpaUserDetailsService;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private LoginSuccessHandler successHandler;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JpaUserDetailsService userDetailsService;

	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {
		
		builder.userDetailsService(userDetailsService)
		.passwordEncoder(passwordEncoder);
		
		/*
		PasswordEncoder encoder = passwordEncoder();
		// Configuramos la forma en que se va a encriptar la contraseña usando una funcion Lambda (forma abreviada)
		//UserBuilder users = User.builder().passwordEncoder(password -> encoder.encode(password)); 
		UserBuilder users = User.builder().passwordEncoder(encoder::encode);
		// Creamos usuarios, contraseñas y roles en memoria
		builder.inMemoryAuthentication()
		.withUser(users.username("admin").password("12345").roles("ADMIN", "USER"))
		.withUser(users.username("ivan").password("12345").roles("USER"));
		*/

	}
	
	/**
	 * Personalizamos el Acceso a las URLs de la aplicacion
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
		// Rutas publicas que no requeriran autenticacion y recursos estaticos
		.antMatchers("/", "/css/**", "/js/**", "/images/**", "/listar**", "/locale", "/api/clientes/**").permitAll()
		
		// Rutas privadas que requeriran autenticacion por ROLES
		.antMatchers("/ver/**").hasAnyRole("USER")
		.antMatchers("/uploads/**").hasAnyRole("USER")
		.antMatchers("/form/**").hasAnyRole("ADMIN")
		.antMatchers("/eliminar/**").hasAnyRole("ADMIN")
		.antMatchers("/factura/**").hasAnyRole("ADMIN")
		
		// Todas las demas URLs de la Aplicacion requieren autenticacion
		.anyRequest().authenticated()
		
		// El formulario de Login no requiere autenticacion
		.and().formLogin().successHandler(successHandler).loginPage("/login").permitAll()
		.and().logout().permitAll()
		
		// Configuracion de las paginas de errores
		.and().exceptionHandling().accessDeniedPage("/error_403");
		
	}

	/**
	 * Implementación de Spring Security que encripta passwords con el algoritmo Bcrypt
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
