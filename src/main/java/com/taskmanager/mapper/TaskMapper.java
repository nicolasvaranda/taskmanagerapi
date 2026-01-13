package com.taskmanager.mapper;

import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.dto.TaskDTO;
import com.taskmanager.model.entity.Task;
import com.taskmanager.model.entity.User;
import com.taskmanager.model.enums.TaskStatus;
import com.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    private final UserRepository userRepository;

    public Task toEntity(TaskDTO dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        if (dto.getStatus() != null) {
            task.setStatus(dto.getStatus());
        } else {
            task.setStatus(TaskStatus.TODO);
        }
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " +
                        dto.getUserId()));
        task.setUser(user);
        return task;
    }

    public TaskDTO toDTO(Task entity) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(entity.getId());
        taskDTO.setUserId(entity.getUser().getId());
        taskDTO.setStatus(entity.getStatus());
        taskDTO.setTitle(entity.getTitle());
        taskDTO.setDescription(entity.getDescription());
        taskDTO.setCreatedAt(entity.getCreatedAt());
        taskDTO.setUpdatedAt(entity.getUpdatedAt());
        return taskDTO;
    }

    public void updateEntity(Task entity, TaskDTO dto) {
        if (dto.getTitle() != null) {
            entity.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
    }

}
