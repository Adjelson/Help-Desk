package com.helpdesk.helpdesk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Validated
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final Jwt jwt = new Jwt();
    private final Mail mail = new Mail();
    private final Upload upload = new Upload();
    private final Admin admin = new Admin();
    private final Cors cors = new Cors();
    private final Security security = new Security();

    public Jwt getJwt()           { return jwt; }
    public Mail getMail()         { return mail; }
    public Upload getUpload()     { return upload; }
    public Admin getAdmin()       { return admin; }
    public Cors getCors()         { return cors; }
    public Security getSecurity() { return security; }

    public static class Jwt {
        @NotBlank
        private String secret;
        @Positive
        private long expiration = 86400000L;

        public String getSecret()         { return secret; }
        public void setSecret(String s)   { this.secret = s; }
        public long getExpiration()       { return expiration; }
        public void setExpiration(long e) { this.expiration = e; }
    }

    public static class Mail {
        @NotBlank
        private String from;

        public String getFrom()       { return from; }
        public void setFrom(String f) { this.from = f; }
    }

    public static class Upload {
        private long maxSize = 10485760L;
        private String allowedTypes;

        public long getMaxSize()               { return maxSize; }
        public void setMaxSize(long s)         { this.maxSize = s; }
        public String getAllowedTypes()         { return allowedTypes; }
        public void setAllowedTypes(String t)  { this.allowedTypes = t; }
    }

    public static class Admin {
        private String email = "admin@helpdesk.com";
        private String senha = "Admin@123456";

        public String getEmail()        { return email; }
        public void setEmail(String e)  { this.email = e; }
        public String getSenha()        { return senha; }
        public void setSenha(String s)  { this.senha = s; }
    }

    public static class Cors {
        private String allowedOrigins = "http://localhost:3000,http://localhost:5173";

        public String getAllowedOrigins()        { return allowedOrigins; }
        public void setAllowedOrigins(String o)  { this.allowedOrigins = o; }
    }

    public static class Security {
        private int loginMaxAttempts = 5;
        private long loginBlockMinutes = 15L;

        public int getLoginMaxAttempts()               { return loginMaxAttempts; }
        public void setLoginMaxAttempts(int a)         { this.loginMaxAttempts = a; }
        public long getLoginBlockMinutes()             { return loginBlockMinutes; }
        public void setLoginBlockMinutes(long m)       { this.loginBlockMinutes = m; }
    }
}
