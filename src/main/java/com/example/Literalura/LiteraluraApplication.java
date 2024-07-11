package com.example.Literalura;
import com.example.Literalura.repository.AutorRepository;
import com.example.Literalura.repository.LibroRepository;
import com.example.Literalura.service.GutendexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	@Autowired
	private GutendexService gutendexService;
	@Autowired
	private AutorRepository autorRepository;
	@Autowired
	private LibroRepository libroRepository; ;

	//Principal principal = new Principal(autorRepository, libroRepository);

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(autorRepository, libroRepository, gutendexService);
		principal.cicloLibro();
	}
}
