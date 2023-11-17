package com.checkcheck.ecoreading.domain.admin.controller;

import com.checkcheck.ecoreading.domain.boards.entity.Boards;
import com.checkcheck.ecoreading.domain.boards.service.BoardService;
import com.checkcheck.ecoreading.domain.boards.service.BookService;
import com.checkcheck.ecoreading.domain.books.dto.BookMainDTO;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import com.checkcheck.ecoreading.domain.transactions.entity.TransactionStatus;
import com.checkcheck.ecoreading.domain.transactions.entity.Transactions;
import com.checkcheck.ecoreading.domain.transactions.service.TransactionService;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import com.checkcheck.ecoreading.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static java.lang.Integer.parseInt;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminViewController {
    private final UserService userService;
    private final BoardService boardService;
    private final BookService bookService;
    private final TransactionService transactionService;

    @GetMapping()
    public String admin(Model model) {
        List<Users> usersList = userService.findAll();
        model.addAttribute("usersList", usersList);
        return "/content/admin/main";
    }

    @GetMapping("/board")
    public String board(Model model) {
        List<Boards> boardsList = boardService.findAll();
        model.addAttribute("boardsList", boardsList);
        return "/content/admin/boardList";
    }

//    @GetMapping("/{boards-id}/boardDetail/checkList")
//    public String checkList(){ return "/content/admin/checkList"; }

    @GetMapping("/board/detail/{boardId}")
    public String boardDetail(@PathVariable Long boardId, Model model) {
        Boards boards = boardService.findAllByBoardId(boardId);
        model.addAttribute("bookList", boards.getBooksList());
        return "content/admin/boardDetail";
    }

    @GetMapping("/user/details/{usersId}")
    public String userDetail(@PathVariable Long usersId, Model model) {
        Users user = userService.findAllById(usersId);
        model.addAttribute("user", user);
        return "content/admin/userDetail";
    }

    @GetMapping("/search")
    public String searchBookList(@RequestParam(name = "searchType", required = false, defaultValue = "search") String searchType,
                                 @RequestParam(name = "search", required = false) String searchInput, Model model) {

        if ("isbn".equals(searchType)) model.addAttribute("boardsList", bookService.searchBooksFromIsbn(searchInput));
        else if ("status".equals(searchType))
            model.addAttribute("boardsList", transactionService.findAllByStatus(TransactionStatus.valueOf(searchInput)));
        else {
            model.addAttribute("boardsList", userService.findAllById(Long.parseLong(searchInput)).getBoardsList());
        }
        return "content/admin/searchedBoards";
    }

    @GetMapping("/checkList/{boardId}")
    public String checkList(@PathVariable Long boardId, Model model) {
        model.addAttribute(boardId);
        return "content/admin/checkList";
    }

    @PostMapping("/checkList/{boardId}")
    public String checkListResult(@PathVariable Long boardId, @RequestParam("minScore") int minScore) {
//        System.out.println(minScore);
        Boards boards = boardService.findAllByBoardId(boardId);
        Books books = bookService.findBoardByBookId(boards.getBooksList().get(0).getBooksId());
        Transactions transactions = transactionService.findByBooks(books);
        transactionService.updateTransactionStatusFinishCheck(transactions);
        // 북의 등급을 업데이트 하고 값을 가져옴
        Books updateGradeBook = bookService.updateGrade(books, minScore);
        userService.updatePoint(updateGradeBook, boards.getUsers().getUsersId(), transactions);
        return "redirect:/admin/board"; // 리다이렉션
    }

    @GetMapping("/update/status/{transactionsId}")
    public String updateTransactionsStatus(@PathVariable String transactionsId) {
        Long id = Long.parseLong(transactionsId);
        Transactions transactions = transactionService.findByTransactionsId(id);
        transactionService.updateTransactionsStatus(transactions);
        return "redirect:/admin/board";
    }

    @GetMapping("/search/user")
    public String selectUserEnabledTrue(@RequestParam(value = "enabled", defaultValue = "true") boolean enabled, Model model) {
        List<Users> user = userService.findAllByEnabled(enabled);
        model.addAttribute("usersList", user);
        return "content/admin/main";
    }

}
