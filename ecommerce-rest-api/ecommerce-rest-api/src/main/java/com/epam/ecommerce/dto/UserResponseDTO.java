package com.epam.ecommerce.dto;
import com.epam.ecommerce.model.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long userId;
    private String username;
    private String email;
    private Role role;
    private boolean deleted;
}