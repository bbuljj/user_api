package com.example.test.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    Page<User> findAllByNameLike(String name, Pageable pageable);
    Optional<User> findUserByIdAndIsActiveIsTrue(Long id);
}