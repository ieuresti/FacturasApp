package com.iuresti.app;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	/**
	 * Método para registrar un controlador de vista
	 */
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/error_403").setViewName("error_403");
	}

	/**
	 * Método que se encarga del adaptador en el cual vamos a guardar nuestro locale
	 * ej en la sesion http o en una cookie
	 * 
	 * @return
	 */
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(new Locale("es", "ES"));
		return localeResolver;
	}

	/**
	 * Método que se encarga del interceptor para cambiar el locale cada vez que se
	 * envie el parametro del lenguaje con el nuevo idioma para cambiar los textos
	 * de nuestra pag
	 * 
	 * @return
	 */
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
		// Cada vez que se pase por url (a traves del metodo get) el parametro lang + el valor "es_ES" se ejecutara el interceptor y realizara el cambio
		localeInterceptor.setParamName("lang");
		return localeInterceptor;
	}

	/**
	 * Método para registrar el interceptor
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
	
	/**
	 * Bean que se usa en nuestra vista xml para convertir la respuesta/contenido (el obj entity)
	 * en un documento xml 
	 * 
	 * @return
	 */
	@Bean
	public Jaxb2Marshaller jaxb2Marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(new Class[] {com.iuresti.app.view.ClienteList.class});
		return marshaller;
	}

}
