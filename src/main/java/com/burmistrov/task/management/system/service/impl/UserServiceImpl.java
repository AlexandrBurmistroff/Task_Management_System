package com.burmistrov.task.management.system.service.impl;

import com.burmistrov.task.management.system.dto.user.IncomeUserDto;
import com.burmistrov.task.management.system.dto.user.UserDto;
import com.burmistrov.task.management.system.entity.Role;
import com.burmistrov.task.management.system.entity.User;
import com.burmistrov.task.management.system.exception.ConflictException;
import com.burmistrov.task.management.system.exception.NotFoundException;
import com.burmistrov.task.management.system.mapper.UserMapper;
import com.burmistrov.task.management.system.repository.RoleRepository;
import com.burmistrov.task.management.system.repository.UserRepository;
import com.burmistrov.task.management.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto addUser(IncomeUserDto incomeUserDto) {
        Optional<Role> userRole = roleRepository.findByName("USER");
        Role role = userRole.orElseThrow(() -> new RuntimeException("Default user role not found"));

        if (userRepository.existsByEmail(incomeUserDto.getEmail())) {
            throw new ConflictException("User with email address: " + incomeUserDto.getEmail() + " already exists");
        }
        User user = UserMapper.toUser(incomeUserDto);
        user.setPassword(passwordEncoder.encode(incomeUserDto.getPassword()));
        user.setRoles(Collections.singleton(role));
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public UserDetails loadUserByEmail(String email) {
        User user = findUserByEmail(email).orElseThrow(() ->
                new NotFoundException("User with email address: " + email + " not found"));
        ;
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UsernameNotFoundException("Method not implemented");
    }
}
