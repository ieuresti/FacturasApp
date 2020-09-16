package com.iuresti.app.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LocaleController {

	/**
	 * Método que obtiene a traves del request la cabecera/header y a traves de ella
	 * obtener la ultima url en la que estabamos
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/locale")
	public String locale(HttpServletRequest request) {
		String ultimaUrl = request.getHeader("referer"); // Referencia de nuestra ultima url
		return "redirect:".concat(ultimaUrl);
	}

}
