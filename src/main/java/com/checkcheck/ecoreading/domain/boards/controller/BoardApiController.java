package com.checkcheck.ecoreading.domain.boards.controller;

import com.checkcheck.ecoreading.domain.boards.dto.BookDTO;
import com.checkcheck.ecoreading.domain.boards.service.BoardService;
import com.checkcheck.ecoreading.domain.boards.service.BookService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;
    private final BookService bookService;

    @PostMapping("/board/new")
    public String addImage(@RequestParam("image") MultipartFile multipartFile) throws IOException {
        System.out.println(multipartFile.toString());
        boardService.saveFile(multipartFile);

        //todo: db에 url 저장

        return null;
    }

    @GetMapping("/board/bookSearch")
    public String search(@RequestParam String text, Model model) {
        List<BookDTO> books = bookService.searchBooks(text);
        System.out.println("검색결과: "+ books);
        model.addAttribute("books", books);
        return "/content/user/bookSearch";
    }

    @PostMapping("/board/bookSearch")
    public String fillBook(BookDTO bookDTO){
        System.out.println("북디티오: "+bookDTO);
        return "/content/user/boardAddForm";
    }
}
