package com.tiktokus.tiktokus.DTO;

import com.tiktokus.tiktokus.Enum.RoleUser;
import com.tiktokus.tiktokus.Enum.UserStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private RoleUser role;
    private UserStatus status;
    private float balance;

}
