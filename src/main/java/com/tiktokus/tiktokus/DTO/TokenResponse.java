package com.tiktokus.tiktokus.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse {

    private String accessToken;
    private String resfreshToken;
    private UserDTO userDTO;
}

