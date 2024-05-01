package com.stiven.rumahhangeul.dto;

import com.stiven.rumahhangeul.entity.UserProjection;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthDto {
    private String message;
    private UserProjection userProjection;
}
