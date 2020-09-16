package com.iuresti.app.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.*;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.iuresti.app.model.Cliente;
import com.iuresti.app.service.IClienteService;
import com.iuresti.app.service.IUploadFileService;
import com.iuresti.app.util.PageRender;

@Controller
@SessionAttributes("cliente")
public class ClienteController {
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private IClienteService clienteService;
	
	@Autowired
	private IUploadFileService uploadFileService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping("/uploads/{filename:.+}") // Esta expresion permite que Spring no borre la extencion del archivo
	public ResponseEntity<Resource> verImagen(@PathVariable String filename) {
		Resource recurso = null;
		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}
	
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash, Locale locale) {
		//Cliente cliente = clienteService.findOne(id);
		Cliente cliente = clienteService.fetchByIdWithFacturas(id);
		if (cliente == null) {
			flash.addAttribute("error", messageSource.getMessage("text.cliente.flash.db.error", null, locale));
			return "redirect:/listar";
		}
		// Si esta todo bien pasamos el cliente a la vista junto con la img
		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", messageSource.getMessage("text.cliente.detalle.titulo", null, locale).concat(": ").concat(cliente.getNombre()));
		return "ver";
	}
	
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
	@GetMapping(value = "/listar-rest")
	public @ResponseBody List<Cliente> listarRest() {
		//return new ClienteList(clienteService.findAll()); Esto es en caso de querer xml + json
		return clienteService.findAll();
	}
	
	@GetMapping(value = {"/listar", "/"})
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model, Authentication authentication, HttpServletRequest request, Locale locale) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// Esta parte solo es visible en la consola, solo para pruebas
		if (hasRole("ROLE_ADMIN")) {
			logger.info("Hola ".concat(auth.getName()).concat(" tienes acceso!"));
		} else {
			logger.info("Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
		}
		// Esta parte solo es visible en la consola, solo para pruebas
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");
		if (securityContext.isUserInRole("ADMIN")) {
			logger.info("Forma usando SecurityContextHolderAwareRequestWrapper: Hola ".concat(auth.getName()).concat(" tienes acceso!"));
		} else {
			logger.info("Forma usando SecurityContextHolderAwareRequestWrapper: Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
		}
		
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
		
		model.addAttribute("titulo", messageSource.getMessage("text.cliente.listar.titulo", null, locale));
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}
	
	@GetMapping("/form")
	public String crear(Model model, Locale locale) {
		Cliente cliente = new Cliente();
		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", messageSource.getMessage("text.cliente.form.titulo.crear", null, locale));
		return "form";
	}
	
	@GetMapping("/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash, Locale locale) {		
		Cliente cliente = null;		
		if (id > 0) {
			cliente = clienteService.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.db.error", null, locale));
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.id.error", null, locale));
			return "redirect:/listar";
		}
		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", messageSource.getMessage("text.cliente.form.titulo.editar", null, locale));
		return "form";
	}
	
	@PostMapping("/form")
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model, @RequestParam("file") MultipartFile imagen, RedirectAttributes flash, SessionStatus status, Locale locale) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", messageSource.getMessage("text.cliente.form.titulo", null, locale));
			return "form";
		}
		
		if (!imagen.isEmpty()) {
			String uniqueFilename = null;
			try {
				uniqueFilename = uploadFileService.copy(imagen);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flash.addFlashAttribute("info", messageSource.getMessage("text.cliente.flash.foto.subir.success", null, locale) + "'" + uniqueFilename + "'");
			cliente.setFoto(uniqueFilename); // Pasamos el nombre de la img al cliente
		}
		
		String mensajeFlash = (cliente.getId() != null)? messageSource.getMessage("text.cliente.flash.editar.success", null, locale) : messageSource.getMessage("text.cliente.flash.crear.success", null, locale);
		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Locale locale) {		
		if (id > 0) {
			Cliente cliente = clienteService.findOne(id);
			
			clienteService.delete(id);
			flash.addFlashAttribute("success", messageSource.getMessage("text.cliente.flash.eliminar.success", null, locale));
			
			// Para eliminar la imagen
			if (uploadFileService.delete(cliente.getFoto())) {
				String mensajeFotoEliminar = String.format(messageSource.getMessage("text.cliente.flash.foto.eliminar.success", null, locale), cliente.getFoto());
				flash.addFlashAttribute("info", mensajeFotoEliminar);
			}
		}
		return "redirect:/listar";
	}
	
	/**
	 * Método para validar si el usuario tiene un rol
	 * 
	 * @param role
	 * @return
	 */
	private boolean hasRole(String role) {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null) {
			return false; // No tiene acceso
		}
		Authentication auth = context.getAuthentication();
		if (auth == null) {
			return false; // No tiene acceso
		}
		// Obtenemos una coleccion de roles
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		// Iteramos y preguntamos por cada authority si el rol = al que estamos pasando por parametro
		for (GrantedAuthority authority: authorities) {
			if (role.equals(authority.getAuthority())) {
				logger.info("Hola usuario ".concat(auth.getName()).concat(" tu rol es: ".concat(authority.getAuthority())));
				return true;
			}
		}
		// Si no encuentra el rol, no tiene acceso
		return false;
	}
	
	/**
	 * Utileria para encriptar texto con el algorito BCrypt
	 * 
	 * @param texto
	 * @return
	 */
	@GetMapping("/bcrypt/{texto}")
	@ResponseBody // Al hacer una peticion a este metodo en vez de buscar una vista, se va a regresar el texto directamente al navegador
	public String encriptar(@PathVariable("texto") String texto) {
		return texto + " Encriptado en Bcrypt: " + passwordEncoder.encode(texto);
	}

}
