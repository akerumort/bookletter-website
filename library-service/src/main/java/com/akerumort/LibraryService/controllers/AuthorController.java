package com.akerumort.LibraryService.controllers;

import com.akerumort.LibraryService.dto.AuthorDTO;
import com.akerumort.LibraryService.dto.BookIdsDTO;
import com.akerumort.LibraryService.services.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
@Tag(name = "Authors", description = "Operations related to authors")
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    @Operation(summary = "Get all authors")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully received the list of authors"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        List<AuthorDTO> authors = authorService.getAllAuthors();
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an author by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully received the author"),
            @ApiResponse(responseCode = "404", description = "Author not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        AuthorDTO author = authorService.getAuthorById(id);
        return author != null ? ResponseEntity.ok(author) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create a new author")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully created the author"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody @Valid AuthorDTO authorDTO) {
        AuthorDTO createdAuthor = authorService.createAuthor(authorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing author")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated the author"),
            @ApiResponse(responseCode = "404", description = "Author not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id,
                                                  @RequestBody @Valid AuthorDTO authorDTO) {
        AuthorDTO updatedAuthor = authorService.updateAuthor(id, authorDTO);
        return updatedAuthor != null ? ResponseEntity.ok(updatedAuthor) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an author by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully deleted the author"),
            @ApiResponse(responseCode = "404", description = "Author not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(summary = "Delete all authors")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully deleted all authors"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteAllAuthors() {
        authorService.deleteAllAuthors();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/add-books")
    @Operation(summary = "Add books to an author")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully added books to the author"),
            @ApiResponse(responseCode = "404", description = "Author not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AuthorDTO> addBooksToAuthor(@PathVariable Long id,
                                                      @RequestBody @Valid BookIdsDTO bookIdsDTO) {
        AuthorDTO author = authorService.addBooksToAuthor(id, bookIdsDTO.getBookIds());
        return ResponseEntity.ok(author);
    }
}
