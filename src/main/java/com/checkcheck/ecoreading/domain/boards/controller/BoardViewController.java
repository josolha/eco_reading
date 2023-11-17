package com.checkcheck.ecoreading.domain.boards.controller;

import com.checkcheck.ecoreading.domain.boards.dto.*;
import com.checkcheck.ecoreading.domain.boards.entity.Boards;
import com.checkcheck.ecoreading.domain.boards.service.BoardService;
import com.checkcheck.ecoreading.domain.boards.service.BookService;
import com.checkcheck.ecoreading.domain.books.dto.BookMainDTO;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import com.checkcheck.ecoreading.domain.delivery.dto.DeliveryDTO;
import com.checkcheck.ecoreading.domain.delivery.entity.Delivery;
import com.checkcheck.ecoreading.domain.delivery.service.DeliveryService;
import com.checkcheck.ecoreading.domain.transactions.entity.TransactionStatus;
import com.checkcheck.ecoreading.domain.transactions.entity.Transactions;
import com.checkcheck.ecoreading.domain.transactions.service.TransactionService;
import com.checkcheck.ecoreading.domain.users.service.UserService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
@Slf4j
public class BoardViewController {

    private final BookService bookService;
    private final BoardService boardService;
    private final DeliveryService deliveryService;
    private final TransactionService transactionService;
    private final UserService userService;

    // 나눔글 쓰기
    @GetMapping("/new")
    public String addBoard(HttpServletRequest request) {
        Long id = userService.getUserIdFromAccessTokenCookie(request);
        System.out.println("시이잉이이이이이잉이ㅣㅇ이"+id);
        return "/content/user/boardAddForm";
    }

    // 나눔 글 상세
    @GetMapping("/detail/{booksId}")
    public String boardDetail(@PathVariable Long booksId, Model model) {
        Books books = bookService.findBoardByBookId(booksId);
        BookMainDTO booksDTO = bookService.convertToDTO(books);  // DTO로 변환

        if (booksDTO == null) {
            return "redirect:/error";
        }

        model.addAttribute("book", booksDTO);

        return "/content/board/boardDetail";
    }

    // 나눔받기
    @GetMapping("/detail/{booksId}/taker")
    public String takeBook(@PathVariable Long booksId, Model model) {
        Books books = bookService.findBoardByBookId(booksId);
        BookMainDTO booksDTO = bookService.convertToDTO(books);  // DTO로 변환
        DeliveryDTO deliveryDTO = deliveryService.convertToDeliveryDTO(new Delivery());

        if (booksDTO == null) {
            return "redirect:/error";
        }

        // 나눔완료시 status=나눔완료, taker_id=현재유저아이디, success_date=현재날짜로 값 삽임
        Transactions transactions = books.getTransactions();
        transactions.setStatus(TransactionStatus.나눔완료);  // status 나눔완료로 변경
        transactions.setTakerId(booksDTO.getBoards().getUsers().getUsersId());  // takerId 현재 유저아이디 삽입
        transactions.setSuccessDate(LocalDateTime.now());  // successDate 현재 날짜로 삽입
        transactionService.saveTransaction(transactions);

        model.addAttribute("book", booksDTO);
        model.addAttribute("delivery", deliveryDTO);

        return "/content/board/takeBook";
    }

    @GetMapping("/update/{boardId}")
    public String boardUpdateForm(@PathVariable Long boardId, Model model){
        Boards boards = boardService.findAllByBoardId(boardId);
        model.addAttribute(boards);
        return "content/board/boardUpdateForm";
    }

//    @PostMapping("/update/{boardId}")
//    public ModelAndView boardUpdate(@PathVariable Long boardId, @ModelAttribute InsertBookDTO bookDTO, @ModelAttribute InsertBoardDTO boardDTO, @ModelAttribute InsertDeliveryDTO deliveryDTO, Model model, ModelAndView modelAndView){
//        boardService.updateBoardByBoardId(boardId, bookDTO, boardDTO, deliveryDTO, model);
//        modelAndView.addObject(boardService.updateBoardByBoardId(boardId, bookDTO, boardDTO, deliveryDTO, model));
//        modelAndView.setViewName("content/board/updateComplete");
//        return modelAndView;
//    }

    @PostMapping(value = "/update/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String boardUpdate(@RequestParam(value = "image", required = false) List<MultipartFile> multipartFiles,
                              UpdateBookDTO bookDTO, UpdateBoardDTO boardDTO,
                              UpdateDeliveryDTO deliveryDTO, @PathVariable String boardId) {
       Boards boards = boardService.findAllByBoardId(Long.parseLong(boardId));
        bookService.findBoardByBookId(boards.getBooksList().get(0).getBooksId());
        // 이미지를 선택하지 않았을 때의 처리
        if (multipartFiles.get(0) == null) {
            // 등록 폼의 전체 데이터 업로드하기
            boardService.updateBoardWithoutImages(bookDTO, boardDTO, deliveryDTO, boards);

        } else {
            // 이미지를 선택하지 않은 경우의 처리 (예: 이미지를 업로드하지 않고 다른 데이터만 업데이트)
            boardService.updateBoard(multipartFiles, bookDTO, boardDTO, deliveryDTO, boards);
        }

        return "redirect:/main/";
    }

    @GetMapping("/board/update/givelist")
    public String boardList(){
        return "content/mypage/giveList";
    }
}
