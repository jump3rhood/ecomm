package org.john.personal.userservice.dtos.response;

import lombok.Getter;
import lombok.Setter;
import org.john.personal.userservice.models.Role;
import org.john.personal.userservice.models.UserEntity;

import java.util.ArrayList;
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
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        List<Role> roleList = new ArrayList<>(user.getRoles());
        dto.setRoles(roleList);
        return dto;
    }
}
