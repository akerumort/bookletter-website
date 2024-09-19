package com.akerumort.LibraryService.repos;

import com.akerumort.LibraryService.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
