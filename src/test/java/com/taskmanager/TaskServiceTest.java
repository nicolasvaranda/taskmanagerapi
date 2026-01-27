package com.taskmanager;

import com.taskmanager.mapper.TaskMapper;
import com.taskmanager.model.dto.TaskDTO;
import com.taskmanager.model.entity.Task;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private TaskDTO taskDTO;
    private Task taskEntity;

    @BeforeEach
    void setUp() {
        taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        taskDTO.setTitle("Test Task");
        taskDTO.setDescription("This is a test task.");
        taskDTO.setCreatedAt(LocalDateTime.now());
        taskDTO.setUserId(1L);

        taskEntity = new Task();
        taskEntity.setId(1L);
        taskEntity.setTitle("Test Task");
        taskEntity.setDescription("This is a test task.");
        taskEntity.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void shouldCreateTaskSuccessfully() {
        //given
        when(userRepository.findById(taskDTO.getUserId())).thenReturn(Optional.empty()); // Mock user retrieval
        when(taskMapper.toEntity(taskDTO)).thenReturn(taskEntity);
        when(taskRepository.save(taskEntity)).thenReturn(taskEntity);
        when(taskMapper.toDTO(taskEntity)).thenReturn(taskDTO);
        //when
        TaskDTO result = taskService.createTask(taskDTO);
        //then
        assertNotNull(result);
        assertEquals(taskDTO.getId(), result.getId());
        verify(userRepository, times(1)).findById(taskDTO.getUserId());
        verify(taskMapper, times(1)).toEntity(taskDTO);
        verify(taskRepository, times(1)).save(taskEntity);
        verify(taskMapper, times(1)).toDTO(taskEntity);
    }
}
