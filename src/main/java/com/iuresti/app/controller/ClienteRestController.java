package com.iuresti.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuresti.app.service.IClienteService;
import com.iuresti.app.view.ClienteList;

@RestController
@RequestMapping("/api/clientes")
public class ClienteRestController {
	
	@Autowired
	private IClienteService clienteService;
	
	/**
	 * Método que traduce la respuesta en un formato REST con un Stream de salida del tipo JSON o XML
	 * (Convierte un POJO en una respuesta JSON o XML)
	 * 
	 * Nota: Usando ResponseBody aqui significa que el listado de clientes se va a almacenar en el cuerpo
	 * de la respuesta y al guardarse de forma automatica Spring va a deducir de que es un REST,
	 * por lo tanto puede ser un JSON o un XML
	 * 
	 * @return
	 */
	@GetMapping(value = "/listar")
	public ClienteList listar() {
		return new ClienteList(clienteService.findAll());
	}

}
