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
import com.checkcheck.ecoreading.domain.users.dto.UserPasswordResetEmail;
import com.checkcheck.ecoreading.domain.users.dto.UserRegisterRequestDTO;
import com.checkcheck.ecoreading.domain.users.dto.UserResponseDTO.TokenInfo;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import com.checkcheck.ecoreading.domain.users.exception.AuthenticationEmailException;
import com.checkcheck.ecoreading.domain.users.service.UserService;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String kakaoLogin(HttpServletResponse response,HttpSession session) {
        //System.out.println("oauthUser ============= " + oauthUser);
        UserOAuth2CustomDTO oauthUser = (UserOAuth2CustomDTO) session.getAttribute("oauthUser");
        //session.getAttribute("oauthUser");
        TokenInfo tokenInfo = userService.kakaoLogin(oauthUser,response);
        return "redirect:/main/";
    }

    @PostMapping("/login")
    public String login(Model model, UserLoginRequestDTO loginDto, HttpServletResponse response) {
        TokenInfo tokenInfo = userService.login(loginDto,response);
        return "redirect:/main/"; // 성공 시 메인 페이지로 리다이렉트
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
    public String postLoginProcessing(@AuthenticationPrincipal UserOAuth2CustomDTO oauthUser, RedirectAttributes attributes,
                                      HttpSession session, HttpServletResponse response) {
        log.info("oauthUser" +oauthUser);

        try {
            if (oauthUser.isNewUser()) {
                System.out.println("새로운 회원입니다.");
                attributes.addFlashAttribute("email", oauthUser.getEmail());
                attributes.addFlashAttribute("nickname", oauthUser.getNickname());
                attributes.addFlashAttribute("socialAuthId", oauthUser.getSocialId());
                session.setAttribute("oauthUser", oauthUser);
                return "redirect:/user/kakao/signup";
            } else {
                System.out.println("이미있는회원.");
                userService.kakaoLogin(oauthUser, response);
                return "redirect:/main/";
            }
        } catch (AuthenticationEmailException e) {
            // 예외 처리
            log.info("controller"+ e.getMessage());
            attributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/login";
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
    @PostMapping("/find-password")
    public ResponseEntity findPassword(@RequestBody UserPasswordResetEmail email){
        try {
            String token = userService.createPasswordResetToken(email.getEmail());
            userService.sendMailPasswordReset(email.getEmail(), token);
            return ResponseEntity.ok().build();
        } catch (UnsupportedOperationException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("카카오 계정은 비밀번호 재설정을 지원하지 않습니다.");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("사용자를 찾을 수 없습니다.");
        }
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token,Model model) {
        // 토큰 유효성 검사
        boolean isTokenValid = userService.validatePasswordResetToken(token);
//        if (!isTokenValid) {
//            return ResponseEntity.badRequest().body("잘못된 토큰입니다.");
//        }
        model.addAttribute("token",token);
        return "changePw";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token,
                                @RequestParam("newPassword") String newPassword,
                                @RequestParam("confirmPassword") String confirmPassword,
                                Model model) {

        System.out.println("token = " + token);
        System.out.println("newPassword = " + newPassword);
        System.out.println("confirmPassword = " + confirmPassword);

        // 여기서 토큰 유효성 검사 및 비밀번호 일치 여부 확인
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("token",token);
            return "changePw"; // 비밀번호 재설정 페이지로 리디렉션
        }

        if (!userService.validatePasswordResetToken(token)) {
            model.addAttribute("error", "유효하지 않은 토큰입니다.");
            model.addAttribute("token",token);
            return "changePw"; // 비밀번호 재설정 페이지로 리디렉션
        }

        // 비밀번호 변경 로직 수행
        userService.changePassword(token, newPassword);
        return "redirect:/user/login"; // 로그인 페이지로 리디렉션
    }


//    @GetMapping("/mypage/givelist")
//    public String giveBoardList(Model model){
//        Users users = new Users();
//        users.setUsersId(1L);  // todo: 토큰에서 id 가져와야 함
//        List<Boards> boards = bookService.giveList(users);
//        model.addAttribute("boards",boards);
//        return "/content/mypage/giveList";
//    }

//    @GetMapping("/mypage/takelist")
//    public String takeBoardList(Model model){
//        Long takerId = 1L;
//        List<Books> books = bookService.takeList(takerId);
//        model.addAttribute("books",books);
//        return "/content/mypage/takeList";
//    }

    @GetMapping("/mypage/myinfor")
    public String myInformation(Model model, HttpServletRequest request){
        Long id = userService.getUserIdFromAccessTokenCookie(request);
//        Long userId = 1L;  // todo: 토큰에서 id 가져와야 함
        Users user = userService.findAllById(id);
        System.out.println(user.getPointHistoryList());
        model.addAttribute("user", user);
        return "content/mypage/myInfor";
    }

    // takeList 페이징 처리
    @GetMapping("/mypage/takelist")
    public String takeBoardListPaging(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model, HttpServletRequest request
    ) {
        Long id = userService.getUserIdFromAccessTokenCookie(request);
        Page<Books> booksPage = bookService.findBooksByTakerId(id, page, size);
        model.addAttribute("books", booksPage);
        return "/content/mypage/takeList";
    }

    // giveList 페이징 처리
    @GetMapping("/mypage/givelist")
    public String giveBoardList(@RequestParam(name = "page", defaultValue = "0") int page,
                                @RequestParam(name = "size", defaultValue = "10") int size,
                                Model model, HttpServletRequest request) {
        Long id = userService.getUserIdFromAccessTokenCookie(request);
        Users user = userService.findAllById(id);

        Page<Boards> boardsPage = bookService.giveListPaging(user, page, size);
        model.addAttribute("boards", boardsPage);
        return "/content/mypage/giveList";
    }

}
