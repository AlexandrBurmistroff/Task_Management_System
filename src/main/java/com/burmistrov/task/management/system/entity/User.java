package com.burmistrov.task.management.system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String username;

    @Column
    private String email;

    @Column
    private String password;

    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Task task_id;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private Collection<Task> tasks;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private Collection<Comment> comments;
}
