package com.burmistrov.task.management.system.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewTaskDto {

    @NotNull(message = "Creator Id cannot be empty")
    private long creator_id;

    @NotBlank(message = "Title cannot be empty")
    @Size(min = 3, max = 155, message = "Heading size must be from 3 to 155 characters")
    private String title;

    @NotBlank(message = "Description cannot be empty")
    @Size(min = 20, max = 1000, message = "The description size must be from 20 to 1000 characters")
    private String description;

    @NotBlank(message = "Status cannot be empty")
    private String status;

    @NotBlank(message = "Priority cannot be empty")
    private String priority;

    private Long executor_id;
}
