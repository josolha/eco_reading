package com.checkcheck.ecoreading.domain.boards.controller;

import com.checkcheck.ecoreading.domain.boards.service.BookService;
import com.checkcheck.ecoreading.domain.books.dto.BookMainDTO;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import com.checkcheck.ecoreading.domain.transactions.entity.TransactionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    // 메인 화면(상태 나눔중 인 나눔 글만 전체 조회) 및 페이징
    @GetMapping("/")
    public String getBoards(@RequestParam(name = "search", required = false) String keyword,
                            @RequestParam(name = "page", defaultValue = "0") int page,
                            @RequestParam(name = "size", defaultValue = "9") int size,
                            Model model) {
        Page<Books> bookPage = bookService.findAllBooksByStatus(TransactionStatus.나눔중, PageRequest.of(page, size));
        List<BookMainDTO> bookDTOs = bookService.returnDTOs(bookPage.getContent());

        model.addAttribute("bookPage", bookPage);
        model.addAttribute("Books", bookDTOs);

        return "content/user/main";
    }

    // 검색 화면
//    @GetMapping("/search")
//    public String searchBooks(@RequestParam(name = "searchType", required = false, defaultValue = "search") String searchType,
//                              @RequestParam(name = "search", required = false) String searchInput, Model model) {
//        List<Books> books = bookService.searchBooks(searchType, searchInput);
//        List<BookMainDTO> searchResults = bookService.returnDTOs(books);  // DTOs로 변환
//
//        model.addAttribute("searchBooks", searchResults);
//
//        return "content/board/searchBooks";
//    }

    // 다음 페이지를 처리할 메소드 추가
//    @GetMapping("/nextPage")
//    public String nextPage(@RequestParam(name = "page", defaultValue = "1") int page,
//                           Model model) {
//        Page<Books> bookPage = bookService.findPagedBooks(PageRequest.of(page, 9));
//        model.addAttribute("bookPage", bookPage);
//
//        return "content/user/main";
//    }

    // 검색 화면 및 페이징
    @GetMapping("/search")
    public String searchBooks(
            @RequestParam(name = "searchType", required = false, defaultValue = "search") String searchType,
            @RequestParam(name = "search", required = false) String searchInput,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {

        Page<Books> booksPage = bookService.searchBooks(TransactionStatus.나눔중, searchType, searchInput, page, size);
        List<BookMainDTO> searchBookDTOs = bookService.returnDTOs(booksPage.getContent());

        model.addAttribute("searchType", searchType);
        model.addAttribute("searchInput", searchInput);
        model.addAttribute("bookPage", booksPage);
        model.addAttribute("searchBooks", searchBookDTOs);

        return "content/board/searchBooks";
    }

    // 검색 화면 및 페이징 다음 페이지를 처리할 메소드 추가
//    @GetMapping("/search/nextPage")
//    public String searchNextPage(
//            @RequestParam(name = "searchType", required = false, defaultValue = "search") String searchType,
//            @RequestParam(name = "search", required = false) String searchInput,
//            @RequestParam(name = "page", defaultValue = "1") int page,
//            @RequestParam(name = "size", defaultValue = "10") int size,
//            Model model) {
//
//        Page<Books> booksPage = bookService.searchBooks(searchType, searchInput, page, size);
//        List<BookMainDTO> searchResults = bookService.returnDTOs(booksPage.getContent());
//
//        model.addAttribute("searchType", searchType);
//        model.addAttribute("searchInput", searchInput);
//        model.addAttribute("bookPage", booksPage);
//        model.addAttribute("searchBooks", searchResults);
//
//        return "content/board/searchBooks";
//    }

}
