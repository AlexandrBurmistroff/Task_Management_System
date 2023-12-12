package com.burmistrov.task.management.system.dto.jwt;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class JwtRequest {

    private String email;
    private String password;
}
