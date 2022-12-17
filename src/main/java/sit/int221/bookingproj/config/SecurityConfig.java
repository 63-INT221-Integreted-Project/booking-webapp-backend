package sit.int221.bookingproj.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import sit.int221.bookingproj.services.TokenService;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private FilterChainExceptionHandler filterChainExceptionHandler;

    private final TokenService tokenService;

    private final String[] PUBLIC = {
            // ในนี้คือไม่ต้องใช้ token ยืนยัน
            "/api/auth/login",
            "/api/auth/login-azure",
//            "/api/events",
//            "/api/events/",
//            "/api/auth/match",
            "/api/auth/token/guest",
            "/api/auth/register",
            "/uploadFile",
            "/downloadFile/**",
//            "/api/events/**",
//            "/api/event-categories",
            "/api/event-categories/guest",
    };

    public SecurityConfig(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(filterChainExceptionHandler, LogoutFilter.class);
        http.authorizeHttpRequests(authorization -> {
                    authorization
                            .antMatchers("/api/users", "/api/users/").hasAnyAuthority("admin")
//                    .antMatchers(HttpMethod.POST, "/api/users").hasAnyAuthority("admin","student", "lecturer")
//                    .antMatchers(HttpMethod.PATCH, "/api/users").hasAnyAuthority("admin","student", "lecturer")
//                    .antMatchers(HttpMethod.DELETE, "/api/users").hasAnyAuthority("admin","student", "lecturer")
                            .antMatchers(HttpMethod.POST, "/api/events", "/api/events/").permitAll()
                            .antMatchers(HttpMethod.PATCH, "/api/events", "/api/events/").permitAll()
                            .antMatchers(HttpMethod.GET, "/api/events/search").hasAnyAuthority("admin")
                            .antMatchers( "/api/events", "/api/events/").hasAnyAuthority("admin","student", "lecturer")
                            .antMatchers("/api/event-categories").hasAnyAuthority("admin","student", "lecturer")
                            .antMatchers("/api/event-categories/").hasAnyAuthority("admin","student", "lecturer")
                            .antMatchers("/api/auth/match").hasAnyAuthority("admin")
                            //                .mvcMatchers("/api/events/").hasAnyAuthority("student")
                            .antMatchers("/**").permitAll()
                            .anyRequest().denyAll();
                }

        );

        http.cors(config -> {
                    CorsConfiguration cors = new CorsConfiguration();
                    cors.setAllowCredentials(true);
                    cors.setAllowedOriginPatterns(Collections.singletonList("https://*"));
                    cors.addAllowedHeader("*");
                    cors.addAllowedMethod("GET");
                    cors.addAllowedMethod("POST");
                    cors.addAllowedMethod("PATCH");
                    cors.addAllowedMethod("PUT");
                    cors.addAllowedMethod("DELETE");
                    cors.addAllowedMethod("OPTIONS");
                    cors.addAllowedMethod(HttpMethod.PATCH);
                    cors.addAllowedMethod(HttpMethod.POST);
                    cors.addAllowedMethod(HttpMethod.GET);
                    cors.addAllowedMethod(HttpMethod.DELETE);
                    cors.addAllowedMethod(HttpMethod.PUT);
                    cors.addAllowedMethod(HttpMethod.HEAD);
                    cors.addAllowedMethod(HttpMethod.OPTIONS);
                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    source.registerCorsConfiguration("/**", cors);
                    config.configurationSource(source);
                }).csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests().antMatchers(PUBLIC).anonymous()
                .anyRequest().authenticated()
                .and().apply(new TokenFilterConfigurer(tokenService));
        // add for azure ad
//                .and()
//                .oauth2ResourceServer().jwt()
        http.exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
    }
}