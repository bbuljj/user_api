package com.example.test.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
public class UserRequest {
    @NotNull(message = "email을 입력해주세요.")
    @Email(message = "email 형식을 사용해주세요.")
    private String email;

    @NotNull(message = "비밀번호를 입력해주세요.")
    @Size(min = 1, max = 50, message = "비밀번호는 최소 1글자에서 50글자 사이 입력 가능합니다.")
    private String password;

    @NotNull(message = "이름을 입력해주세요.")
    @Size(min = 1, max = 20, message = "이름은 최소 1글자에서 50글자 사이 입력 가능합니다.")
    private String name;
}
