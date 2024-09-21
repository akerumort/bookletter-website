package com.akerumort.libraryservice.repos;

import com.akerumort.libraryservice.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
