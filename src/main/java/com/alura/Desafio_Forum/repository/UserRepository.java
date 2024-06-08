package com.alura.Desafio_Forum.repository;

import com.alura.Desafio_Forum.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByLogin(String username);

    boolean existsByLogin(String login);
}
