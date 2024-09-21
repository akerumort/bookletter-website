package com.akerumort.libraryservice.mappers;

import com.akerumort.libraryservice.dto.AuthorDTO;
import com.akerumort.libraryservice.entities.Author;
import com.akerumort.libraryservice.entities.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class AuthorMapper {

    @Mapping(target = "bookIds", source = "books")
    public abstract AuthorDTO toDTO(Author author);

    @Mapping(target = "books", source = "bookIds")
    public abstract Author toEntity(AuthorDTO authorDTO);

    public Set<Long> mapBooksToIds(Set<Book> books) {
        return books.stream().map(Book::getId).collect(Collectors.toSet());
    }

    public Set<Book> mapIdsToBooks(Set<Long> bookIds) {
        return bookIds.stream().map(id -> {
            Book book = new Book();
            book.setId(id);
            return book;
        }).collect(Collectors.toSet());
    }
}