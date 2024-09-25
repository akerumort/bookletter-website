package com.akerumort.libraryservice.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "book")
@Schema(description = "Entity representing a book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Book ID", example = "1")
    private Long id;

    @Column(name = "title", nullable = false)
    @Schema(description = "Title of the book", example = "Java. The Complete Reference")
    private String title;

    @Column(name = "genre", nullable = false)
    @Schema(description = "Genre of the book", example = "Programming")
    private String genre;

    @Column(name = "publication_year")
    @Schema(description = "Publication year of the book", example = "2023")
    private Integer publicationYear;

    @ManyToMany(mappedBy = "books")
    @Schema(description = "Set of authors associated with the book")
    private Set<Author> authors = new HashSet<>();
}
