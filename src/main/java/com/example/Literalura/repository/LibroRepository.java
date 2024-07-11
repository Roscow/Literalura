package com.example.Literalura.repository;
import com.example.Literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    @Query("SELECT l FROM Libro l JOIN l.languages lang WHERE lang.name = :language")
    List<Libro> findLibrosByLanguage(@Param("language") String language);
}
