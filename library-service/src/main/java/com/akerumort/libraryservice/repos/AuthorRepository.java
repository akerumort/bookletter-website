package com.akerumort.libraryservice.repos;

import com.akerumort.libraryservice.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
