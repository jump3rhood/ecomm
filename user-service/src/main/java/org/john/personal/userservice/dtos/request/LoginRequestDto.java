package org.john.personal.userservice.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    private String username; // email
    private String password;
}
