package com.checkcheck.ecoreading.domain.boards.dto;

import com.checkcheck.ecoreading.domain.books.entity.BookProcessingMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@Setter
@AllArgsConstructor
public class InsertBookDTO {
    private String isbn;
    private String title;
    private String pubdate;
    private String publisher;
    private String author;
    private String description;
    private BookProcessingMethod process;

}
