package com.checkcheck.ecoreading.domain.boards.controller;

import com.checkcheck.ecoreading.domain.boards.service.BoardService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;
    @PostMapping("/board/add")
    public String addImage(@RequestParam("image") MultipartFile multipartFile) throws IOException {
        System.out.println(multipartFile.toString());
        boardService.saveFile(multipartFile);

        //todo: db에 url 저장

        return null;
    }
}
