package com.checkcheck.ecoreading.domain.books.dto;


import com.checkcheck.ecoreading.domain.boards.entity.Boards;
import com.checkcheck.ecoreading.domain.images.entity.Images;
import com.checkcheck.ecoreading.domain.transactions.entity.Transactions;
import lombok.*;

import java.util.Date;
import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookMainDTO {
    private Long booksId;
    private Boards boards;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String pubDate;
    private String description;
    private String grade;
    private Transactions transactions;
    private List<Images> images;

}
