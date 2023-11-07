package com.checkcheck.ecoreading.domain.users.controller;


import com.checkcheck.ecoreading.domain.boards.dto.BookDTO;
import com.checkcheck.ecoreading.domain.boards.service.BookService;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserViewController {
    private final BookService bookService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }
    @GetMapping("/test")
    public String showTestPage() {
        return "test";
    }

    @GetMapping("/mypage/myInfor")
    public String myInfor(){
        return "content/mypage/myInfor";
    }

    @GetMapping("/mypage/giveList")
    public String giveList(){
        return "content/mypage/giveList";
    }

    @GetMapping("/mypage/takeList")
    public String takeList(){
        return "content/mypage/takeList";
    }

    // 메인 화면(나눔 글 전체 조회)
    @GetMapping("/")
    public String getBoards(Model model){
        List<Books> books = bookService.findAll(); // Book 엔티티를 가져옴
        List<BookDTO> bookDTOs = new ArrayList<>();

        for (Books book : books) {
            bookDTOs.add(bookService.convertToDTO(book)); // 엔티티를 DTO로 변환
        }

        model.addAttribute("Books", bookDTOs); // BookDTO 리스트를 모델에 추가

        return "content/user/main";
    }
}
