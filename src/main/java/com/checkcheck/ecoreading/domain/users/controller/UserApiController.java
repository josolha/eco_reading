package com.checkcheck.ecoreading.domain.users.controller;


import com.checkcheck.ecoreading.domain.boards.entity.Boards;
import com.checkcheck.ecoreading.domain.boards.service.BookService;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import com.checkcheck.ecoreading.domain.users.dto.EmailVerificationRequestDTO;
import com.checkcheck.ecoreading.domain.users.dto.UserEmailVerificationRequestDTO;
import com.checkcheck.ecoreading.domain.users.dto.UserRegisterRequestDTO;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import com.checkcheck.ecoreading.domain.users.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@RequiredArgsConstructor
@Controller
@Slf4j
public class UserApiController {

    private final UserService userService;


    @PostMapping("/user")
    public String signup(UserRegisterRequestDTO request){
        System.out.println("request = " + request);
        userService.save(request);
        return "redirect:/login";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request,response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
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
