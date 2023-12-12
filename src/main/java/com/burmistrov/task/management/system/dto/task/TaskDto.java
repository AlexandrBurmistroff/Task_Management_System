package com.burmistrov.task.management.system.dto.task;

import com.burmistrov.task.management.system.dto.user.ShortUserDto;
import com.burmistrov.task.management.system.enums.Priority;
import com.burmistrov.task.management.system.enums.Status;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDto {

    private long id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private ShortUserDto creator;
    private ShortUserDto executor;
}
