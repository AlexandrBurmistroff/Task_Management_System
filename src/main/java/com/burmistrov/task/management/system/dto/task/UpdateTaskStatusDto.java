package com.burmistrov.task.management.system.dto.task;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateTaskStatusDto {

    private String status;
}
