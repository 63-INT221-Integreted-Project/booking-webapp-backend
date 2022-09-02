package sit.int221.bookingproj.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sit.int221.bookingproj.dtos.UserLoginDto;
import sit.int221.bookingproj.entities.User;
import sit.int221.bookingproj.repositories.UserRepository;

@Service
public class TokenService {
    @Autowired
    public UserRepository userRepository;
    public String tokenize(UserLoginDto userLoginDto){
        User user = userRepository.findAllByEmail(userLoginDto.getEmail());
        Algorithm algorithm = Algorithm.HMAC256("oasipLnwzaSecret");
        return JWT.create().withIssuer("BackendService").withClaim("userId", user.getUserId())
                .withClaim("name", user.getName())
                .withClaim("role", user.getRole())
                .sign(algorithm);
    };

    public DecodedJWT verify(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm())
                    .withIssuer("BackendService")
                    .build();
            System.out.println("test versify " +  verifier.verify(token).getToken());
            return verifier.verify(token);

        } catch (Exception e) {
            return null;
        }
    }

    private Algorithm algorithm() {
        return Algorithm.HMAC256("oasipLnwzaSecret");
    }

}
