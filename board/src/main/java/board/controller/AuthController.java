package board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import board.domain.DTO.SignUpDto;
import board.domain.user.User;
import board.service.AuthService;
import lombok.RequiredArgsConstructor;



@Controller
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	
	@GetMapping("/auth/signin")
    public String signinForm(){
        return "auth/signin";
    }
    @GetMapping("/auth/signup")
    public String signupForm(){
        return "auth/signup";
    } 

    @PostMapping("/auth/signup")
    public String signup(@ModelAttribute SignUpDto signupDto){
            User user = signupDto.toEntity();
            User signupUser = authService.signup(user);
            System.out.println(signupUser);
            return "auth/signin";
    }

}
