package com.akerumort.libraryservice.services;

import com.akerumort.libraryservice.dto.AuthorDTO;
import com.akerumort.libraryservice.entities.Author;
import com.akerumort.libraryservice.entities.Book;
import com.akerumort.libraryservice.mappers.AuthorMapper;
import com.akerumort.libraryservice.repos.AuthorRepository;
import com.akerumort.libraryservice.repos.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapper authorMapper;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private AuthorService authorService;

    private Author author;
    private AuthorDTO authorDTO;

    @BeforeEach
    void setUp() {
        author = new Author();
        author.setId(1L);
        author.setFirstName("Ivan");
        author.setLastName("Ivanov");
        author.setCountry("Russia");

        authorDTO = new AuthorDTO();
        authorDTO.setId(1L);
        authorDTO.setFirstName("Ivan");
        authorDTO.setLastName("Ivanov");
        authorDTO.setCountry("Russia");
    }

    @Test
    public void testGetAllAuthors() {
        List<Author> authors = Collections.singletonList(author);
        when(authorRepository.findAll()).thenReturn(authors);
        when(authorMapper.toDTO(any(Author.class))).thenReturn(authorDTO);

        List<AuthorDTO> authorDTOS = authorService.getAllAuthors();

        assertEquals(1, authorDTOS.size());
        verify(authorRepository, times(1)).findAll();
    }

    @Test
    public void testGetAuthorById() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(author));
        when(authorMapper.toDTO(any(Author.class))).thenReturn(authorDTO);

        AuthorDTO result = authorService.getAuthorById(1L);

        assertNotNull(result);
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateAuthor() {
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        when(authorMapper.toEntity(any(AuthorDTO.class))).thenReturn(author);
        when(authorMapper.toDTO(any(Author.class))).thenReturn(authorDTO);

        AuthorDTO result = authorService.createAuthor(authorDTO);

        assertNotNull(result);
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    public void testUpdateAuthor() {
        Set<Long> bookIds = new HashSet<>(Collections.singletonList(1L));
        authorDTO.setBookIds(bookIds);

        Book book = new Book();
        book.setId(1L);
        Set<Book> books = new HashSet<>(Collections.singletonList(book));
        author.setBooks(books);

        when(authorRepository.existsById(anyLong())).thenReturn(true); // проверка на существование id
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(author));
        when(bookRepository.findAllById(anySet())).thenReturn(new ArrayList<>(books));
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        when(authorMapper.toDTO(any(Author.class))).thenReturn(authorDTO);

        AuthorDTO result = authorService.updateAuthor(1L, authorDTO);

        assertNotNull(result);
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    public void testDeleteAuthor() {
        when(authorRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(authorRepository).deleteById(anyLong());

        authorService.deleteAuthor(1L);

        verify(authorRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testAddBooksToAuthor() {
        Book book = new Book();
        book.setId(1L);
        Set<Book> books = new HashSet<>(Collections.singletonList(book));
        author.setBooks(books);

        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(author));
        when(bookRepository.findAllById(anySet())).thenReturn(new ArrayList<>(books));
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        when(authorMapper.toDTO(any(Author.class))).thenReturn(authorDTO);

        AuthorDTO result = authorService.addBooksToAuthor(1L, new HashSet<>(Collections.singletonList(1L)));

        assertNotNull(result);
        verify(authorRepository, times(1)).save(any(Author.class));
    }
}
