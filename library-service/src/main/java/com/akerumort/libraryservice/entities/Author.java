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
@Table(name = "author")
@Schema(description = "Entity representing an author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Author ID", example = "1")
    private Long id;

    @Column(name = "first_name", nullable = false)
    @Schema(description = "First name of the author", example = "Ivan")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Schema(description = "Last name of the author", example = "Ivanov")
    private String lastName;

    @Column(name = "country", nullable = false)
    @Schema(description = "Country of the author", example = "Russia")
    private String country;

    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "author_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    @Schema(description = "Set of books associated with the author")
    private Set<Book> books = new HashSet<>();
}
