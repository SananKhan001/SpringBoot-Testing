package com.springboot.testing.SpringBoot_Testing;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "book_record")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bookID;

    @NonNull
    private String name;

    @NonNull
    private String summary;

    private int rating;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return rating == book.rating &&
                Objects.equals(bookID, book.bookID) &&
                Objects.equals(name, book.name) &&
                Objects.equals(summary, book.summary);
    }
}
