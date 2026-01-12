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

    //TODO toDTO(Task entity)


}
