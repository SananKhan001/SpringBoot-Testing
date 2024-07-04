package com.springboot.testing.SpringBoot_Testing;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.swing.text.html.Option;
import java.util.Optional;

@RestController
@RequestMapping(value = "/book")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    // http://localhost:8080/book
    @GetMapping
    public ResponseEntity getAllBookRecords() {
        return ResponseEntity.status(HttpStatus.OK).body(bookRepository.findAll());
    }

    // http://localhost:8080/book/{bookId}
    @GetMapping(value = "{bookID}")
    public ResponseEntity getBookByID(@PathVariable(value = "bookID") Long bookID) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookRepository.findById(bookID).get());
    }

    // http://localhost:8080/book
    @PostMapping
    public ResponseEntity createBookRecord(@RequestBody @Valid Book book){
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookRepository.save(book));
    }

    // http://localhost:8080/book
    @PutMapping
    public ResponseEntity updateBookRecord(@RequestBody @Valid Book book){
        if(book == null || book.getBookID() == null){
            throw new EntityNotFoundException("Book Record or ID must not be null");
        }
        Optional<Book> optionalBook = bookRepository.findById(book.getBookID());
        if(!optionalBook.isPresent()){
            throw new EntityNotFoundException("Book is not present in the DB");
        }
        Book existingBook = optionalBook.get();
        existingBook.setName(book.getName());
        existingBook.setSummary(book.getSummary());
        existingBook.setRating(book.getRating());

        return ResponseEntity.status(HttpStatus.OK).body(bookRepository.save(existingBook));
    }

    // TODO: write /delete endpoint using TDO method
    @DeleteMapping(value = "{bookID}")
    public void deleteBookById(@PathVariable(value = "bookID") Long bookID) throws EntityNotFoundException{
        Optional<Book> book = bookRepository.findById(bookID);
        if(!book.isPresent()) throw new EntityNotFoundException("Book not found !!!");
        bookRepository.deleteById(bookID);
        return ;
    }
}