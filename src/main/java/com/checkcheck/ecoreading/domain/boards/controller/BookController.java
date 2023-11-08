//package com.checkcheck.ecoreading.domain.boards.controller;
//
//import com.checkcheck.ecoreading.domain.books.dto.BookDTO;
//import com.checkcheck.ecoreading.domain.boards.dto.NaverResultDTO;
//import com.checkcheck.ecoreading.domain.boards.service.BookService;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.RequestEntity;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.net.URI;
//import java.util.List;
//
//@Controller
//@RequiredArgsConstructor
//public class BookController {
//    private final BookService bookService;
//
//    @GetMapping("/board/bookSearch")
//    public String search(@RequestParam String text, Model model) {
//        List<BookDTO> books = bookService.searchBooks(text);
//        System.out.println(books);
//        model.addAttribute("books", books);
//
//        return "/content/user/bookSearch";
//    }
//
//    @PostMapping("/board/new")
//    public String fillBook(@RequestBody BookDTO bookDTO){
//        System.out.println(bookDTO);
//        return "/content/user/boardAddForm";
//    }
//}