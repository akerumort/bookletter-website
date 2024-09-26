package com.akerumort.libraryservice.services;

import com.akerumort.libraryservice.dto.BookDTO;
import com.akerumort.libraryservice.entities.Author;
import com.akerumort.libraryservice.entities.Book;
import com.akerumort.libraryservice.exceptions.CustomException;
import com.akerumort.libraryservice.mappers.BookMapper;
import com.akerumort.libraryservice.repos.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private static final Logger logger = LogManager.getLogger(BookService.class);
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthorService authorService;

    @Cacheable(value = "books")
    public List<BookDTO> getAllBooks() {
        logger.info("Fetching all books");
        return bookRepository.findAll().stream().map(bookMapper::toDTO).collect(Collectors.toList());
    }

    @Cacheable(value = "book", key = "#id")
    public BookDTO getBookById(Long id) {
        logger.info("Fetching book with ID: {}", id);
        return bookMapper.toDTO(bookRepository.findById(id).orElse(null));
    }

    @Transactional
    @CachePut(value = "book", key = "#result.id")
    @CacheEvict(value = "books", allEntries = true)
    public BookDTO createBook(BookDTO bookDTO) {
        logger.info("Creating new book...");

        if (bookDTO.getAuthorIds() == null || bookDTO.getAuthorIds().isEmpty()) {
            logger.error("A book must have at least one author.");
            throw new CustomException("A book must have at least one author.");
        }

        Set<Author> authors = authorService.getAuthorsByIds(bookDTO.getAuthorIds().stream().collect(Collectors.toSet()));

        if (authors.size() != bookDTO.getAuthorIds().size()) {
            logger.error("One or more of the authors listed do not exist. " +
                            "Provided IDs: {}, Existing IDs: {}", bookDTO.getAuthorIds(),
                    authors.stream().map(Author::getId).collect(Collectors.toSet()));
            throw new CustomException("One or more of the authors listed do not exist.");
        }

        Book book = bookMapper.toEntity(bookDTO);
        book.setAuthors(authors);
        Book savedBook = bookRepository.save(book);

        for (Author author : savedBook.getAuthors()) {
            author.getBooks().add(savedBook);
            authorService.saveAuthor(author);
        }

        logger.info("Book created successfully with ID: {}", savedBook.getId());
        return bookMapper.toDTO(savedBook);
    }

    @Transactional
    @CachePut(value = "book", key = "#id")
    @CacheEvict(value = "books", allEntries = true)
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        logger.info("Updating book with ID: {}", id);

        if (!bookRepository.existsById(id)) {
            logger.error("Book with ID {} does not exist.", id);
            throw new CustomException("Book with ID " + id + " does not exist.");
        }

        Book existingBook = bookRepository.findById(id).orElseThrow(() ->
                new CustomException("Book with ID " + id + " does not exist."));

        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setGenre(bookDTO.getGenre());
        existingBook.setPublicationYear(bookDTO.getPublicationYear());

        Set<Author> newAuthors = new HashSet<>();
        if (bookDTO.getAuthorIds() != null && !bookDTO.getAuthorIds().isEmpty()) {
            newAuthors = authorService.getAuthorsByIds(bookDTO.getAuthorIds()
                    .stream().collect(Collectors.toSet()));
            if (newAuthors.size() != bookDTO.getAuthorIds().size()) {
                logger.error("One or more of the authors listed do not exist. " +
                                "Provided IDs: {}, Existing IDs: {}", bookDTO.getAuthorIds(),
                        newAuthors.stream().map(Author::getId).collect(Collectors.toSet()));
                throw new CustomException("One or more of the authors listed do not exist.");
            }
        }

        Set<Author> currentAuthors = existingBook.getAuthors();

        // удаляем книгу у старых авторов
        for (Author author : currentAuthors) {
            if (!newAuthors.contains(author)) {
                author.getBooks().remove(existingBook);
                authorService.saveAuthor(author);
            }
        }

        existingBook.setAuthors(newAuthors);

        // добавляем книгу к новым авторам
        for (Author author : newAuthors) {
            if (!author.getBooks().contains(existingBook)) {
                author.getBooks().add(existingBook);
                authorService.saveAuthor(author);
            }
        }

        Book updatedBook = bookRepository.save(existingBook);
        logger.info("Book updated successfully with ID: {}", updatedBook.getId());
        return bookMapper.toDTO(updatedBook);
    }

    @Transactional
    @CacheEvict(value = "book", key = "#id")
    public void deleteBook(Long id) {
        logger.info("Deleting book with ID: {}", id);
        try {
            if (bookRepository.existsById(id)) {
                bookRepository.deleteById(id);
            }
        } catch (EmptyResultDataAccessException e) {
            logger.error("Book with ID {} does not exist.", id);
            throw new CustomException("Book with ID " + id + " does not exist.");
        }
    }

    @Transactional
    @CacheEvict(value = "books", allEntries = true)
    public void deleteAllBooks() {
        logger.info("Deleting all books...");
        bookRepository.deleteAll();
        logger.info("All books deleted successfully.");
    }
}
