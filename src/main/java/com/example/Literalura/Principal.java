package com.example.Literalura;
import com.example.Literalura.model.Autor;
import com.example.Literalura.model.Libro;
import com.example.Literalura.repository.AutorRepository;
import com.example.Literalura.repository.LibroRepository;
import com.example.Literalura.service.GutendexService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class Principal {
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    private GutendexService gutendexService;

    Scanner sc = new Scanner(System.in);
    public Principal(AutorRepository autorRepository,LibroRepository libroRepository,GutendexService gutendexService) {
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
        this.gutendexService = gutendexService;
    }

    public void cicloLibro(){
        Integer opcion = 1;
        while (opcion != 0){
            System.out.println("-------Literalura------------");
            System.out.println("----------menu--------------");
            System.out.println(" 1- Buscar por titulo de libro ");
            System.out.println(" 2- Mostrar libros registrados ");
            System.out.println(" 3- Mostrar autores registrados");
            System.out.println(" 4- Mostrar autores vivos en un año");
            System.out.println(" 5- Mostrar libros segun un idioma ");
            System.out.println(" 0 - Salir...");
            System.out.println("Opcion: ");

            opcion = sc.nextInt();
            menuAccion(opcion);
        }
        System.out.println("Saliendo....");
    }

    public void menuAccion(Integer opcion){
        switch (opcion){
            case 1:
                System.out.println("-----------------Buscando por titulo de libro---------------------");
                System.out.println("Ingrese el título o parte del título del libro a buscar:");
                String query = sc.nextLine();
                gutendexService.searchAndSaveBooks(query);
                System.out.println("Búsqueda completada.");
                break;
            case 2:
                System.out.println("-----------------Listando libros registrados----------------");
                List<Libro> libros = new ArrayList<>();
                libros = libroRepository.findAll();
                for (Libro libro  : libros){
                    System.out.println(libro.toString());
                }
                break;
            case 3:
                System.out.println("--------------------Listando autores registrados----------------");
                List<Autor> autores = new ArrayList<>();
                autores = autorRepository.findAll();
                for (Autor autor : autores){
                    System.out.println("Nombre: "+ autor.getName());
                }
                break;
            case 4:
                System.out.println("-------------------Mostrar autores vivos segun año-------------------------");
                System.out.println("Ingrese el año en el cual buscar autores existentes:");
                int anio = sc.nextInt();
                List<Autor> autoresVivos = new ArrayList<>();
                autoresVivos = autorRepository.findAutoresVivosEnAno(anio);
                for (Autor autor : autoresVivos){
                    int edad = anio-autor.getBirthYear();
                    System.out.println("Nombre: "+ autor.getName() + " ("+ autor.getBirthYear()+ " - " +autor.getDeathYear()+ ") Edad: " +edad +" en " + anio);
                }
                break;
            case 5:
                System.out.println("------------------Mostrar libros segun idioma------------------------");
                System.out.println("Ingrese el idioma para buscar libros (por ejemplo, 'en' para inglés):");
                sc.nextLine();
                String language = sc.nextLine();
                List<Libro> librosPorIdioma = libroRepository.findLibrosByLanguage(language);
                for (Libro libro : librosPorIdioma) {
                    System.out.println(libro.toString());
                }
                break;

        }
    }
}
