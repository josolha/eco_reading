package com.checkcheck.ecoreading.domain.users.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class UserViewController {

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

    @GetMapping("/mypage/myInfor")
    public String myInfor(){
        return "content/mypage/myInfor";
    }

    @GetMapping("/mypage/giveList")
    public String giveList(){
        return "content/mypage/giveList";
    }
}
