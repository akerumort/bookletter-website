package com.akerumort.libraryservice.controllers;

import com.akerumort.libraryservice.dto.BookDTO;
import com.akerumort.libraryservice.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import(BookControllerTest.TestConfig.class)
public class BookControllerTest {

    @Autowired
    private BookController bookController;

    @Autowired
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        when(bookService.getAllBooks()).thenReturn(Collections.emptyList());
    }

    @Test
    public void testGetAllBooks() {
        ResponseEntity<List<BookDTO>> response = bookController.getAllBooks();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetBookById_validId() {
        BookDTO book = new BookDTO();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setGenre("Test Genre");
        book.setPublicationYear(2024);
        Set<Long> authorIds = new HashSet<>();
        authorIds.add(1L);
        book.setAuthorIds(authorIds);

        when(bookService.getBookById(1L)).thenReturn(book);

        ResponseEntity<BookDTO> response = bookController.getBookById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book.getId(), response.getBody().getId());
    }

    @Test
    public void testGetBookById_invalidId() {
        when(bookService.getBookById(999L)).thenReturn(null);

        ResponseEntity<BookDTO> response = bookController.getBookById(999L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testCreateBook() {
        BookDTO book = new BookDTO();
        book.setTitle("Test Book");
        book.setGenre("Test Genre");
        book.setPublicationYear(2024);
        Set<Long> authorIds = new HashSet<>();
        authorIds.add(1L);
        book.setAuthorIds(authorIds);

        BookDTO createdBook = new BookDTO();
        createdBook.setId(1L);
        createdBook.setTitle("Test Book");
        createdBook.setGenre("Test Genre");
        createdBook.setPublicationYear(2024);
        createdBook.setAuthorIds(authorIds);

        when(bookService.createBook(Mockito.any(BookDTO.class))).thenReturn(createdBook);

        ResponseEntity<BookDTO> response = bookController.createBook(book);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createdBook.getId(), response.getBody().getId());
    }

    @Test
    public void testUpdateBook_validId() {
        BookDTO book = new BookDTO();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setGenre("Test Genre");
        book.setPublicationYear(2025);
        Set<Long> authorIds = new HashSet<>();
        authorIds.add(1L);
        book.setAuthorIds(authorIds);

        when(bookService.updateBook(eq(1L), Mockito.any(BookDTO.class))).thenReturn(book);

        ResponseEntity<BookDTO> response = bookController.updateBook(1L, book);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(book.getTitle(), response.getBody().getTitle());
    }

    @Test
    public void testUpdateBook_invalidId() {
        when(bookService.updateBook(eq(999L), Mockito.any(BookDTO.class))).thenReturn(null);

        BookDTO book = new BookDTO();
        book.setId(999L);
        book.setTitle("Updated Book");
        book.setGenre("Updated Genre");
        book.setPublicationYear(2024);
        Set<Long> authorIds = new HashSet<>();
        authorIds.add(1L);
        book.setAuthorIds(authorIds);

        ResponseEntity<BookDTO> response = bookController.updateBook(999L, book);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteBook_validId() {
        doNothing().when(bookService).deleteBook(1L);

        ResponseEntity<Void> response = bookController.deleteBook(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteBook_invalidId() {
        doThrow(new RuntimeException("Book not found")).when(bookService).deleteBook(999L);

        assertThrows(RuntimeException.class, () -> bookController.deleteBook(999L));
    }

    @Test
    public void testDeleteAllBooks() {
        doNothing().when(bookService).deleteAllBooks();

        ResponseEntity<Void> response = bookController.deleteAllBooks();

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Configuration
    public static class TestConfig {
        @Bean
        public BookController bookController(BookService bookService) {
            return new BookController(bookService);
        }

        @Bean
        public BookService bookService() {
            return Mockito.mock(BookService.class);
        }
    }
}
