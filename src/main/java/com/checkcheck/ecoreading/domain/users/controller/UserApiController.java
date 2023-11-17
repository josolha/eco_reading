package com.checkcheck.ecoreading.domain.users.controller;


import com.checkcheck.ecoreading.domain.boards.entity.Boards;
import com.checkcheck.ecoreading.domain.boards.service.BookService;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import com.checkcheck.ecoreading.domain.transactions.entity.Transactions;
import com.checkcheck.ecoreading.domain.users.dto.EmailVerificationRequestDTO;
import com.checkcheck.ecoreading.domain.users.dto.UserEmailVerificationRequestDTO;
import com.checkcheck.ecoreading.domain.users.dto.UserFindIdDTO;
import com.checkcheck.ecoreading.domain.users.dto.UserKakaoRegisterRequestDTO;
import com.checkcheck.ecoreading.domain.users.dto.UserLoginRequestDTO;
import com.checkcheck.ecoreading.domain.users.dto.UserOAuth2CustomDTO;
import com.checkcheck.ecoreading.domain.users.dto.UserRegisterRequestDTO;
import com.checkcheck.ecoreading.domain.users.dto.UserResponseDTO.TokenInfo;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import com.checkcheck.ecoreading.domain.users.service.UserService;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/user")
public class UserApiController {

    private final BookService bookService;

    private final UserService userService;

    @PostMapping("/signup")
    public String signup(UserRegisterRequestDTO request) {
        System.out.println("request = " + request);
        userService.save(request);
        return "redirect:/user/login";
    }

    @PostMapping("/kakao/signup")
    public String kakaoSignup(UserKakaoRegisterRequestDTO request) {
        System.out.println("request = " + request);
        userService.saveKakao(request);
        return "redirect:/user/kakao/login";
    }
    @GetMapping("/kakao/login")
    public String kakaoLogin(@AuthenticationPrincipal UserOAuth2CustomDTO oauthUser, HttpServletResponse response) {
        TokenInfo tokenInfo = userService.kakaoLogin(oauthUser,response);
        return "redirect:/user/";
    }

    @PostMapping("/login")
    public String login(Model model, UserLoginRequestDTO loginDto, HttpServletResponse response) {
        // try 블록은 유지하되, catch 블록은 제거합니다.
        TokenInfo tokenInfo = userService.login(loginDto,response);
       // model.addAttribute("token", tokenInfo);
        return "redirect:/user/"; // 성공 시 메인 페이지로 리다이렉트
        // GlobalExceptionHandler가 예외를 처리함.
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        userService.logout(request, response);
       // new SecurityContextLogoutHandler().logout(request,response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/user/login";
    }
    @PostMapping("/emails/verification-requests")
    public ResponseEntity sendMessage(@RequestBody @Validated UserEmailVerificationRequestDTO request, BindingResult bindingResult) {
        //todo : 아래 에러 처리 필요
        if (bindingResult.hasErrors()) {
           return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        userService.sendCodeToEmail(request.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/emails/verifications")
    public ResponseEntity verificationEmail(@RequestBody EmailVerificationRequestDTO emailVerificationRequestDTO) {
        System.out.println("email = " + emailVerificationRequestDTO.getEmail());
        System.out.println("code = " + emailVerificationRequestDTO.getCode());
        userService.verifiedCode(emailVerificationRequestDTO.getEmail(), emailVerificationRequestDTO.getCode());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/point")
    public String getTotalPoint(Model model) {
        Long userId = 1L;
        Users user = userService.findAllById(userId);
        int totalPoint = user.getTotalPoint();
        model.addAttribute("totalPoint", totalPoint);
        return "/header/header";
    }
  
    @GetMapping("/social/login")
    public String postLoginProcessing(@AuthenticationPrincipal UserOAuth2CustomDTO oauthUser, RedirectAttributes attributes,HttpServletResponse response) {
        if (oauthUser.isNewUser()) {
            System.out.println("새로운 회원입니다.");
            attributes.addFlashAttribute("email", oauthUser.getEmail());
            attributes.addFlashAttribute("nickname", oauthUser.getNickname());
            attributes.addFlashAttribute("socialAuthId", oauthUser.getSocialId());
            return "redirect:/user/kakao/signup";
        } else {
            System.out.println("이미있는회원.");
            userService.kakaoLogin(oauthUser, response);
            return "redirect:/user/"; // 메인 페이지 또는 적절한 대시보드로 리디렉션
        }
    }
    @PostMapping("/find-email")
    public ResponseEntity findEmail(@RequestBody UserFindIdDTO findIdDTO){
        System.out.println("findIdDTO = " + findIdDTO);
        Optional<Users> users = userService.findByUserNameAndPhone(findIdDTO.getUsername(), findIdDTO.getPhone());

        if(users.isPresent()) {
            // 사용자 정보나 관련 메시지를 포함하여 반환
            return ResponseEntity.ok(users.get());
        } else {
            // 사용자를 찾을 수 없는 경우 적절한 상태 코드와 메시지를 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
    }

    @GetMapping("/mypage/givelist")
    public String giveBoardList(Model model){
        Users users = new Users();
        users.setUsersId(1L);  // todo: 토큰에서 id 가져와야 함
        List<Boards> boards = bookService.giveList(users);
        model.addAttribute("boards",boards);
        return "/content/mypage/giveList";
    }

//    @GetMapping("/mypage/takelist")
//    public String takeBoardList(Model model){
//        Long takerId = 1L;
//        List<Books> books = bookService.takeList(takerId);
//        model.addAttribute("books",books);
//        return "/content/mypage/takeList";
//    }

    @GetMapping("/mypage/myinfor")
    public String myInformation(Model model){
        Long userId = 1L;  // todo: 토큰에서 id 가져와야 함
        Users user = userService.findAllById(userId);
        System.out.println(user.getPointHistoryList());
        model.addAttribute("user", user);
        return "content/mypage/myInfor";
    }

    // takeList 페이징 처리
    @GetMapping("/mypage/takelist")
    public String takeBoardListPaging(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model
    ) {
        Long takerId = 1L;
        Page<Books> booksPage = bookService.findBooksByTakerId(takerId, page, size);
        model.addAttribute("books", booksPage);
        return "/content/mypage/takeList";
    }

}
