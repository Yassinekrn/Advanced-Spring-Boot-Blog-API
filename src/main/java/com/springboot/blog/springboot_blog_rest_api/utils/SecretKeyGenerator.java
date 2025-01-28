package com.springboot.blog.springboot_blog_rest_api.utils;

import java.util.Base64;

public class SecretKeyGenerator {
    public static void main(String[] args) {
        byte[] key = io.jsonwebtoken.Jwts.SIG.HS512.key().build().getEncoded();
        System.out.println(Base64.getEncoder().encodeToString(key));
    }
}
