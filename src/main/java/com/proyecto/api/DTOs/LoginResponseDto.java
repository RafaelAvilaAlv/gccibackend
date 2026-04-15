package com.proyecto.api.DTOs;

public class LoginResponseDto {

    private String token;
    private String email;
    private String rol;

    public LoginResponseDto() {
    }

    public LoginResponseDto(String token, String email, String rol) {
        this.token = token;
        this.email = email;
        this.rol = rol;
    }

    // getters y setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}