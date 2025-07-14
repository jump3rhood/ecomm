package org.john.personal.userservice.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDto {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
}
