package com.checkcheck.ecoreading.domain.delivery.controller;

import com.checkcheck.ecoreading.domain.boards.service.BookService;
import com.checkcheck.ecoreading.domain.books.dto.BookMainDTO;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import com.checkcheck.ecoreading.domain.delivery.dto.DeliveryDTO;
import com.checkcheck.ecoreading.domain.delivery.entity.Delivery;
import com.checkcheck.ecoreading.domain.delivery.repository.DeliveryRepository;
import com.checkcheck.ecoreading.domain.delivery.service.DeliveryService;
import com.checkcheck.ecoreading.domain.pointHistory.service.PointHistoryService;
import com.checkcheck.ecoreading.domain.transactions.entity.TransactionStatus;
import com.checkcheck.ecoreading.domain.transactions.entity.Transactions;
import com.checkcheck.ecoreading.domain.transactions.service.TransactionService;
import com.checkcheck.ecoreading.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;
    private final TransactionService transactionService;
    private final UserService userService;
    private final PointHistoryService pointHistoryService;

    // 나눔받기 완료
    @PostMapping("/board/detail/complete")
    public String completeTakeBook(@ModelAttribute DeliveryDTO deliveryDTO, @ModelAttribute BookMainDTO booksDTO, HttpServletRequest request) {
        Long id = userService.getUserIdFromAccessTokenCookie(request);

        transactionService.updateTransactions(booksDTO, id);  // Transaction 테이블 값 변경
        deliveryService.insertDelivery(deliveryDTO);  // Delivery 테이블 값 추가
        userService.updateUserTotalPoint(id);  // User 테이블 5 책갈피 차감
        pointHistoryService.insertPointHistory(id, booksDTO.getTransactions());  // PointHistory 테이블 값 추가

        return "content/board/completeTakeBook";
    }

}
