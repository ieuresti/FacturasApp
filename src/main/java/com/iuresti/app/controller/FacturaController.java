package com.iuresti.app.controller;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.iuresti.app.model.Cliente;
import com.iuresti.app.model.Factura;
import com.iuresti.app.model.ItemFactura;
import com.iuresti.app.model.Producto;
import com.iuresti.app.service.IClienteService;

@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {
	
	@Autowired
	private IClienteService clienteService;
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash, Locale locale) {
		//Factura factura = clienteService.findFacturaById(id);
		Factura factura = clienteService.fetchFacturaByIdWithClienteWithItemFacturaWithProducto(id);
		if (factura == null) {
			flash.addFlashAttribute("error", messageSource.getMessage("text.factura.flash.db.error", null, locale));
			return "redirect:/listar";
		}
		model.addAttribute("factura", factura);
		model.addAttribute("titulo", String.format(messageSource.getMessage("text.factura.ver.titulo", null, locale), factura.getDescripcion()));
		return "factura/ver";
	}
	
	@GetMapping("/form/{clienteId}")
	public String crear(@PathVariable("clienteId") Long clienteId, Model model, RedirectAttributes flash, Locale locale) {
		Cliente cliente = clienteService.findOne(clienteId); // Obtener el cliente por su id
		if (cliente == null) {
			flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.db.error", null, locale));
			return "redirect:/listar";
		}
		
		Factura factura = new Factura();
		factura.setCliente(cliente); // Relacionando la factura con el cliente
		model.addAttribute("factura", factura); // Pasando la factura a la vista o formulario
		model.addAttribute("titulo", messageSource.getMessage("text.factura.form.titulo", null, locale));
		return "factura/form";
	}
	
	@GetMapping(value = "/cargar-productos/{term}", produces = {"application/json"}) // Generar y producir una salida json
	// ResponseBody transforma la salida en json y la guarda dentro del response en el body
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term) {
		return clienteService.findByNombre(term);
	}
	
	@PostMapping("/form")
	public String guardar(@Valid Factura factura, BindingResult result, Model model, @RequestParam(name = "item_id[]", required = false) Long[] itemId,
			@RequestParam(name = "cantidad[]", required = false) Integer[] cantidad, RedirectAttributes flash, SessionStatus status, Locale locale) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", messageSource.getMessage("text.factura.form.titulo", null, locale));
			return "factura/form";
		}
		
		if (itemId == null || itemId.length == 0) {
			model.addAttribute("titulo", messageSource.getMessage("text.factura.form.titulo", null, locale));
			model.addAttribute("error", messageSource.getMessage("text.factura.flash.lineas.error", null, locale));
			return "factura/form";
		}
		
		for (int i = 0; i < itemId.length; i++) {
			// Por cada linea de la factura, obtenemos el producto
			Producto producto = clienteService.findProductoById(itemId[i]);
			// Formamos la linea con todos los datos necesarios
			ItemFactura linea = new ItemFactura();
			linea.setCantidad(cantidad[i]);
			linea.setProducto(producto);
			// Agregar la linea a la factura
			factura.addItemFactura(linea);
		}
		clienteService.saveFactura(factura);
		status.setComplete(); // Finalizar el SessionAttribute (finalizar proceso y eliminar factura de la sesion)
		flash.addFlashAttribute("success", messageSource.getMessage("text.factura.flash.crear.success", null, locale));
		return "redirect:/ver/" + factura.getCliente().getId(); // Redirigir al detalle del cliente para mostrar el listado de sus facturas
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Locale locale) {
		Factura factura = clienteService.findFacturaById(id);
		if (factura != null) {
			clienteService.deleteFactura(id);
			flash.addFlashAttribute("success", messageSource.getMessage("text.factura.flash.eliminar.success", null, locale));
			return "redirect:/ver/" + factura.getCliente().getId();
		}
		flash.addFlashAttribute("error", messageSource.getMessage("text.factura.flash.db.error", null, locale));
		return "redirect:/listar";
	}

}
