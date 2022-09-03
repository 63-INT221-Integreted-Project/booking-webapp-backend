package sit.int221.bookingproj.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.filter.GenericFilterBean;
import sit.int221.bookingproj.exception.TokenInvalidException;
import sit.int221.bookingproj.services.TokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TokenFilter extends GenericFilterBean {

    @ExceptionHandler(TokenInvalidException.class)
    public void handleTokenInvalidException(){}

    private final TokenService tokenService;

    public TokenFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

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

            // user id
            String userId = decoded.getClaim("userId").asString();
            String name = decoded.getClaim("name").asString();
            String role = decoded.getClaim("role").asString();

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(name));
            authorities.add(new SimpleGrantedAuthority(role));
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, "(protected)", authorities);
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authentication);


        filterChain.doFilter(servletRequest, servletResponse);
    }

}