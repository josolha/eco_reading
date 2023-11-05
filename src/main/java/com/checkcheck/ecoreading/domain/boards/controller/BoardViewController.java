package com.checkcheck.ecoreading.domain.boards.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardViewController {
    @GetMapping("/new")
    public String addBoard() {
        return "/content/user/boardAddForm";
    }

    @GetMapping("/detail")
    public String mainhtml(){
        return "/content/board/detail";
    }
}
