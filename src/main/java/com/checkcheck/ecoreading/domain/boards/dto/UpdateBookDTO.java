package com.checkcheck.ecoreading.domain.boards.dto;

import com.checkcheck.ecoreading.domain.books.entity.BookProcessingMethod;
import lombok.*;

@Getter
@ToString
@Setter
@NoArgsConstructor
public class UpdateBookDTO {
    private String isbn;
    private String title;
    private String pubdate;
    private String publisher;
    private String author;
    private String description;
    private BookProcessingMethod process;

}
