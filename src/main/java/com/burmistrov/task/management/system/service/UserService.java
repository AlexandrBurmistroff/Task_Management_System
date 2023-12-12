package com.burmistrov.task.management.system.service;

import com.burmistrov.task.management.system.dto.user.IncomeUserDto;
import com.burmistrov.task.management.system.dto.user.UserDto;
import com.burmistrov.task.management.system.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    UserDto addUser(IncomeUserDto incomeUserDto);

    User findByUserByEmail(String email);

    UserDetails loadUserByEmail(String email);
}
