package com.burmistrov.task.management.system.entity;

import com.burmistrov.task.management.system.enums.Priority;
import com.burmistrov.task.management.system.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Collection;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status; // Status

    @Enumerated(EnumType.STRING)
    private Priority priority; //Priority

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User creator_id;

    //    @OneToMany(mappedBy = "task")
//    private Collection<User> executor_id;
    @ManyToOne
    private User executor_id;

//    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
//    private Collection<Comment> comments;
}
