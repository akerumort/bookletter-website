package com.akerumort.libraryservice.controllers;

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
        author.setFirstName("John");
        author.setLastName("Doe");
        author.setCountry("USA");

        when(authorService.getAuthorById(1L)).thenReturn(author);

        ResponseEntity<AuthorDTO> response = authorController.getAuthorById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(author.getId(), response.getBody().getId());
        assertEquals(author.getFirstName(), response.getBody().getFirstName());
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
        AuthorDTO newAuthor = new AuthorDTO();
        newAuthor.setFirstName("Alice");
        newAuthor.setLastName("Smith");
        newAuthor.setCountry("Canada");

        AuthorDTO savedAuthor = new AuthorDTO();
        savedAuthor.setId(2L);
        savedAuthor.setFirstName("Alice");
        savedAuthor.setLastName("Smith");
        savedAuthor.setCountry("Canada");

        when(authorService.createAuthor(Mockito.any(AuthorDTO.class))).thenReturn(savedAuthor);

        ResponseEntity<AuthorDTO> response = authorController.createAuthor(newAuthor);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(savedAuthor.getId(), response.getBody().getId());
    }

    @Test
    public void testUpdateAuthor_validId() {
        AuthorDTO updatedAuthor = new AuthorDTO();
        updatedAuthor.setId(1L);
        updatedAuthor.setFirstName("John");
        updatedAuthor.setLastName("Updated");
        updatedAuthor.setCountry("USA");

        when(authorService.updateAuthor(eq(1L), Mockito.any(AuthorDTO.class))).thenReturn(updatedAuthor);

        ResponseEntity<AuthorDTO> response = authorController.updateAuthor(1L, updatedAuthor);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updatedAuthor.getLastName(), response.getBody().getLastName());
    }

    @Test
    public void testUpdateAuthor_invalidId() {
        when(authorService.updateAuthor(eq(999L), Mockito.any(AuthorDTO.class))).thenReturn(null);

        AuthorDTO updatedAuthor = new AuthorDTO();
        updatedAuthor.setId(999L);
        updatedAuthor.setFirstName("NotExist");
        updatedAuthor.setLastName("Invalid");
        updatedAuthor.setCountry("Unknown");

        ResponseEntity<AuthorDTO> response = authorController.updateAuthor(999L, updatedAuthor);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteAuthor_validId() {
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
