package com.akerumort.libraryservice.services;

import com.akerumort.libraryservice.dto.AuthorDTO;
import com.akerumort.libraryservice.entities.Author;
import com.akerumort.libraryservice.entities.Book;
import com.akerumort.libraryservice.exceptions.CustomException;
import com.akerumort.libraryservice.mappers.AuthorMapper;
import com.akerumort.libraryservice.repos.AuthorRepository;
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
public class AuthorService {
    private static final Logger logger = LogManager.getLogger(AuthorService.class);
    private final AuthorRepository authorRepository; // final для иммутабельности
    private final AuthorMapper authorMapper;
    private final BookRepository bookRepository;

    public List<AuthorDTO> getAllAuthors() {
        logger.info("Fetching all authors");
        return authorRepository.findAll().stream().map(authorMapper::toDTO).collect(Collectors.toList());
    }

    @Cacheable(value = "author", key = "#id")
    public AuthorDTO getAuthorById(Long id) {
        logger.info("Fetching author with ID: {}", id);
        return authorMapper.toDTO(authorRepository
                .findById(id)
                .orElse(null));
    }

    public Set<Author> getAuthorsByIds(Set<Long> ids) {
        logger.info("Fetching authors with IDs: {}", ids);
        return authorRepository.findAllById(ids).stream().collect(Collectors.toSet());
    }

    @Transactional
    @CachePut(value = "author", key = "#result.id")
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        logger.info("Creating new author...");

        Set<Long> bookIds = authorDTO.getBookIds();
        if (bookIds != null && !bookIds.isEmpty()) {
            Set<Book> books = bookRepository.findAllById(bookIds).stream().collect(Collectors.toSet());
            if (books.size() != bookIds.size()) {
                logger.error("One or more of the books listed do not exist.");
                throw new CustomException("One or more of the books listed do not exist.");
            }
            Author author = authorMapper.toEntity(authorDTO);
            author.setBooks(books);
            Author savedAuthor = authorRepository.save(author);
            logger.info("Author saved with ID: {}", savedAuthor.getId());
            return authorMapper.toDTO(savedAuthor);
        }

        Author author = authorMapper.toEntity(authorDTO);
        return authorMapper.toDTO(authorRepository.save(author));
    }

    @Transactional
    @CachePut(value = "author", key = "#id")
    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        logger.info("Updating author with ID: {}", id);

        if (!authorRepository.existsById(id)) {
            logger.error("Author with ID {} does not exist.", id);
            throw new CustomException("Author with ID " + id + " does not exist.");
        }

        Author existingAuthor = authorRepository.findById(id).orElseThrow(() ->
                new CustomException("Author with ID " + id + " does not exist."));

        existingAuthor.setFirstName(authorDTO.getFirstName());
        existingAuthor.setLastName(authorDTO.getLastName());
        existingAuthor.setCountry(authorDTO.getCountry());

        Set<Book> newBooks = new HashSet<>();
        if (authorDTO.getBookIds() != null && !authorDTO.getBookIds().isEmpty()) {
            newBooks = bookRepository.findAllById(authorDTO.getBookIds()).stream().collect(Collectors.toSet());
            if (newBooks.size() != authorDTO.getBookIds().size()) {
                logger.error("One or more of the books listed do not exist. " +
                                "Provided IDs: {}, Existing IDs: {}", authorDTO.getBookIds(),
                        newBooks.stream().map(Book::getId).collect(Collectors.toSet()));
                throw new CustomException("One or more of the books listed do not exist.");
            }
        }

        Set<Book> currentBooks = existingAuthor.getBooks();

        // удаляем старые книги
        for (Book book : currentBooks) {
            if (!newBooks.contains(book)) {
                book.getAuthors().remove(existingAuthor);
                bookRepository.save(book);
            }
        }

        existingAuthor.setBooks(newBooks);

        // добавляем новые книги к автору
        for (Book book : newBooks) {
            if (!book.getAuthors().contains(existingAuthor)) {
                book.getAuthors().add(existingAuthor);
                bookRepository.save(book);
            }
        }

        Author updatedAuthor = authorRepository.save(existingAuthor);
        logger.info("Author updated successfully with ID: {}", updatedAuthor.getId());
        return authorMapper.toDTO(updatedAuthor);
    }

    @Transactional
    @CacheEvict(value = "author", key = "#id")
    public void deleteAuthor(Long id) {
        logger.info("Deleting author with ID: {}", id);
        try {
            if (authorRepository.existsById(id)) {
                authorRepository.deleteById(id);
            }
        } catch (EmptyResultDataAccessException e) {
            logger.error("Author with ID " + id + " does not exist.");
            throw new CustomException("Author with ID " + id + " does not exist.");
        }
    }

    @Transactional
    @CacheEvict(value = "author", allEntries = true)
    public void deleteAllAuthors() {
        logger.info("Deleting all authors");
        authorRepository.deleteAll();
    }

    @Transactional
    // добавить cacheput
    public void saveAuthor(Author author) {
        logger.info("Saving author: {}", author);
        authorRepository.save(author);
    }

    @Transactional
    @CachePut(value = "author", key = "#authorId")
    public AuthorDTO addBooksToAuthor(Long authorId, Set<Long> bookIds) {
        logger.info("Adding books to author with ID: {}", authorId);
        Author author = authorRepository.findById(authorId).orElseThrow(()->
                new CustomException("Author with ID " + authorId + " does not exist."));
        Set<Book> books = bookRepository.findAllById(bookIds).stream().collect(Collectors.toSet());

        if (books.size() != bookIds.size()) {
            logger.error("One or more of the books listed do not exist.");
            throw new CustomException("One or more of the books listed do not exist.");
        }

        author.getBooks().addAll(books);
        books.forEach(book -> book.getAuthors().add(author));

        authorRepository.save(author);
        bookRepository.saveAll(books);

        return authorMapper.toDTO(author);
    }
}
