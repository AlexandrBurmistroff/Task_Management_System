package com.burmistrov.task.management.system.mapper;

import com.burmistrov.task.management.system.dto.user.IncomeUserDto;
import com.burmistrov.task.management.system.dto.user.ShortUserDto;
import com.burmistrov.task.management.system.dto.user.UserDto;
import com.burmistrov.task.management.system.entity.User;
import com.burmistrov.task.management.system.enums.Roles;
import lombok.experimental.UtilityClass;

import java.util.HashSet;

@UtilityClass
public class UserMapper {

    public User toUser(IncomeUserDto incomeUserDto) {
        return User.builder()
                .username(incomeUserDto.getUsername())
                .email(incomeUserDto.getEmail())
                .password(incomeUserDto.getPassword())
                .roles(new HashSet<>(Roles.USER.ordinal()))
                .build();
    }

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    public ShortUserDto toShortUserDto(User user) {
        return ShortUserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
