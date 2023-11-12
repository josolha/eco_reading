package com.checkcheck.ecoreading.domain.boards.controller;

import com.checkcheck.ecoreading.domain.boards.dto.InsertBoardDTO;
import com.checkcheck.ecoreading.domain.boards.dto.InsertBookDTO;
import com.checkcheck.ecoreading.domain.boards.dto.InsertDeliveryDTO;
import com.checkcheck.ecoreading.domain.boards.entity.Boards;
import com.checkcheck.ecoreading.domain.boards.service.BoardService;
import com.checkcheck.ecoreading.domain.boards.service.BookService;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardViewController {

    private final BookService bookService;
    private final BoardService boardService;

    // 나눔글 쓰기
    @GetMapping("/new")
    public String addBoard() {
        return "/content/user/boardAddForm";
    }

    // 나눔 글 상세
    @GetMapping("/detail/{book_id}")
    public String boardDetail(@PathVariable Long book_id, Model model) {
        Books books = bookService.findBoardByBookId(book_id);

        if (books == null) {
            return "redirect:/error";
        }

        model.addAttribute("book", books);

        return "/content/board/detail";
    }

    @GetMapping("/update/{boardId}")
    public String boardUpdateForm(@PathVariable Long boardId, Model model){
        Boards boards = boardService.findAllByBoardId(boardId);
        model.addAttribute(boards);
        return "content/board/boardUpdateForm";
    }

    @PostMapping("/update/{boardId}")
    public ModelAndView boardUpdate(@PathVariable Long boardId, @ModelAttribute InsertBookDTO bookDTO, @ModelAttribute InsertBoardDTO boardDTO, @ModelAttribute InsertDeliveryDTO deliveryDTO, Model model, ModelAndView modelAndView){
        boardService.updateBoardByBoardId(boardId, bookDTO, boardDTO, deliveryDTO, model);
        modelAndView.addObject(boardService.updateBoardByBoardId(boardId, bookDTO, boardDTO, deliveryDTO, model));
        modelAndView.setViewName("content/board/updateComplete");
        return modelAndView;
    }

    @GetMapping("/board/update/givelist")
    public String boardList(){
        return "content/mypage/giveList";
    }
}
