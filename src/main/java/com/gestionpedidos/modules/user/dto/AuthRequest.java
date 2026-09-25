package com.gestionpedidos.modules.user.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}