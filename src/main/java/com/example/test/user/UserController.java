package com.example.test.user;

import com.example.test.exception.NoContentException;
import com.example.test.exception.ValidatorException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    private UserService userService;
    private ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidatorException(bindingResult);
        }

        User user = modelMapper.map(dto, User.class);
        User savedUser = userService.create(user);
        UserResponse response = this.userToResponse(savedUser);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<Page<User>> list(@RequestParam(required = false) String query, Pageable pageable) {
        Page<User> users = userService.list(query, pageable);
        if (users.isEmpty()) {
            throw new NoContentException();
        }

        return ResponseEntity
                .ok()
                .body(users);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserResponse> retrieve(@PathVariable Long id) {
        User user = userService.retrieve(id);

        UserResponse response = this.userToResponse(user);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody @Valid UserRequest dto) {
        User user = modelMapper.map(dto, User.class);
        user.setId(id);
        User updatedUser = userService.update(user);

        UserResponse response = this.userToResponse(updatedUser);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UserResponse> delete(@PathVariable Long id) {
        User user = userService.retrieve(id);

        User deletedUser = userService.delete(user);

        return ResponseEntity
                .ok()
                .body(this.userToResponse(deletedUser));
    }

    private UserResponse userToResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }
}
