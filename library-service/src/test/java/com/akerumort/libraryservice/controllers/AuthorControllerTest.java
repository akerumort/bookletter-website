package com.akerumort.libraryservice.controllers;

import com.akerumort.libraryservice.dto.BookIdsDTO;
import com.akerumort.libraryservice.exceptions.CustomException;
import com.akerumort.libraryservice.services.AuthorService;
import com.akerumort.libraryservice.dto.AuthorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import(AuthorControllerTest.TestConfig.class)
public class AuthorControllerTest {

    @Autowired
    private AuthorController authorController;

    @Autowired
    private AuthorService authorService;

    @BeforeEach
    public void setUp() {
        when(authorService.getAllAuthors()).thenReturn(Collections.emptyList());
    }

    @Test
    public void testGetAllAuthors() {
        ResponseEntity<List<AuthorDTO>> response = authorController.getAllAuthors();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetAuthorById_validId() {
        AuthorDTO author = new AuthorDTO();
        author.setId(1L);
        author.setFirstName("Ivan");
        author.setLastName("Ivanov");
        author.setCountry("Russia");

        when(authorService.getAuthorById(1L)).thenReturn(author);

        ResponseEntity<AuthorDTO> response = authorController.getAuthorById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(author.getId(), response.getBody().getId());
    }

    @Test
    public void testGetAuthorById_invalidId() {
        when(authorService.getAuthorById(999L)).thenReturn(null);

        ResponseEntity<AuthorDTO> response = authorController.getAuthorById(999L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testCreateAuthor() {
        AuthorDTO author = new AuthorDTO();
        author.setFirstName("Ivan");
        author.setLastName("Ivanov");
        author.setCountry("Russia");

        AuthorDTO savedAuthor = new AuthorDTO();
        savedAuthor.setId(1L);
        savedAuthor.setFirstName("Ivan");
        savedAuthor.setLastName("Ivanov");
        savedAuthor.setCountry("Russia");

        when(authorService.createAuthor(Mockito.any(AuthorDTO.class))).thenReturn(savedAuthor);

        ResponseEntity<AuthorDTO> response = authorController.createAuthor(author);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(savedAuthor.getId(), response.getBody().getId());
    }

    @Test
    public void testUpdateAuthor_validId() {
        AuthorDTO author = new AuthorDTO();
        author.setId(1L);
        author.setFirstName("Ivan");
        author.setLastName("Ivanov");
        author.setCountry("Russia");

        when(authorService.updateAuthor(eq(1L), Mockito.any(AuthorDTO.class))).thenReturn(author);

        ResponseEntity<AuthorDTO> response = authorController.updateAuthor(1L, author);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(author.getLastName(), response.getBody().getLastName());
    }

    @Test
    public void testUpdateAuthor_invalidId() {
        when(authorService.updateAuthor(eq(999L), Mockito.any(AuthorDTO.class))).thenReturn(null);

        AuthorDTO author = new AuthorDTO();
        author.setId(999L);
        author.setFirstName("Update");
        author.setLastName("Updatov");
        author.setCountry("Updatia");

        ResponseEntity<AuthorDTO> response = authorController.updateAuthor(999L, author);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteAuthor_validId() {
        // т.к. метод удаления void - помечаем, что ничего не должно возвращаться
        doNothing().when(authorService).deleteAuthor(1L);

        ResponseEntity<Void> response = authorController.deleteAuthor(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteAuthor_invalidId() {
        doThrow(new RuntimeException("Author not found")).when(authorService).deleteAuthor(999L);

        assertThrows(RuntimeException.class, () -> authorController.deleteAuthor(999L));
    }

    @Test
    public void testDeleteAllAuthors() {
        doNothing().when(authorService).deleteAllAuthors();

        ResponseEntity<Void> response = authorController.deleteAllAuthors();

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testAddBooksToAuthor_success() {
        Long authorId = 1L;
        Set<Long> bookIds = Set.of(1L, 2L);
        BookIdsDTO bookIdsDTO = new BookIdsDTO();
        bookIdsDTO.setBookIds(bookIds);

        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(authorId);
        authorDTO.setFirstName("Ivan");
        authorDTO.setLastName("Ivanov");
        authorDTO.setCountry("Russia");
        authorDTO.setBookIds(bookIds);

        when(authorService.addBooksToAuthor(authorId, bookIds)).thenReturn(authorDTO);

        ResponseEntity<AuthorDTO> response = authorController.addBooksToAuthor(authorId, bookIdsDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(authorId, response.getBody().getId());
        assertEquals(bookIds, response.getBody().getBookIds());
    }

    @Test
    public void testAddBooksToAuthor_authorNotFound() {
        Long authorId = 999L;
        Set<Long> bookIds = Set.of(1L, 2L);
        BookIdsDTO bookIdsDTO = new BookIdsDTO();
        bookIdsDTO.setBookIds(bookIds);

        when(authorService.addBooksToAuthor(authorId, bookIds))
                .thenThrow(new CustomException("Author with ID " + authorId + " does not exist."));

        CustomException exception = assertThrows(CustomException.class,
                () -> authorController.addBooksToAuthor(authorId, bookIdsDTO));

        assertEquals("Author with ID " + authorId + " does not exist.", exception.getMessage());
    }

    @Test
    public void testAddBooksToAuthor_booksNotFound() {
        Long authorId = 1L;
        Set<Long> bookIds = Set.of(1L, 999L);
        BookIdsDTO bookIdsDTO = new BookIdsDTO();
        bookIdsDTO.setBookIds(bookIds);

        when(authorService.addBooksToAuthor(authorId, bookIds))
                .thenThrow(new CustomException("One or more of the books do not exist."));

        CustomException exception = assertThrows(CustomException.class,
                () -> authorController.addBooksToAuthor(authorId, bookIdsDTO));

        assertEquals("One or more of the books do not exist.", exception.getMessage());
    }

    @Configuration
    public static class TestConfig {
        @Bean
        public AuthorController authorController(AuthorService authorService) {
            return new AuthorController(authorService);
        }

        @Bean
        public AuthorService authorService() {
            return Mockito.mock(AuthorService.class);
        }
    }
}
