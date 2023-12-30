package com.burmistrov.task.management.system.repository;

import com.burmistrov.task.management.system.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findById(long id);

    @Override
    Page<Task> findAll(Pageable pageable);

    List<Task> findTasksByCreatorIdOrderByPriorityDesc(long creatorId, Pageable pageable);

    List<Task> findTasksByCreatorIdOrderByStatusDesc(long creatorId, Pageable pageable);

    List<Task> findTasksByExecutorIdOrderByPriorityDesc(long executorId, Pageable pageable);

    List<Task> findTasksByExecutorIdOrderByStatusDesc(long executorId, Pageable pageable);
}
