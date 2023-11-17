package com.checkcheck.ecoreading.domain.boards.controller;


import com.checkcheck.ecoreading.domain.boards.dto.InsertBoardDTO;
import com.checkcheck.ecoreading.domain.boards.dto.InsertBookDTO;
import com.checkcheck.ecoreading.domain.boards.dto.InsertDeliveryDTO;
import com.checkcheck.ecoreading.domain.boards.entity.Boards;
import com.checkcheck.ecoreading.domain.boards.service.BoardService;
import com.checkcheck.ecoreading.domain.boards.service.BookService;
import com.checkcheck.ecoreading.domain.books.dto.BookDTO;
import com.checkcheck.ecoreading.domain.books.dto.NaverBookDTO;
import com.checkcheck.ecoreading.domain.images.service.ImageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;
    private final BookService bookService;
    private final ImageService imageService;
    // 나눔글 등록 폼에서 input 가져와서 DB에 업로드
    @PostMapping(value = "/board/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadBoard(@RequestParam("image") List<MultipartFile> multipartFiles,
                              InsertBookDTO bookDTO, InsertBoardDTO boardDTO, InsertDeliveryDTO deliveryDTO) {
        // 등록 폼의 전체 데이터 업로드하기
        boardService.uploadBoard(multipartFiles, bookDTO, boardDTO, deliveryDTO);

        return "redirect:/main/";
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

    @GetMapping("/board/delete/{boardId}")
    public String deleteBoard(@PathVariable Long boardId){
        if (boardService.deleteBoardByBoardId(boardId)) return "/content/board/deleteComplete";
        return "/content/mypage/giveList";
    }

    @GetMapping("/board/update/bookSearch")
    public String updateSearch(@RequestParam String searchText, Model model, @RequestParam Long boardId) {
        List<NaverBookDTO> books = bookService.searchBooks(searchText);
        System.out.println("검색결과: "+ books);
        model.addAttribute("books", books);
        model.addAttribute("boardId", boardId);
        model.addAttribute("BookDTO", new BookDTO());
        return "/content/board/updateBoardSearch";
    }

    @PostMapping("/board/update/bookSearch")
    public String updateBook(@ModelAttribute BookDTO dto, @RequestParam Long boardId, Model model) {
        bookService.update(dto, boardId);
        Boards boards = boardService.findAllByBoardId(boardId);
        model.addAttribute("boards", boards);
        return "/content/board/updateBoardSearchForm";
    }

    @PostMapping("/board/deleteimage/{imagesId}")
    public String deleteImage(@PathVariable Long imagesId, @RequestParam("boardId") Long boardId, Model model){
        imageService.deleteImage(imagesId);
        model.addAttribute(boardService.findAllByBoardId(boardId));
        return "/content/board/boardUpdateForm";
    }


}
