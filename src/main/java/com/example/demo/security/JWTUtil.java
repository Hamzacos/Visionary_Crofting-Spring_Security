package com.example.demo.security;

public class JWTUtil {

    public static final String SECRET = "mySeceret123";
    public static final String AUTH8HEADER = "Authorization";
    public static final String PREFIX = "Bearer ";
    public static final long EXPIRE_TOKEN = 2*60*1000;
    public static final long REFRESH_TOKEN = 15*60*1000;
}
