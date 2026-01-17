package com.taskmanager.mapper;

import com.taskmanager.model.dto.TaskDTO;
import com.taskmanager.model.entity.Task;
import com.taskmanager.model.enums.TaskStatus;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toEntity(TaskDTO dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus() != null ? dto.getStatus() : TaskStatus.TODO);
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
