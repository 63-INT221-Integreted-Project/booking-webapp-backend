package sit.int221.bookingproj.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.HandlerExceptionResolver;
import sit.int221.bookingproj.exception.TokenInvalidException;
import sit.int221.bookingproj.services.TokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

public class TokenFilter extends GenericFilterBean {

    @ExceptionHandler(TokenInvalidException.class)
    public void handleTokenInvalidException(){}


    private final TokenService tokenService;

    public TokenFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String authorization = request.getHeader("Authorization");

            if (ObjectUtils.isEmpty(authorization)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            if (!authorization.startsWith("Bearer ")) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            String token = authorization.substring(7);
            DecodedJWT decoded = tokenService.verify(token);


        if (decoded == null) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }


            String userId = decoded.getClaim("userId").toString();
            String name = decoded.getClaim("name").asString();
            String role = decoded.getClaim("role").asString();

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(role.toLowerCase()));
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, "(protected)", authorities);
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authentication);


        filterChain.doFilter(servletRequest, servletResponse);
    }

}