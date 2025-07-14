package org.john.personal.userservice.dtos.response;

import lombok.Getter;
import lombok.Setter;
import org.john.personal.userservice.models.UserEntity;

@Getter
@Setter
public class UserResponseDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String role;

    public static UserResponseDTO from(UserEntity user) {
        UserResponseDTO dto = new UserResponseDTO();
        user.setUsername(user.getUsername());
        user.setFirstName(user.getFirstName());
        user.setLastName(user.getLastName());
        user.setRole(user.getRole());
        return dto;
    }
}
