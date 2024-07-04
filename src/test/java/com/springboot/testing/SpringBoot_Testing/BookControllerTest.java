package com.springboot.testing.SpringBoot_Testing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
//@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();
    private ObjectWriter objectWriter = objectMapper.writer();

    @MockBean
    private BookRepository bookRepository;

    @InjectMocks
    private BookController bookController;

    Book RECORD_1 = new Book(1L, "Atomic Habits", "How to build better habits", 5);
    Book RECORD_2 = new Book(2L, "Thinking Fast and Slow", "How to create good mental models about thinking", 4);
    Book RECORD_3 = new Book(3L, "Grokking Algorithms", "Learn algorithms the fun way", 5);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void getAllRecords_success() throws Exception {
        List<Book> records = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));

        Mockito.when(bookRepository.findAll()).thenReturn(records);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/book")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].name", is("Grokking Algorithms")))
                .andExpect(jsonPath("$[1].name", is("Thinking Fast and Slow")));
    }

    @Test
    public void getBookById_Success() throws Exception {
        Mockito.when(bookRepository.findById(RECORD_1.getBookID())).thenReturn(java.util.Optional.of(RECORD_1));
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/book/1")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Atomic Habits")));
    }

    @Test
    public void createRecord_Success() throws Exception {

        Book record = Book.builder()
                .bookID(4L)
                .name("Introduction to C")
                .summary("The name but longer")
                .rating(5)
                .build();

        Mockito.when(bookRepository.save(record)).thenReturn(record);


        String content = objectWriter.writeValueAsString(record);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Introduction to C")));

    }

    @Test
    public void updateBookRecord_Success() throws Exception {

        Book update = Book.builder()
                .bookID(1L)
                .name("Life Is Hell")
                .rating(10)
                .summary("something something something ...").build();

        Mockito.when(bookRepository.findById(RECORD_1.getBookID())).thenReturn(java.util.Optional.of(RECORD_1));
        Mockito.when(bookRepository.save(update)).thenReturn(update);

        String content = objectWriter.writeValueAsString(update);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Life Is Hell")))
                .equals(update);

    }

    @Test
    void deleteBookById_Success() throws Exception {
        Mockito.when(bookRepository.findById(RECORD_2.getBookID())).thenReturn(Optional.of(RECORD_2));

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/book/2").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

}
