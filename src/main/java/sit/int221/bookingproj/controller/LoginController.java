package sit.int221.bookingproj.controller;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.int221.bookingproj.dtos.UserActionDto;
import sit.int221.bookingproj.dtos.UserLoginDto;
import sit.int221.bookingproj.entities.User;
import sit.int221.bookingproj.exception.EmailUserNotFoundException;
import sit.int221.bookingproj.exception.PasswordUserNotMatchException;
import sit.int221.bookingproj.exception.UniqueEmailException;
import sit.int221.bookingproj.exception.UniqueNameException;
import sit.int221.bookingproj.services.LoginService;
import sit.int221.bookingproj.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class LoginController {
    @Autowired
    public LoginService loginService;

    @Autowired
    public UserService userService;

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

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public User createUser(@Valid @RequestBody UserActionDto userActionDto) throws UniqueEmailException, UniqueNameException {
        return userService.createUser(userActionDto);
    }

    @GetMapping("/token/guest")
    public JSONObject getNewGuestToken(){
        return loginService.getGuestToken();
    }

}
