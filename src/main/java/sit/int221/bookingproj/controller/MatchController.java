package sit.int221.bookingproj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sit.int221.bookingproj.dtos.UserActionDto;
import sit.int221.bookingproj.dtos.UserLoginDto;
import sit.int221.bookingproj.entities.User;
import sit.int221.bookingproj.exception.EmailUserNotFoundException;
import sit.int221.bookingproj.exception.PasswordUserNotMatchException;
import sit.int221.bookingproj.repositories.UserRepository;
import sit.int221.bookingproj.services.MatchService;

@RestController
@RequestMapping("/api/auth/match")
public class MatchController {

    @Autowired
    public MatchService matchService;

    @PostMapping(name = "")
    @ResponseStatus(code = HttpStatus.OK)
    public HttpStatus matchPassword(@RequestBody UserLoginDto userLoginDto) throws PasswordUserNotMatchException, EmailUserNotFoundException {
        return matchService.matchUsernameAndPassword(userLoginDto);
    }
}
