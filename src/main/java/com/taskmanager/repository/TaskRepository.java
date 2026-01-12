package com.taskmanager.repository;

import com.taskmanager.model.entity.Task;
import com.taskmanager.model.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByUserId(Long userId, Pageable pageable);

    Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    Page<Task> findByUserIdAndStatus(Long userId, TaskStatus status, Pageable pageable);

    long countByUserId(Long userId);

    long countByUserIdAndStatus(Long userId, TaskStatus status);
}
