package com.checkcheck.ecoreading.domain.boards.dto;

import com.checkcheck.ecoreading.domain.books.dto.BookDTO;
import lombok.*;

import java.util.List;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NaverResultDTO {
    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<BookDTO> items;

}
