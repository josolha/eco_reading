package com.checkcheck.ecoreading.domain.admin.controller;

import com.checkcheck.ecoreading.domain.boards.entity.Boards;
import com.checkcheck.ecoreading.domain.boards.service.BoardService;
import com.checkcheck.ecoreading.domain.books.dto.BookMainDTO;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import com.checkcheck.ecoreading.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminViewController {
    private final UserService userService;
    private final BoardService boardService;
    @GetMapping()
    public String admin(Model model){
        List<Users> usersList = userService.findAll();
        model.addAttribute("usersList", usersList);
        return "/content/admin/main";
    }

    @GetMapping("/board")
    public String board(Model model){
        List<Boards> boardsList = boardService.findAll();
        model.addAttribute("boardsList", boardsList);
        return "/content/admin/boardList";
    }

    @GetMapping("/{board-id}/boardDetail/checkList")
    public String checkList(){ return "/content/admin/checkList"; }

    @GetMapping("/board/detail/{boardId}")
    public String boardDetail(@PathVariable Long boardId, Model model) {
        Boards boards = boardService.findAllByBoardId(boardId);
        model.addAttribute("bookList", boards.getBooksList());
        return "content/admin/boardDetail";
    }

    @GetMapping("/user/details/{usersId}")
    public String userDetail(@PathVariable Long usersId, Model model){
        Users user = userService.findAllById(usersId);
        model.addAttribute("user", user);
        return "content/admin/userDetail";
    }

//    @GetMapping("/search")
//    public String searchBookList(@RequestParam(name = "searchType", required = false, defaultValue = "search") String searchType,
//                                 @RequestParam(name = "search", required = false) String searchInput, Model model){
//
//        List<Boards> books = boardService.adminSearchBooks(searchType, searchInput);
//        List<BookMainDTO> searchResults = bookService.returnDTOs(books);  // DTOs로 변환
//
//        model.addAttribute("searchBooks", searchResults);
//
//        return "content/board/searchBooks";
//    }
}
