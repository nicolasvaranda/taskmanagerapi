package com.taskmanager.model.dto;

import com.taskmanager.model.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private Long id;
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200)
    private String title;
    @Size(max = 300)
    private String description;
    private TaskStatus status;
    @NotNull(message = "User ID is required")
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
