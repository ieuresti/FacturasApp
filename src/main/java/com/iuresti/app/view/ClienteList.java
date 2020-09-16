package com.iuresti.app.view;

// Clase ClienteList que envuelve nuestro obj list o collection (wrapper) que representa nuestro xml root

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.iuresti.app.model.Cliente;

@XmlRootElement(name = "clientesList")
public class ClienteList {
	
	@XmlElement(name = "cliente")
	public List<Cliente> clientes;
	
	public ClienteList() {}

	public ClienteList(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

}
