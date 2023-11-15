package com.checkcheck.ecoreading.domain.users.controller;



import com.checkcheck.ecoreading.domain.boards.service.BookService;
import com.checkcheck.ecoreading.domain.books.dto.BookMainDTO;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import com.checkcheck.ecoreading.domain.boards.entity.Boards;
import com.checkcheck.ecoreading.domain.boards.service.BookService;
import com.checkcheck.ecoreading.domain.books.dto.BookDTO;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import com.checkcheck.ecoreading.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserViewController {
    private final BookService bookService;

    private final UserService userService;


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
    @GetMapping("/403error")
    public String errorAccessDenied(){
        return "error/403error";
    }

    @GetMapping("/mypage/myinfor")
    public String myInformation(Model model){
        Long userId = 1L;
        Users user = userService.findAllById(userId);
        System.out.println(user.getPointHistoryList());
        model.addAttribute("user", user);
        return "content/mypage/myInfor";
    }


    @GetMapping("/mypage/givelist")
    public String giveBoardList(Model model){
        Users users = new Users();
        users.setUsersId(1L);
        List<Boards> boards = bookService.giveList(users);
        model.addAttribute("boards",boards);
        return "/content/mypage/giveList";
    }

    @GetMapping("/mypage/takelist")
    public String takeBoardList(Model model){
        Long takerId = 1L;
        List<Books> books = bookService.takeList(takerId);
        model.addAttribute("books",books);
        return "/content/mypage/takeList";
    }
}
