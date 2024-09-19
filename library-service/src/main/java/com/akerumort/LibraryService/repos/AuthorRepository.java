package com.akerumort.LibraryService.repos;

import com.akerumort.LibraryService.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
