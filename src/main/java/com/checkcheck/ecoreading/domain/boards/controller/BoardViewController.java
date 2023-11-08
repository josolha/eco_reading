package com.checkcheck.ecoreading.domain.boards.controller;

import com.checkcheck.ecoreading.domain.boards.dto.BookDTO;
import com.checkcheck.ecoreading.domain.boards.service.BookService;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardViewController {

    private final BookService bookService;

    @GetMapping("/new")
    public String addBoard() {
        return "/content/user/boardAddForm";
    }

    // 나눔 글 상세
    @GetMapping("/detail/{book_id}")
    public String boardDetail(@PathVariable Long book_id, Model model) {
        Books books = bookService.findBoardByBookId(book_id);

        if (books == null) {
            return "redirect:/error";
        }

        model.addAttribute("book", books);

        return "/content/board/detail";
    }
}
