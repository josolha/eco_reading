package com.checkcheck.ecoreading.domain.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class AdminViewController {
    @GetMapping("/admin")
    public String admin(){
        return "/content/admin/main";
    }
    @GetMapping("/admin/board")
    public String board(){
        return "/content/admin/board";
    }
}
