package com.checkcheck.ecoreading.domain.users.controller;


import com.checkcheck.ecoreading.domain.boards.entity.Boards;
import com.checkcheck.ecoreading.domain.boards.service.BookService;
import com.checkcheck.ecoreading.domain.books.entity.Books;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        // try 블록은 유지하되, catch 블록은 제거합니다.
        TokenInfo tokenInfo = userService.login(loginDto,response);
       // model.addAttribute("token", tokenInfo);
        return "redirect:/main/"; // 성공 시 메인 페이지로 리다이렉트
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
        System.out.println("email = " + email);
        //userService.createPasswordResetToken(email.getEmail());
        userService.sendMailPasswordReset(email.getEmail(),userService.createPasswordResetToken(email.getEmail()));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token,Model model) {
        // 토큰 유효성 검사
        boolean isTokenValid = userService.validatePasswordResetToken(token);
//        if (!isTokenValid) {
//            return ResponseEntity.badRequest().body("잘못된 토큰입니다.");
//        }
        /*
         todo : 여기서는 예시로 OK 상태만 반환하며, 실제로는 비밀번호 재설정 페이지로 리디렉션할 수 있다.
         이 페이지는 사용자가 로그인하지 않아도 접근할 수 있어야 하며, 이메일을 통해 받은 유효한 토큰을 기반으로 비밀번호를 재설정할 수 있도록 설계된다.
         따라서 사용자가 로그인 상태가 아니어도, 재설정 링크에 포함된 토큰을 사용하여 접근하고 비밀번호를 변경할 수 있다.
         이 토큰은 사용자를 인증하고 비밀번호 재설정 권한을 부여하는 역할을 한다.
         */
        // 이메일이랑, 토큰 보내야함.
        return "changePw";
    }


    @GetMapping("/mypage/givelist")
    public String giveBoardList(Model model){
        Users users = new Users();
        users.setUsersId(1L);
        List<Boards> boards = bookService.giveList(users);
        model.addAttribute("boards",boards);
        return "/content/mypage/giveList";
    }

    @GetMapping("/mypage/takelist")
    public String takeBoardList(Model model){
        Long takerId = 1L;
        List<Books> books = bookService.takeList(takerId);
        model.addAttribute("books",books);
        return "/content/mypage/takeList";
    }

    @GetMapping("/mypage/myinfor")
    public String myInformation(Model model){
        Long userId = 1L;
        Users user = userService.findAllById(userId);
        System.out.println(user.getPointHistoryList());
        model.addAttribute("user", user);
        return "content/mypage/myInfor";
    }

}
