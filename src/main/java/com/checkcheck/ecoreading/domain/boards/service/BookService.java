package com.checkcheck.ecoreading.domain.boards.service;

import com.checkcheck.ecoreading.domain.boards.dto.BookDTO;
import com.checkcheck.ecoreading.domain.boards.dto.NaverResultDTO;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import com.checkcheck.ecoreading.domain.books.repository.BookRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

// API 활용해 책 정보 검색 기능 구현
@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Value("${naver-property.clientId}")
    private String naverClientId;

    @Value("${naver-property.clientSecret}")
    private String naverClientSecret;

    //네이버 검색 API 요청
    public List<BookDTO> searchBooks (String text) {
        // JSON 결과
        // String apiURL = "https://openapi.naver.com/v1/search/book.json?query="+ text;
        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/book.json")
                .queryParam("query", text)
                .queryParam("display", 10)
                .queryParam("start", 1)
                .queryParam("sort", "sim")
                .encode()
                .build()
                .toUri();

//        log.info("uri: "+uri);

        // Spring 요청 제공 클래스  (Request)
        RequestEntity<Void> request = RequestEntity.get(uri)
                .header("X-Naver-Client-Id", naverClientId)
                .header("X-Naver-Client-Secret", naverClientSecret)
                .build();

        // Spring 제공 restTemplate (Response)
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // JSON 파싱 (JSON 문자열을 객체로 만듦, 문서화하기)
        ObjectMapper om = new ObjectMapper();
        NaverResultDTO resultDTO = null;

        try {
            resultDTO = om.readValue(response.getBody(), NaverResultDTO.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<BookDTO> books = resultDTO.getItems();
//        BookDTO bookDTO = new BookDTO();
//        bookDTO.getIsbn();
        return books;
    }

    public List<Books> findAll() {
        return bookRepository.findAll();
    }

    // Book 엔티티를 BookDTO로 변환 메서드
    public BookDTO convertToDTO(Books books) {
        BookDTO bookDTO = new BookDTO();

        bookDTO.setBook_id(books.getBook_id());
        bookDTO.setBoards(books.getBoards());
        bookDTO.setIsbn(books.getIsbn());
        bookDTO.setTitle(books.getTitle());
        bookDTO.setAuthor(books.getAuthor());
        bookDTO.setPublisher(books.getPublisher());
        bookDTO.setPubdate(books.getPubdate());
        bookDTO.setDescription(books.getDescription());
        bookDTO.setGrade(books.getGrade());
        bookDTO.setTransactions(books.getTransactions());
        bookDTO.setImages(books.getImages());

        return bookDTO;
    }

    // 나눔 글 상세
    public Books findBoardByBookId(Long book_id) {
        return bookRepository.findById(book_id).orElse(null);
    }

}
