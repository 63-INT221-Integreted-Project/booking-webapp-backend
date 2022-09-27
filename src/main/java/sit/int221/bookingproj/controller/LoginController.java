package sit.int221.bookingproj.controller;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.int221.bookingproj.dtos.UserLoginDto;
import sit.int221.bookingproj.exception.EmailUserNotFoundException;
import sit.int221.bookingproj.exception.PasswordUserNotMatchException;
import sit.int221.bookingproj.services.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class LoginController {
    @Autowired
    public LoginService loginService;

    @PostMapping("/login")
    public JSONObject login(@RequestBody UserLoginDto userLoginDto) throws PasswordUserNotMatchException, EmailUserNotFoundException {
        return loginService.login(userLoginDto);
    }

    @GetMapping("/profile")
    public JSONObject getProfile(){
        return loginService.getProfile();
    }

    @GetMapping("/refresh")
    public JSONObject refreshToken(){
        return loginService.refreshToken();
    }

    @GetMapping("/token")
    public JSONObject getNewToken(){
        return loginService.reToken();
    }

}
