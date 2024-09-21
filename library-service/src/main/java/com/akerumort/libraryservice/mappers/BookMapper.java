package com.akerumort.libraryservice.mappers;

import com.akerumort.libraryservice.dto.BookDTO;
import com.akerumort.libraryservice.entities.Author;
import com.akerumort.libraryservice.entities.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class BookMapper {

    @Mapping(target = "authorIds", source = "authors")
    public abstract BookDTO toDTO(Book book);

    @Mapping(target = "authors", source = "authorIds")
    public abstract Book toEntity(BookDTO bookDTO);

    public Set<Long> mapAuthorsToIds(Set<Author> authors) {
        return authors.stream().map(Author::getId).collect(Collectors.toSet());
    }

    public Set<Author> mapIdsToAuthors(Set<Long> authorIds) {
        return authorIds.stream().map(id -> {
            Author author = new Author();
            author.setId(id);
            return author;
        }).collect(Collectors.toSet());
    }
}
