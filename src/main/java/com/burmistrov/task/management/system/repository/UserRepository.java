package com.burmistrov.task.management.system.repository;

import com.burmistrov.task.management.system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(long id);

    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(String email);
}
