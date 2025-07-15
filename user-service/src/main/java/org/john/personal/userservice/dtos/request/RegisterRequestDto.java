package org.john.personal.userservice.dtos.request;

import lombok.Getter;
import lombok.Setter;
import org.john.personal.userservice.models.Role;

import java.util.List;


@Getter
@Setter
public class RegisterRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<Role> roles;
}
