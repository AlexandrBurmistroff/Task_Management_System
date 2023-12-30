package com.burmistrov.task.management.system.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCommentDto {

    @NotBlank(message = "Comment cannot be empty")
    @Size(min = 5, max = 200)
    private String comment;
}
