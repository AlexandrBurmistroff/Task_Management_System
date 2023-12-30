package com.burmistrov.task.management.system.service;

import com.burmistrov.task.management.system.dto.user.NewUserDto;
import com.burmistrov.task.management.system.dto.user.UserDto;

public interface UserService {

    UserDto addUser(NewUserDto newUserDto);
}
