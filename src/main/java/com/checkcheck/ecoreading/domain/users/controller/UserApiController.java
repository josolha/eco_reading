package com.checkcheck.ecoreading.domain.users.controller;


import com.checkcheck.ecoreading.domain.users.dto.UserEmailVerificationRequestDTO;
import com.checkcheck.ecoreading.domain.users.dto.UserRegisterRequest;
import com.checkcheck.ecoreading.domain.users.service.MailService;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
@Slf4j
public class UserApiController {

    private final UserService userService;
    private final MailService mailService;

    @PostMapping("/user")
    public String signup(UserRegisterRequest request){
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
        if (bindingResult.hasErrors()) {
           return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        userService.sendCodeToEmail(request.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @GetMapping("/emails/verifications")
//    public ResponseEntity verificationEmail(@RequestParam("email") @Valid @CustomEmail String email,
//                                            @RequestParam("code") String authCode) {
//        EmailVerificationResult response = memberService.verifiedCode(email, authCode);
//
//        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
//    }
}
