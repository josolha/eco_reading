package com.checkcheck.ecoreading.domain.users.controller;


import com.checkcheck.ecoreading.domain.boards.dto.BookDTO;
import com.checkcheck.ecoreading.domain.boards.service.BookService;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import com.checkcheck.ecoreading.domain.transactions.entity.Transactions;
import com.checkcheck.ecoreading.domain.transactions.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserViewController {
    private final BookService bookService;
    private final TransactionService transactionService;

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
//    @GetMapping("/")
//    public String mainhtml(){
//        return "/content/user/main";
//    }

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

    @GetMapping("/")
    public String getBooks(Model model){
        List<Books> books = bookService.findAll();
        model.addAttribute("Books", books);

        List<Transactions> transactions = transactionService.findAll();
        model.addAttribute("Transactions", transactions);

        return "content/user/main";
    }
}
