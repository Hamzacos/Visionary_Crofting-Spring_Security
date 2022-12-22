package com.example.demo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtauthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    public JwtauthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("attemptAuthentication");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username);
        System.out.println(password);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username,password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication");
        User user = (User) authResult.getPrincipal();
        Algorithm algo = Algorithm.HMAC256(JWTUtil.SECRET);
        String JwtAccessToken = JWT.create().withSubject((user.getUsername()))
                .withExpiresAt(new Date(System.currentTimeMillis() + JWTUtil.EXPIRE_TOKEN))
                .withIssuer((request.getRequestURL().toString()))
                .withClaim("roles",user.getAuthorities().stream().map(ga -> ga.getAuthority()).collect(Collectors.toList()))
                .sign((algo));


        String JwtRefreshToken = JWT.create().withSubject((user.getUsername()))
                .withExpiresAt(new Date(System.currentTimeMillis() + JWTUtil.REFRESH_TOKEN))
                .withIssuer((request.getRequestURL().toString()))
                .sign((algo));
        Map<String,String> idToken =new HashMap<>();
        idToken.put("accesse-token",JwtAccessToken);
        idToken.put("resresh-token",JwtRefreshToken);
        response.setContentType(("application/json"));
       new ObjectMapper().writeValue(response.getOutputStream(),idToken);
    }

}
