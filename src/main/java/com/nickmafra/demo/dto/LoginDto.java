package com.nickmafra.demo.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginDto {
    @NotEmpty
    private String login;
    @NotEmpty
    private String senha;
}
