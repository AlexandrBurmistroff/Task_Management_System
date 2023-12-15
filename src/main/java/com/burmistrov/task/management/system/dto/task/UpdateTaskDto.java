package com.burmistrov.task.management.system.dto.task;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateTaskDto {

    @Size(min = 3, max = 155, message = "Heading size must be from 3 to 155 characters")
    private String title;

    @Size(min = 3, max = 1000, message = "The description size must be from 3 to 1000 characters")
    private String description;

    private String status;

    private String priority;

    private Long executor_id;
}
