package com.iuresti.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.iuresti.app.service.IUploadFileService;

@SpringBootApplication
public class FacturacionApplication implements CommandLineRunner {
	
	@Autowired
	IUploadFileService uploadFileService;

	public static void main(String[] args) {
		SpringApplication.run(FacturacionApplication.class, args);
	}

	/**
	 * Método para crear de forma automatica el directorio de "uploads"
	 */
	@Override
	public void run(String... args) throws Exception {
		uploadFileService.deleteAll(); // 1ero se elimina todo
		uploadFileService.init(); // Despues se vuelve a inicializar
	}

}
