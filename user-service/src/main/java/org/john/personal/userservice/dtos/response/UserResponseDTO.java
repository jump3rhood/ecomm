package org.john.personal.userservice.dtos.response;

import lombok.Getter;
import lombok.Setter;
import org.john.personal.userservice.models.Role;
import org.john.personal.userservice.models.UserEntity;

import java.util.List;

@Getter
@Setter
public class UserResponseDTO {
    private String email;
    private String firstName;
    private String lastName;
    private List<Role> roles;

    public static UserResponseDTO from(UserEntity user) {
        UserResponseDTO dto = new UserResponseDTO();
        user.setEmail(user.getEmail());
        user.setFirstName(user.getFirstName());
        user.setLastName(user.getLastName());
        user.setRoles(user.getRoles());
        return dto;
    }
}
