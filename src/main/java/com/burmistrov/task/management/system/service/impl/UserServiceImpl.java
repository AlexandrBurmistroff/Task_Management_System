package com.burmistrov.task.management.system.service.impl;

import com.burmistrov.task.management.system.dto.user.NewUserDto;
import com.burmistrov.task.management.system.dto.user.UserDto;
import com.burmistrov.task.management.system.entity.Role;
import com.burmistrov.task.management.system.entity.User;
import com.burmistrov.task.management.system.exception.ConflictException;
import com.burmistrov.task.management.system.mapper.UserMapper;
import com.burmistrov.task.management.system.repository.RoleRepository;
import com.burmistrov.task.management.system.repository.UserRepository;
import com.burmistrov.task.management.system.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

/**
 * Service for working with users
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Method for adding a new user to the database
     * @param newUserDto includes: username, email, password
     * @return UserDto(id, username, email)
     */
    @Override
    public UserDto addUser(NewUserDto newUserDto) {
        Optional<Role> userRole = roleRepository.findByName("USER");
        Role role = userRole.orElseThrow(() -> new RuntimeException("Default user role not found"));

        if (userRepository.existsByEmail(newUserDto.getEmail())) {
            throw new ConflictException("User with email address: " + newUserDto.getEmail() + " already exists");
        }
        User user = UserMapper.toUser(newUserDto);
        user.setPassword(passwordEncoder.encode(newUserDto.getPassword()));
        user.setRoles(Collections.singleton(role));
        log.info("Create new user");
        return UserMapper.toUserDto(userRepository.save(user));
    }
}
