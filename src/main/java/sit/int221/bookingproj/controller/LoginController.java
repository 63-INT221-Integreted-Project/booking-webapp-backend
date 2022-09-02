package sit.int221.bookingproj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sit.int221.bookingproj.dtos.UserLoginDto;
import sit.int221.bookingproj.exception.EmailUserNotFoundException;
import sit.int221.bookingproj.exception.PasswordUserNotMatchException;
import sit.int221.bookingproj.services.LoginService;

@RestController
@RequestMapping("/auth/login")
public class LoginController {
    @Autowired
    public LoginService loginService;

    @PostMapping("")
    public String login(@RequestBody UserLoginDto userLoginDto) throws PasswordUserNotMatchException, EmailUserNotFoundException {
        return loginService.login(userLoginDto);
    }

}
