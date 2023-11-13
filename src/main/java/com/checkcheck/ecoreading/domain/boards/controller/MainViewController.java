package com.checkcheck.ecoreading.domain.boards.controller;

import com.checkcheck.ecoreading.domain.boards.service.BookService;
import com.checkcheck.ecoreading.domain.books.dto.BookMainDTO;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/main")
public class MainViewController {
    private final BookService bookService;

    // 메인 화면(나눔 글 전체 조회)
    @GetMapping("/")
    public String getBoards(@RequestParam(name = "search", required = false) String keyword, Model model){
        List<Books> books = bookService.findAll();
        List<BookMainDTO> bookDTOs = bookService.returnDTOs(books);  // DTOs로 변환

        model.addAttribute("Books", bookDTOs);

        return "content/user/main/";
    }

    // 검색 화면
    @GetMapping("/search")
    public String searchBooks(@RequestParam(name = "searchType", required = false, defaultValue = "search") String searchType,
                              @RequestParam(name = "search", required = false) String searchInput, Model model) {
        List<Books> books = bookService.searchBooks(searchType, searchInput);
        List<BookMainDTO> searchResults = bookService.returnDTOs(books);  // DTOs로 변환

        model.addAttribute("searchBooks", searchResults);

        return "content/board/searchBooks";
    }

}
