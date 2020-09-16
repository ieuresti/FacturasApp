package com.iuresti.app.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {

	private String url;
	private Page<T> page;

	private int totalPaginas;
	private int numElementosPorPagina;

	private int paginaActual;
	
	private List<PageItem> paginas;

	public PageRender(String url, Page<T> page) {
		this.url = url;
		this.page = page;
		this.paginas = new ArrayList<PageItem>();

		numElementosPorPagina = page.getSize(); // Cantidad elementos x pagina (En el controlador lo inicializamos)
		totalPaginas = page.getTotalPages();
		paginaActual = page.getNumber() + 1;

		int desde, hasta;
		if (totalPaginas <= numElementosPorPagina) { // Si es true mostrara el paginador completo
			desde = 1;
			hasta = totalPaginas;
		} else {
			// Paginacion por rangos
			if (paginaActual <= numElementosPorPagina / 2) { // Rango inicial
				desde = 1;
				hasta = numElementosPorPagina;
			} else if (paginaActual >= totalPaginas - numElementosPorPagina / 2) { // Rango final
				desde = totalPaginas - numElementosPorPagina + 1;
				hasta = numElementosPorPagina;
			} else { // Rango intermedio
				desde = paginaActual - numElementosPorPagina / 2;
				hasta = numElementosPorPagina;
			}
		}
		
		// Empezar a recorrer el hasta y empezar a llenar las pags con c/u de los items
		for (int i=0; i < hasta; i++) {
			paginas.add(new PageItem(desde + i, paginaActual == desde + i));
		}

	}

	public String getUrl() {
		return url;
	}

	public int getTotalPaginas() {
		return totalPaginas;
	}

	public int getPaginaActual() {
		return paginaActual;
	}

	public List<PageItem> getPaginas() {
		return paginas;
	}
	
	public boolean isFirst() {
		return page.isFirst();
	}
	
	public boolean isLast() {
		return page.isLast();
	}
	
	public boolean isHasNext() {
		return page.hasNext();
	}
	
	public boolean isHasPrevious() {
		return page.hasPrevious();
	}

}
