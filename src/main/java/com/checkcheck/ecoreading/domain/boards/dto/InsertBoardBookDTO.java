package com.checkcheck.ecoreading.domain.boards.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
@AllArgsConstructor
public class InsertBoardBookDTO {
    private String isbn;
    private String title;
    private String pubdate;
    private String publisher;
    private String author;
    private String description;
    private String message;

}
