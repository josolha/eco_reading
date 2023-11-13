package com.checkcheck.ecoreading.domain.users.controller;


import com.checkcheck.ecoreading.domain.boards.entity.Boards;
import com.checkcheck.ecoreading.domain.boards.service.BookService;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import com.checkcheck.ecoreading.domain.users.dto.EmailVerificationRequestDTO;
import com.checkcheck.ecoreading.domain.users.dto.UserEmailVerificationRequestDTO;
import com.checkcheck.ecoreading.domain.users.dto.UserLoginRequestDTO;
import com.checkcheck.ecoreading.domain.users.dto.UserRegisterRequestDTO;
import com.checkcheck.ecoreading.domain.users.dto.UserResponseDTO;
import com.checkcheck.ecoreading.domain.users.dto.UserResponseDTO.TokenInfo;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import com.checkcheck.ecoreading.domain.users.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/user")
public class UserApiController {

    private final UserService userService;

    @PostMapping("/signup")
    public String signup(UserRegisterRequestDTO request) {
        System.out.println("request = " + request);
        userService.save(request);
        return "redirect:/user/login";
    }

    @PostMapping("/login")
    public String login(Model model, UserLoginRequestDTO loginDto, HttpServletResponse response) {
        // try 블록은 유지하되, catch 블록은 제거합니다.
        TokenInfo tokenInfo = userService.login(loginDto,response);
        // TODO: 토큰을 세션에 저장하거나 쿠키에 추가하는 등의 로직을 추가하세요. ok 함
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
//        System.out.println("email = " + emailVerificationRequestDTO.getEmail());
//        System.out.println("code = " + emailVerificationRequestDTO.getCode());
        userService.verifiedCode(emailVerificationRequestDTO.getEmail(), emailVerificationRequestDTO.getCode());
        return new ResponseEntity<>(HttpStatus.OK);
    }




}
