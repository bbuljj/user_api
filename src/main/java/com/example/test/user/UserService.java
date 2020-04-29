package com.example.test.user;

import com.example.test.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class UserService {

    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User create(User user) {
        user.setCreatedAt(LocalDateTime.now());
        return repository.save(user);
    }

    public Page<User> list(String query, Pageable page) {
        if (Objects.nonNull(query)) {
            return repository.findAllByNameLike(query, page);
        }
        return repository.findAll(page);
    }

    public User retrieve(Long id) {
        return repository.findUserByIdAndIsActiveIsTrue(id)
                .orElseThrow(() -> new NotFoundException("탈퇴했거나 존재하지 않 사용자입니다."));
    }

    public User update(User user) {
        this.retrieve(user.getId());
        user.setUpdatedAt(LocalDateTime.now());

        return repository.save(user);
    }

    public User delete(User user) {
        user.setIsActive(false);
        user.setDeletedAt(LocalDateTime.now());

        return repository.save(user);
    }
}
