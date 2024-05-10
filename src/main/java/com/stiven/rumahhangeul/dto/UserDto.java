package com.stiven.rumahhangeul.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String namaDepan;
    private String namaBelakang;
    private String password;
    private String email;
    private Long score;
    private Long point;
    private String newBorder;
    private String newProfile;

}
