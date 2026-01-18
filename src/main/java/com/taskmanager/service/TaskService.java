package com.taskmanager.service;

import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.mapper.TaskMapper;
import com.taskmanager.model.dto.TaskDTO;
import com.taskmanager.model.entity.Task;
import com.taskmanager.model.entity.User;
import com.taskmanager.model.enums.TaskStatus;
import com.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserService userService;

    public TaskDTO createTask(TaskDTO dto) {
        User user = userService.findUserEntityById(dto.getUserId());
        Task task = taskMapper.toEntity(dto);
        task.setUser(user);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toDTO(savedTask);
    }

    protected Task findTaskEntityById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }

    public TaskDTO updateTask(Long id, TaskDTO dto) {
        Task taskEntity = findTaskEntityById(id);
        taskMapper.updateEntity(taskEntity, dto);
        Task savedEntity = taskRepository.save(taskEntity);
        return taskMapper.toDTO(savedEntity);
    }

    public TaskDTO findById(Long id) {
        Task task = findTaskEntityById(id);
        return taskMapper.toDTO(task);
    }

    public void deleteTask(Long id) {
        Task task = findTaskEntityById(id);
        taskRepository.delete(task);
    }

    public Page<TaskDTO> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable)
                .map(taskMapper::toDTO);
    }

    public Page<TaskDTO> findByStatus(Pageable pageable, TaskStatus status) {
        return taskRepository.findByStatus(status, pageable)
                .map(taskMapper::toDTO);
    }

    public Page<TaskDTO> findByUserId(Pageable pageable, Long userId) {
        User user = userService.findUserEntityById(userId);
        return taskRepository.findByUserId(user.getId(), pageable)
                .map(taskMapper::toDTO);
    }

    public Page<TaskDTO> findByUserIdAndStatus(Long userId, TaskStatus status, Pageable pageable) {
        User user = userService.findUserEntityById(userId);
        return taskRepository.findByUserIdAndStatus(user.getId(), status, pageable)
                .map(taskMapper::toDTO);
    }
}
