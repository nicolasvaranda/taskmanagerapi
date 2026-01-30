package com.taskmanager;

import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.mapper.TaskMapper;
import com.taskmanager.model.dto.TaskDTO;
import com.taskmanager.model.entity.Task;
import com.taskmanager.model.entity.User;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.service.TaskService;
import com.taskmanager.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskService taskService;

    private TaskDTO taskDTO;
    private Task taskEntity;
    private User userEntity;

    @BeforeEach
    void setUp() {
        taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        taskDTO.setTitle("Test Task");
        taskDTO.setDescription("This is a test task.");
        taskDTO.setUserId(1L);

        userEntity = new User();
        userEntity.setId(1L);
        userEntity.setName("Test User");

        taskEntity = new Task();
        taskEntity.setId(1L);
        taskEntity.setTitle("Test Task");
        taskEntity.setDescription("This is a test task.");
        taskEntity.setUser(userEntity);

    }

    @Test
    void shouldCreateTaskSuccessfully() {
        // given
        when(userService.findUserEntityById(taskDTO.getUserId()))
                .thenReturn(userEntity);
        when(taskMapper.toEntity(taskDTO))
                .thenReturn(taskEntity);
        when(taskRepository.save(any(Task.class)))
                .thenReturn(taskEntity);
        when(taskMapper.toDTO(taskEntity))
                .thenReturn(taskDTO);

        // when
        TaskDTO result = taskService.createTask(taskDTO);

        // then
        assertNotNull(result);
        assertEquals(taskDTO.getId(), result.getId());

        verify(userService).findUserEntityById(taskDTO.getUserId());
        verify(taskMapper).toEntity(taskDTO);
        verify(taskRepository).save(any(Task.class));
        verify(taskMapper).toDTO(taskEntity);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundWhileCreatingTask() {
        //given
        when(userService.findUserEntityById(taskDTO.getUserId()))
                .thenThrow(new ResourceNotFoundException("User not found with id: " + taskDTO.getUserId()));
        //when e then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.createTask(taskDTO)
        );
        assertEquals("User not found with id: " + taskDTO.getUserId(), exception.getMessage());
        verify(userService).findUserEntityById(taskDTO.getUserId());
        verify(taskMapper, never()).toEntity(any());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void shouldFindTaskByIdSuccessfully() {
        //given
        when(taskRepository.findById(taskDTO.getId()))
                .thenReturn(Optional.of(taskEntity));
        when(taskMapper.toDTO(taskEntity))
                .thenReturn(taskDTO);
        //when
        TaskDTO result = taskService.findById(taskDTO.getId());
        //then
        assertNotNull(result);
        assertEquals(taskDTO.getId(), result.getId());
        verify(taskRepository).findById(taskDTO.getId());
        verify(taskMapper).toDTO(taskEntity);
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFoundById() {
        //given
        when(taskRepository.findById(taskDTO.getId())).thenReturn(Optional.empty());
        //when e then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.findById(taskDTO.getId())
        );
        assertEquals("Task not found with id: " + taskDTO.getId(), exception.getMessage());
        verify(taskRepository).findById(taskDTO.getId());
        verify(taskMapper, never()).toDTO(any());
    }

    @Test
    void shouldUpdateTaskSuccessfully() {
        //given
        when(taskRepository.findById(taskDTO.getId())).thenReturn(Optional.of(taskEntity));
        when(taskRepository.save(taskEntity)).thenReturn(taskEntity);
        when(taskMapper.toDTO(taskEntity)).thenReturn(taskDTO);
        //when
        TaskDTO result = taskService.updateTask(taskDTO.getId(), taskDTO);
        //then
        assertNotNull(result);
        assertEquals(taskDTO.getId(), result.getId());
        assertEquals(taskDTO.getTitle(), result.getTitle());

        verify(taskRepository).findById(taskDTO.getId());
        verify(taskMapper).updateEntity(taskEntity, taskDTO);
        verify(taskRepository).save(taskEntity);
        verify(taskMapper).toDTO(taskEntity);
    }



}
