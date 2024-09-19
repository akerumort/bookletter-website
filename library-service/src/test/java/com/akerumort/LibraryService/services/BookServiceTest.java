package com.akerumort.LibraryService.services;

import com.akerumort.LibraryService.dto.BookDTO;
import com.akerumort.LibraryService.entities.Author;
import com.akerumort.LibraryService.entities.Book;
import com.akerumort.LibraryService.mappers.BookMapper;
import com.akerumort.LibraryService.repos.BookRepository;
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
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Java. The Complete Reference");
        book.setGenre("Programming");
        book.setPublicationYear(2023);

        bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Java. The Complete Reference");
        bookDTO.setGenre("Programming");
        bookDTO.setPublicationYear(2023);
    }

    @Test
    public void testGetAllBooks() {
        List<Book> books = Collections.singletonList(book);
        when(bookRepository.findAll()).thenReturn(books);
        when(bookMapper.toDTO(any(Book.class))).thenReturn(bookDTO);

        List<BookDTO> bookDTOS = bookService.getAllBooks();

        assertEquals(1, bookDTOS.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void testGetBookById() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookMapper.toDTO(any(Book.class))).thenReturn(bookDTO);

        BookDTO result = bookService.getBookById(1L);

        assertNotNull(result);
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateBook() {
        Set<Long> authorIds = new HashSet<>(Collections.singletonList(1L));
        bookDTO.setAuthorIds(authorIds);

        Author author = new Author();
        author.setId(1L);
        Set<Author> authors = new HashSet<>(Collections.singletonList(author));
        book.setAuthors(authors);

        when(authorService.getAuthorsByIds(anySet())).thenReturn(authors);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toEntity(any(BookDTO.class))).thenReturn(book);
        when(bookMapper.toDTO(any(Book.class))).thenReturn(bookDTO);

        BookDTO result = bookService.createBook(bookDTO);

        assertNotNull(result);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void testUpdateBook() {
        Set<Long> authorIds = new HashSet<>(Collections.singletonList(1L));
        bookDTO.setAuthorIds(authorIds);

        Author author = new Author();
        author.setId(1L);
        Set<Author> authors = new HashSet<>(Collections.singletonList(author));
        book.setAuthors(authors);

        when(bookRepository.existsById(anyLong())).thenReturn(true); // обязательно добавлять
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(authorService.getAuthorsByIds(anySet())).thenReturn(authors);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toDTO(any(Book.class))).thenReturn(bookDTO);

        BookDTO result = bookService.updateBook(1L, bookDTO);

        assertNotNull(result, "Expected result to be not null");
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void testDeleteBook() {
        when(bookRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(bookRepository).deleteById(anyLong());

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }
}
