package com.taskmanager.service;

import com.taskmanager.mapper.TaskMapper;
import com.taskmanager.model.dto.TaskDTO;
import com.taskmanager.model.entity.Task;
import com.taskmanager.model.entity.User;
import com.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
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
}
