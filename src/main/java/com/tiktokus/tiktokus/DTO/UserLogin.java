package com.tiktokus.tiktokus.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLogin {

    private  String email;
    private String password;
}
