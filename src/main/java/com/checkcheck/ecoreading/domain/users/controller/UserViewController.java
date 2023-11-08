package com.checkcheck.ecoreading.domain.users.controller;


import com.checkcheck.ecoreading.domain.boards.entity.Boards;
import com.checkcheck.ecoreading.domain.boards.service.BookService;
import com.checkcheck.ecoreading.domain.books.dto.BookDTO;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
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
    @GetMapping("/")
    public String mainhtml(){
        return "/content/user/main";
    }

    @GetMapping("/mypage/myinfor")
    public String myInfor(){
        return "content/mypage/myInfor";
    }

//    @GetMapping("/mypage/givelist")
//    public String giveList(){
//        return "content/mypage/giveList";
//    }

    @GetMapping("/mypage/givelist")
    public String giveBoardList(Model model){
        Long userId = 1L;
        List<Boards> boards = bookService.giveList(userId);
        model.addAttribute("boards",boards);
        return "/content/mypage/giveList";
    }

    @GetMapping("/mypage/takelist")
    public String takeBoardList(Model model){
        Long userId = 1L;
        List<Books> books = bookService.takeList(userId);
        model.addAttribute("books",books);
        return "/content/mypage/takeList";
    }

}
