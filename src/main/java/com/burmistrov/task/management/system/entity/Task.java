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
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "creator_id")
    private User creator;

    //    @OneToMany(mappedBy = "task")
//    private Collection<User> executor_id;
    @ManyToOne
    @JoinColumn(name = "executor_id")
    private User executor;

//    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
//    private Collection<Comment> comments;
}
