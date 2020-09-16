package com.iuresti.app.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.xml.MarshallingView;

import com.iuresti.app.model.Cliente;

@Component("listar.xml")
public class ClienteListXmlView extends MarshallingView {

	@Autowired
	public ClienteListXmlView(Jaxb2Marshaller marshaller) {
		super(marshaller);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		// Quitamos las cosas que no queremos dentro del xml
		model.remove("titulo");
		model.remove("page");
		
		// Obtenemos los clientes paginados
		Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");
		
		// Lo eliminamos
		model.remove("clientes");
		
		// Modificamos los clientes, convertimos los clientes al tipo list y se lo pasamos a la clase wrapper
		// y lo guardamos en el model y se lo pasamos al metodo de la clase padre para renderizar a xml
		model.put("clienteList", new ClienteList(clientes.getContent()));
		
		super.renderMergedOutputModel(model, request, response);
	}

}
