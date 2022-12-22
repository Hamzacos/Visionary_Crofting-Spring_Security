package com.example.demo.rest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.security.JWTUtil;
import com.example.demo.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AccountServiceController {
    @Autowired
    private AccountService accountService;

    @GetMapping(path = "/users")
    @PostAuthorize("hasAnyAuthority('User')")
    public List<User> userList(){
        return accountService.listUser();
    }

    @PostMapping(path = "/users")
    @PostAuthorize("hasAnyAuthority('Admin')")
    User saveUser(@RequestBody  User user){
        return accountService.addNewUser(user);
    }

    @PostMapping(path = "/roles")
    //@PostAuthorize("hasAnyAuthority('ADMIN')")
    Role saveRole(@RequestBody Role role){
        return accountService.addNewRole(role);
    }
    @PostMapping(path = "/addRoleToUser")
    //@PostAuthorize("hasAnyAuthority('ADMIN')")
    public String addRoleToUser(@RequestBody RoleUserForm roleUserForm){
        accountService.addRoleToUser(roleUserForm.getUsername(),roleUserForm.getRoleName());
        return "{message succes}";
    }

    @GetMapping(path = "/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws  Exception{
        String authToken = request.getHeader(JWTUtil.AUTH8HEADER);
        if(authToken != null && authToken.startsWith(JWTUtil.PREFIX)){
            try{
                String refreshToken = authToken.substring(JWTUtil.PREFIX.length());
                Algorithm algorithm = Algorithm.HMAC256(JWTUtil.SECRET);
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                User user = accountService.loadUserByUsername(username);
                String JwtAccessToken = JWT.create().withSubject((user.getUsername()))
                        .withExpiresAt(new Date(System.currentTimeMillis() + JWTUtil.EXPIRE_TOKEN))
                        .withIssuer((request.getRequestURL().toString()))
                        .withClaim("roles",user.getUserRoles().stream().map(r -> r.getRoleName()).collect(Collectors.toList()))
                        .sign((algorithm));

                Map<String,String> idToken =new HashMap<>();
                idToken.put("accesse-token",JwtAccessToken);
                idToken.put("resresh-token",refreshToken);
                response.setContentType(("application/json"));
                new ObjectMapper().writeValue(response.getOutputStream(),idToken);

            }catch (Exception e){

                response.setHeader("error-message",e.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        }else{
            throw new RuntimeException(("Refresh Token required !!"));
        }

    }

    @GetMapping(path="/profile")
    public User profile(Principal principal){
        return accountService.loadUserByUsername(principal.getName());
    }

}

@Data
class RoleUserForm{
    private String username;
    private String roleName;
}
