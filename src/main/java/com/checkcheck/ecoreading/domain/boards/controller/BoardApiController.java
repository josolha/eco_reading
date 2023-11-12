package com.checkcheck.ecoreading.domain.boards.controller;


import com.checkcheck.ecoreading.domain.boards.dto.InsertBoardDTO;
import com.checkcheck.ecoreading.domain.boards.dto.InsertBookDTO;
import com.checkcheck.ecoreading.domain.boards.dto.InsertDeliveryDTO;
import com.checkcheck.ecoreading.domain.boards.service.BoardService;
import com.checkcheck.ecoreading.domain.boards.service.BookService;
import com.checkcheck.ecoreading.domain.boards.service.S3Service;
import com.checkcheck.ecoreading.domain.books.dto.NaverBookDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;
    private final BookService bookService;

    // 나눔글 등록 폼에서 input 가져와서 DB에 업로드
    @PostMapping(value = "/board/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadBoard(@RequestParam("image") List<MultipartFile> multipartFiles,
                              InsertBookDTO bookDTO, InsertBoardDTO boardDTO, InsertDeliveryDTO deliveryDTO) {
        // 등록 폼의 전체 데이터 업로드하기
        boardService.uploadBoard(multipartFiles, bookDTO, boardDTO, deliveryDTO);

        return "redirect:/user";
    }

    // 나눔글 등록시 책 검색하기
    @GetMapping("/board/bookSearch")
    public String search(@RequestParam String text, Model model) {
        List<NaverBookDTO> books = bookService.searchBooks(text);
        System.out.println("검색결과: "+ books);
        model.addAttribute("books", books);
        return "/content/user/bookSearch";
    }

    // 나눔글 등록시 책 검색 결과 갖고오기
    @PostMapping("/board/bookSearch")
    public String fillBook(NaverBookDTO naverBookDTO){
        System.out.println("북디티오: "+ naverBookDTO);
        return "/content/user/boardAddForm";
    }
}
