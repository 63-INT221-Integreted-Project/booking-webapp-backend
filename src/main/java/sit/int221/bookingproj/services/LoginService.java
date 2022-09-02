package sit.int221.bookingproj.services;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sit.int221.bookingproj.dtos.UserLoginDto;
import sit.int221.bookingproj.entities.User;
import sit.int221.bookingproj.exception.EmailUserNotFoundException;
import sit.int221.bookingproj.exception.PasswordUserNotMatchException;
import sit.int221.bookingproj.repositories.UserRepository;

@Service
public class LoginService {

    @ExceptionHandler(EmailUserNotFoundException.class)
    public void handleEmailUserNotFound(){}

    @ExceptionHandler(PasswordUserNotMatchException.class)
    public void handleEmailPasswordUserNotMatch(){}

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public TokenService tokenService;
    public String login(UserLoginDto userLoginDto) throws EmailUserNotFoundException, PasswordUserNotMatchException {
        Argon2 argon2 = Argon2Factory.create(
                Argon2Factory.Argon2Types.ARGON2id,
                16,
                16);
        User user = new User();
        userLoginDto.setEmail(userLoginDto.getEmail().trim());
        userLoginDto.setPassword(userLoginDto.getPassword().trim());
        user = userRepository.findAllByEmail(userLoginDto.getEmail());
        if(user != null){
            if(argon2.verify(user.getPassword() , userLoginDto.getPassword())){
                String token = tokenService.tokenize(userLoginDto);
                return token;
            }
            else{
                throw new PasswordUserNotMatchException("This password is not match in system");
            }
        }
        else{
            throw new EmailUserNotFoundException("This Email is Not Found");
        }
    }

}
