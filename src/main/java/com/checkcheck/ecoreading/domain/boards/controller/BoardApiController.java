package com.checkcheck.ecoreading.domain.boards.controller;

import com.checkcheck.ecoreading.config.S3Config;
import com.checkcheck.ecoreading.domain.boards.dto.BookDTO;
import com.checkcheck.ecoreading.domain.boards.dto.InsertBoardBookDTO;
import com.checkcheck.ecoreading.domain.boards.service.BoardService;
import com.checkcheck.ecoreading.domain.boards.service.BookService;
import com.checkcheck.ecoreading.domain.boards.service.S3Service;
import com.checkcheck.ecoreading.domain.books.dto.InsertBookRequest;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;
    private final BookService bookService;
    private final S3Service s3Service;

    // 등록 폼에서 input 가져오기
    @ResponseBody
    @PostMapping(value = "/board/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadBoard(@RequestParam("image") List<MultipartFile> multipartFiles,
                            InsertBoardBookDTO insertDTO) throws IOException {
        System.out.println(multipartFiles);
        System.out.println(insertDTO.toString());
//        if(multipartFiles==null) {
//            //예외 던지기
//        }
        List<String> imgPaths = s3Service.uploadImg(multipartFiles);
        System.out.println(imgPaths);
        //todo: db에 url 저장

        return null;
    }

    // 나눔글 등록시 책 검색하기
    @GetMapping("/board/bookSearch")
    public String search(@RequestParam String text, Model model) {
        List<BookDTO> books = bookService.searchBooks(text);
        System.out.println("검색결과: "+ books);
        model.addAttribute("books", books);
        return "/content/user/bookSearch";
    }

    // 나눔글 등록시 책 검색 결과 갖고오기
    @PostMapping("/board/bookSearch")
    public String fillBook(BookDTO bookDTO){
        System.out.println("북디티오: "+bookDTO);
        return "/content/user/boardAddForm";
    }
}
