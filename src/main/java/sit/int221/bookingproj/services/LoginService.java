package sit.int221.bookingproj.services;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonToken;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sit.int221.bookingproj.dtos.UserLoginDto;
import sit.int221.bookingproj.entities.User;
import sit.int221.bookingproj.exception.EmailUserNotFoundException;
import sit.int221.bookingproj.exception.PasswordUserNotMatchException;
import sit.int221.bookingproj.repositories.UserRepository;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class LoginService {

    @ExceptionHandler(EmailUserNotFoundException.class)
    public void handleEmailUserNotFound(){}

    @ExceptionHandler(PasswordUserNotMatchException.class)
    public void handleEmailPasswordUserNotMatch(){}

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public UserService userService;

    @Autowired
    public TokenService tokenService;
    public JSONObject login(UserLoginDto userLoginDto) throws EmailUserNotFoundException, PasswordUserNotMatchException {
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
                String refreshToken = tokenService.tokenizeRefreshToken(userLoginDto);
                JSONObject json = new JSONObject();
                json.put("access_token", token);
                json.put("refresh_token", refreshToken);
                return json;
            }
            else{
                throw new PasswordUserNotMatchException("This password is not match in system");
            }
        }
        else{
            throw new EmailUserNotFoundException("This Email is Not Found");
        }
    }


    public JSONObject refreshToken(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Integer userId = Integer.parseInt((String) authentication.getPrincipal());
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            User userGet = user.get();
            UserLoginDto userLoginDto = userService.castUserToUserLogin(userGet);
            String token = tokenService.tokenize(userLoginDto);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("access_token", token);
            return jsonObject;
        }
        return null;
    }

    public JSONObject reToken(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Integer userId = Integer.parseInt((String) authentication.getPrincipal());
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            User userGet = user.get();
            UserLoginDto userLoginDto = userService.castUserToUserLogin(userGet);
            String token = tokenService.tokenize(userLoginDto);
            JSONObject refreshToken = new JSONObject();
            refreshToken.put("access_token", token);
            return refreshToken;
        }
        return null;
    }

    public JSONObject getProfile(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Integer userId = Integer.parseInt((String) authentication.getPrincipal());
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("role", user.get().getRole());
            jsonObject.put("email", user.get().getEmail());
            jsonObject.put("name", user.get().getName());
            jsonObject.put("userId", user.get().getUserId());
            return jsonObject;
        }
        return null;
    }

}
