package sit.int221.bookingproj.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sit.int221.bookingproj.dtos.UserAzureDto;
import sit.int221.bookingproj.dtos.UserLoginDto;
import sit.int221.bookingproj.entities.User;
import sit.int221.bookingproj.exception.JwtTokenExpiredException;
import sit.int221.bookingproj.exception.TokenInvalidException;
import sit.int221.bookingproj.repositories.UserRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

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

    public String tokenizeGuestToken(User user){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 30);
        Date expiresAt = calendar.getTime();
        Algorithm algorithm = Algorithm.HMAC256("oasipLnwzaSecret");
        return JWT.create().withIssuer("BackendService").withClaim("userId", user.getUserId())
                .withClaim("name", user.getName())
                .withClaim("role", user.getRole())
                .withExpiresAt(expiresAt)
                .sign(algorithm);
    };

    public String tokenizeToken(UserAzureDto userAzureDto){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 30);
        Date expiresAt = calendar.getTime();
        Optional<User> user = Optional.ofNullable(userRepository.findUserByEmail(userAzureDto.getEmail()));
        if(user.isEmpty()){
            createUserToken(userAzureDto);
            user = Optional.ofNullable(userRepository.findUserByEmail(userAzureDto.getEmail()));
            Algorithm algorithm = Algorithm.HMAC256("oasipLnwzaSecret");
            return JWT.create().withIssuer("BackendService").withClaim("userId", user.get().getUserId())
                    .withClaim("name", user.get().getName())
                    .withClaim("role", user.get().getRole())
                    .withExpiresAt(expiresAt)
                    .sign(algorithm);
        }
        else{
            Algorithm algorithm = Algorithm.HMAC256("oasipLnwzaSecret");
            return JWT.create().withIssuer("BackendService").withClaim("userId", user.get().getUserId())
                    .withClaim("name", user.get().getName())
                    .withClaim("role", user.get().getRole())
                    .withExpiresAt(expiresAt)
                    .sign(algorithm);
        }
    };

    public void createUserToken(UserAzureDto userAzureDto){
        Argon2 argon2 = Argon2Factory.create(
                Argon2Factory.Argon2Types.ARGON2id,
                16,
                16);
        User user = new User();
        user.setEmail(userAzureDto.getEmail().trim().toLowerCase());
        user.setName(userAzureDto.getName().trim());
        user.setRole(userAzureDto.getRole().trim().toLowerCase());
        String password = "microsoftazuread";
        String hash = argon2.hash(22, 65536, 1, password);
        user.setPassword(hash);
        userRepository.save(user);
    }

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

    public String tokenizeRefreshGuestToken(User user){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 24);
        Date expiresAt = calendar.getTime();
        Algorithm algorithm = Algorithm.HMAC256("oasipLnwzaSecret");
        return JWT.create().withIssuer("BackendService").withClaim("userId", user.getUserId())
                .withClaim("name", user.getName())
                .withClaim("role", user.getRole())
                .withExpiresAt(expiresAt)
                .sign(algorithm);
    };

    public String tokenizeRefreshTokenAzure(UserAzureDto userAzureDto){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 24);
        Date expiresAt = calendar.getTime();
        User user = userRepository.findUserByEmail(userAzureDto.getEmail());
        Algorithm algorithm = Algorithm.HMAC256("oasipLnwzaSecret");
        return JWT.create().withIssuer("BackendService").withClaim("userId", user.getUserId())
                .withClaim("name", user.getName())
                .withClaim("role", user.getRole())
                .withExpiresAt(expiresAt)
                .sign(algorithm);
    };

    public DecodedJWT verify(String token) throws JwtTokenExpiredException {
        try {
            JWTVerifier verifier = JWT.require(algorithm())
                    .withIssuer("BackendService")
                    .build();
            try {
                verifier.verify(token);
                if (verifier.verify(token) != null) {
                    return verifier.verify(token);
                }
            } catch (TokenExpiredException ex) {
                throw new JwtTokenExpiredException("Expired Token Please Use Refresh Token for get new Access Token");
            }

        } catch (Exception e) {

        }
        return null;
    }

    private Algorithm algorithm() {
        return Algorithm.HMAC256("oasipLnwzaSecret");
    }



}
