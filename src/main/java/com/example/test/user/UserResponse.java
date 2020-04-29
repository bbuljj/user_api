package com.example.test.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class UserResponse extends RepresentationModel<UserResponse> {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Boolean isActive;
}
