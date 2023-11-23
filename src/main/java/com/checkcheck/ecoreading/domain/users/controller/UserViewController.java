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
import com.checkcheck.ecoreading.security.jwt.JwtTokenProvider;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/login")
    public String login(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            // 사용자가 이미 로그인된 경우
            return "redirect:/main/";
        }
        return "login";
    }

    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }
    @GetMapping("/403error")
    public String errorAccessDenied(){
        System.out.println("권한페이지 이동합니다.");
        return "error/403error";
    }
    @GetMapping("/kakao/signup")
    public String signupKakao() {
    return "signupKakao";
    }
    @GetMapping("/find/idPw")
    public String findIdPw(){
        return "findIdPw";
    }
}
