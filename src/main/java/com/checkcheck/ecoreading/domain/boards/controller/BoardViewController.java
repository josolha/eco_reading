package com.checkcheck.ecoreading.domain.boards.controller;

import com.checkcheck.ecoreading.domain.boards.service.BookService;
import com.checkcheck.ecoreading.domain.books.dto.BookMainDTO;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import com.checkcheck.ecoreading.domain.delivery.dto.DeliveryDTO;
import com.checkcheck.ecoreading.domain.delivery.entity.Delivery;
import com.checkcheck.ecoreading.domain.delivery.repository.DeliveryRepository;
import com.checkcheck.ecoreading.domain.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardViewController {

    private final BookService bookService;
    private final DeliveryService deliveryService;

    @GetMapping("/new")
    public String addBoard() {
        return "/content/user/boardAddForm";
    }

    // 나눔 글 상세
    @GetMapping("/detail/{booksId}")
    public String boardDetail(@PathVariable Long booksId, Model model) {
        Books books = bookService.findBoardByBookId(booksId);
        BookMainDTO booksDTO = bookService.convertToDTO(books);  // DTO로 변환

        if (booksDTO == null) {
            return "redirect:/error";
        }

        model.addAttribute("book", booksDTO);

        return "/content/board/boardDetail";
    }

    // 나눔받기
    @GetMapping("/detail/{booksId}/taker")
    public String takeBook(@PathVariable Long booksId, Model model) {
        Books books = bookService.findBoardByBookId(booksId);
        BookMainDTO booksDTO = bookService.convertToDTO(books);  // DTO로 변환

        if (booksDTO == null) {
            return "redirect:/error";
        }
        model.addAttribute("book", booksDTO);

        DeliveryDTO deliveryDTO = deliveryService.convertToDeliveryDTO(new Delivery());
        model.addAttribute("delivery", deliveryDTO);

        return "/content/board/takeBook";
    }

    // 나눔받기 완료
//    @PostMapping("/detail/complete")
//    public String completeTakeBook() {
//
//        return "/content/board/completeTakeBook";
//    }

}
