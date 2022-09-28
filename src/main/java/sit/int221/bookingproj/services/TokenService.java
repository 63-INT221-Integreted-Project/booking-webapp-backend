package sit.int221.bookingproj.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sit.int221.bookingproj.dtos.UserLoginDto;
import sit.int221.bookingproj.entities.User;
import sit.int221.bookingproj.exception.JwtTokenExpiredException;
import sit.int221.bookingproj.exception.TokenInvalidException;
import sit.int221.bookingproj.repositories.UserRepository;

import javax.servlet.ServletException;
import java.util.Calendar;
import java.util.Date;

@Service
public class TokenService {
    @ExceptionHandler(TokenInvalidException.class)
    public void handleTokenInvalidException() {}

    @ExceptionHandler(JwtTokenExpiredException.class)
    public void handleJwtTokenExpiredException() {}

    @Autowired
    public UserRepository userRepository;

    public String tokenize(UserLoginDto userLoginDto){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 30);
        Date expiresAt = calendar.getTime();
        User user = userRepository.findUserByEmail(userLoginDto.getEmail());
        Algorithm algorithm = Algorithm.HMAC256("oasipLnwzaSecret");
        return JWT.create().withIssuer("BackendService").withClaim("userId", user.getUserId())
                .withClaim("name", user.getName())
                .withClaim("role", user.getRole())
                .withExpiresAt(expiresAt)
                .sign(algorithm);
    };

    public String tokenizeRefreshToken(UserLoginDto userLoginDto){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 24);
        Date expiresAt = calendar.getTime();
        User user = userRepository.findUserByEmail(userLoginDto.getEmail());
        Algorithm algorithm = Algorithm.HMAC256("oasipLnwzaSecret");
        return JWT.create().withIssuer("BackendService").withClaim("userId", user.getUserId())
                .withClaim("name", user.getName())
                .withClaim("role", user.getRole())
                .withExpiresAt(expiresAt)
                .sign(algorithm);
    };

    public DecodedJWT verify(String token) throws JwtTokenExpiredException{
            JWTVerifier verifier = JWT.require(algorithm())
                    .withIssuer("BackendService")
                    .build();
            try {
                verifier.verify(token);
                if(verifier.verify(token) != null){
                    return verifier.verify(token);
                }
            }
            catch (TokenExpiredException ex){
                throw new JwtTokenExpiredException("Expired Token Please Use Refresh Token for get new Access Token");
            }

            return null;
    }

    private Algorithm algorithm() {
        return Algorithm.HMAC256("oasipLnwzaSecret");
    }

}
