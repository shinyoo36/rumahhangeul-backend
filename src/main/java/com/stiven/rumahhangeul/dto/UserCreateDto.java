package com.stiven.rumahhangeul.dto;

import lombok.Data;
@Data

public class UserCreateDto {
    private String username;
    private String namaDepan;
    private String namaBelakang;
    private String password;
    private String email;

}
