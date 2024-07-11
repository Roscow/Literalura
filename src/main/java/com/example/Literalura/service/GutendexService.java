package com.example.Literalura.service;

import com.example.Literalura.model.*;
import com.example.Literalura.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Service
public class GutendexService {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LanguageRepository languageRepository;

    public void searchAndSaveBooks(String query) {
        String url = "https://gutendex.com/books?search=" + query;

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response);
            JsonNode results = root.path("results");

            int bookCount = results.size();
            System.out.println("Número de libros encontrados: " + bookCount);

            Scanner scanner = new Scanner(System.in);

            for (JsonNode result : results) {
                System.out.println("Título: " + result.path("title").asText());
                System.out.print("¿Desea ingresar este libro y sus datos en la base de datos? (s/n): ");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("s")) {
                    Long bookId = result.path("id").asLong();
                    String title = result.path("title").asText();
                    int downloadCount = result.path("download_count").asInt();

                    // Parse authors
                    List<Autor> authors = new ArrayList<>();
                    for (JsonNode authorNode : result.path("authors")) {
                        String authorName = authorNode.path("name").asText();
                        Integer birthYear = authorNode.path("birth_year").isNull() ? null : authorNode.path("birth_year").asInt();
                        Integer deathYear = authorNode.path("death_year").isNull() ? null : authorNode.path("death_year").asInt();

                        Optional<Autor> optionalAuthor = autorRepository.findByName(authorName);
                        Autor author;
                        if (optionalAuthor.isPresent()) {
                            author = optionalAuthor.get();
                            System.out.println("El autor ya existe, no se vuelve a registrar");
                        } else {
                            author = new Autor();
                            author.setName(authorName);
                            author.setBirthYear(birthYear);
                            author.setDeathYear(deathYear);
                            autorRepository.save(author);
                            System.out.println("El autor no existe, se registra");
                        }
                        authors.add(author);
                    }

                    // Parse languages
                    List<Language> languages = new ArrayList<>();
                    for (JsonNode langNode : result.path("languages")) {
                        String langName = langNode.asText();

                        Optional<Language> optionalLanguage = languageRepository.findByName(langName);
                        Language language;
                        if (optionalLanguage.isPresent()) {
                            language = optionalLanguage.get();
                        } else {
                            language = new Language();
                            language.setName(langName);
                            languageRepository.save(language);
                        }
                        languages.add(language);
                    }

                    // Parse subjects
                    List<String> subjects = new ArrayList<>();
                    for (JsonNode subjectNode : result.path("subjects")) {
                        subjects.add(subjectNode.asText());
                    }

                    // Create and save the book
                    Libro libro = new Libro();
                    libro.setTitle(title);
                    libro.setDownloadCount(downloadCount);
                    libro.setSubjects(subjects);
                    libro.setAuthors(authors);
                    libro.setLanguages(languages);
                    libroRepository.save(libro);
                    System.out.println("Libro guardado en la base de datos.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
