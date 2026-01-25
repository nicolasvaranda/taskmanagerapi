package com.taskmanager;

import com.taskmanager.exception.DuplicateResourceException;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.mapper.UserMapper;
import com.taskmanager.model.dto.UserDTO;
import com.taskmanager.model.dto.UserStatsDTO;
import com.taskmanager.model.entity.User;
import com.taskmanager.model.enums.TaskStatus;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private UserService userService;

    private UserDTO userDTO;
    private User userEntity;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("Teste");
        userDTO.setEmail("teste@hotmail.com");
        userDTO.setCreatedAt(LocalDateTime.now());

        userEntity = new User();
        userEntity.setId(1L);
        userEntity.setName("Teste");
        userEntity.setEmail("teste@hotmail.com");
        userEntity.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void shouldCreateUserSuccessfully() {
        //given
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);
        when(userMapper.toEntity(userDTO)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toDTO(userEntity)).thenReturn(userDTO);
        //when
        UserDTO result = userService.createUser(userDTO);
        //then
        assertNotNull(result);
        assertEquals(userDTO.getEmail(), result.getEmail());
        verify(userRepository, times(1)).existsByEmail(userDTO.getEmail());
        verify(userMapper, times(1)).toEntity(userDTO);
        verify(userRepository, times(1)).save(userEntity);
        verify(userMapper, times(1)).toDTO(userEntity);
    }

    @Test
    void shouldThrowDuplicateResourceExceptionWhenEmailAlreadyExists() {
        //given
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);
        //when e then
        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> userService.createUser(userDTO)
        );

        assertEquals("Email already exists: teste@hotmail.com", exception.getMessage());
        verify(userRepository, times(1)).existsByEmail(userDTO.getEmail());
        verify(userMapper, never()).toEntity(any());
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).toDTO(any());
    }

    @Test
    void shouldFindUserByIdSuccessfully() {
        //given
        when(userRepository.findById(userDTO.getId()))
                .thenReturn(Optional.of(userEntity));
        when(userMapper.toDTO(userEntity))
                .thenReturn(userDTO);
        //when
        UserDTO result = userService.findById(userDTO.getId());
        //then
        assertNotNull(result);
        assertEquals(userDTO.getId(), result.getId());
        verify(userRepository, times(1)).findById(userDTO.getId());
        verify(userMapper, times(1)).toDTO(userEntity);
    }

    @Test
    void shouldThrowResourceNotFoundWhenUserNotFound() {
        //given
        when(userRepository.findById(userDTO.getId()))
                .thenReturn(Optional.empty());
        //when e then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.findById(userDTO.getId())
        );

        assertEquals("User not found with id: " + userDTO.getId(), exception.getMessage());
        verify(userRepository, times(1)).findById(userDTO.getId());
        verify(userMapper, never()).toDTO(any());
    }

    @Test
    void shouldThrowExceptionWhenNewEmailAlreadyExists() {
        //given
        String newEmail = "novo@email.com";
        userDTO.setEmail(newEmail);
        when(userRepository.findById(userDTO.getId()))
                .thenReturn(Optional.of(userEntity));
        when(userRepository.existsByEmail(newEmail))
                .thenReturn(true);
        //when e then
        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> userService.updateUser(userDTO.getId(), userDTO));

        assertEquals("Email already exists" + newEmail, exception.getMessage());
        verify(userRepository, times(1)).findById(userDTO.getId());
        verify(userRepository, times(1)).existsByEmail(newEmail);
        verify(userMapper, never()).updateEntity(any(), any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundForUpdate() {
        //given
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.empty());
        //when e then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.updateUser(userDTO.getId(), userDTO)
        );

        assertEquals("User not found with id: " + userDTO.getId(), exception.getMessage());
        verify(userRepository, times(1)).findById(userDTO.getId());
        verify(userMapper, never()).updateEntity(any(), any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldUpdateUserWithSameEmail() {
        //given
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(userEntity));
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toDTO(userEntity)).thenReturn(userDTO);
        //when
        UserDTO result = userService.updateUser(userDTO.getId(), userDTO);
        //then
        assertNotNull(result);
        assertEquals(userDTO.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findById(userDTO.getId());
        verify(userMapper, times(1)).updateEntity(userEntity, userDTO);
        verify(userRepository, times(1)).save(userEntity);
        verify(userMapper, times(1)).toDTO(userEntity);
    }

    @Test
    void shouldUpdateUserSucessfuly() {
        //given
        String oldEmail = "antigo@gmail.com";
        String newEmail = "novo@gmail.com";
        String newName = "Novo Nome";
        userEntity.setEmail(oldEmail);
        userDTO.setEmail(newEmail);
        userDTO.setName(newName);
        when(userRepository.findById(userDTO.getId()))
                .thenReturn(Optional.of(userEntity));
        when(userRepository.existsByEmail(newEmail))
                .thenReturn(false);
        when(userRepository.save(userEntity))
                .thenReturn(userEntity);
        when(userMapper.toDTO(userEntity))
                .thenReturn(userDTO);
        //when
        UserDTO result = userService.updateUser(userDTO.getId(), userDTO);
        //then
        assertNotNull(result);
        assertEquals(newEmail, result.getEmail());
        assertEquals(newName, result.getName());

        verify(userRepository, times(1)).findById(userDTO.getId());
        verify(userRepository, times(1)).existsByEmail(newEmail);
        verify(userMapper, times(1)).updateEntity(userEntity, userDTO);
        verify(userRepository, times(1)).save(userEntity);
        verify(userMapper, times(1)).toDTO(userEntity);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundForDeletion() {
        //given
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.empty());
        //when e then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.deleteUser(userDTO.getId())
        );
        assertEquals("User not found with id: " + userDTO.getId(), exception.getMessage());
        verify(userRepository, times(1)).findById(userDTO.getId());
        verify(userRepository, never()).delete(any());
    }

    @Test
    void shouldDeleteUserSucessfully() {
        //given
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(userEntity));
        //when
        userService.deleteUser(userDTO.getId());
        //then
        verify(userRepository, times(1)).findById(userDTO.getId());
        verify(userRepository, times(1)).delete(userEntity);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldReturnZeroCompletionRateWhenNoTasks() {
        //given
        when(userRepository.findById(userDTO.getId()))
                .thenReturn(Optional.of(userEntity));
        when(taskRepository.countByUserId(userDTO.getId()))
                .thenReturn(0L);
        when(taskRepository.countByUserIdAndStatus(userDTO.getId(), TaskStatus.TODO))
                .thenReturn(0L);
        when(taskRepository.countByUserIdAndStatus(userDTO.getId(), TaskStatus.IN_PROGRESS))
                .thenReturn(0L);
        when(taskRepository.countByUserIdAndStatus(userDTO.getId(), TaskStatus.DONE))
                .thenReturn(0L);
        //when
        UserStatsDTO result = userService.getUserStats(userDTO.getId());
        //then
        assertNotNull(result);
        assertEquals(userEntity.getId(), result.getUserId());
        assertEquals(userEntity.getName(), result.getUserName());

        assertEquals(0L, result.getTotalTasks());
        assertEquals(0L, result.getTodoTasks());
        assertEquals(0L, result.getInProgressTasks());
        assertEquals(0L, result.getDoneTasks());
        assertEquals(0.0, result.getCompletionRate());

        verify(userRepository, times(1)).findById(userDTO.getId());
        verify(taskRepository, times(1)).countByUserId(userDTO.getId());
        verify(taskRepository, times(1)).countByUserIdAndStatus(userDTO.getId(), TaskStatus.TODO);
        verify(taskRepository, times(1)).countByUserIdAndStatus(userDTO.getId(), TaskStatus.IN_PROGRESS);
        verify(taskRepository, times(1)).countByUserIdAndStatus(userDTO.getId(), TaskStatus.DONE);
    }

    @Test
    void shouldCalculateCompletionRateCorrectly() {
        //given
        when(userRepository.findById(userDTO.getId()))
                .thenReturn(Optional.of(userEntity));
        when(taskRepository.countByUserId(userDTO.getId()))
                .thenReturn(10L);
        when(taskRepository.countByUserIdAndStatus(userDTO.getId(), TaskStatus.TODO))
                .thenReturn(3L);
        when(taskRepository.countByUserIdAndStatus(userDTO.getId(), TaskStatus.IN_PROGRESS))
                .thenReturn(2L);
        when(taskRepository.countByUserIdAndStatus(userDTO.getId(), TaskStatus.DONE))
                .thenReturn(5L);
        //when
        UserStatsDTO result = userService.getUserStats(userDTO.getId());
        //then
        assertNotNull(result);
        assertEquals(userEntity.getId(), result.getUserId());
        assertEquals(userEntity.getName(), result.getUserName());

        assertEquals(10L, result.getTotalTasks());
        assertEquals(3L, result.getTodoTasks());
        assertEquals(2L, result.getInProgressTasks());
        assertEquals(5L, result.getDoneTasks());
        assertEquals(50.0, result.getCompletionRate());

        verify(userRepository, times(1)).findById(userDTO.getId());
        verify(taskRepository, times(1)).countByUserId(userDTO.getId());
        verify(taskRepository, times(1)).countByUserIdAndStatus(userDTO.getId(), TaskStatus.TODO);
        verify(taskRepository, times(1)).countByUserIdAndStatus(userDTO.getId(), TaskStatus.IN_PROGRESS);
        verify(taskRepository, times(1)).countByUserIdAndStatus(userDTO.getId(), TaskStatus.DONE);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundForStats() {
        //given
        when(userRepository.findById(userDTO.getId()))
                .thenReturn(Optional.empty());
        //when e then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserStats(userDTO.getId())
        );
        assertEquals("User not found with id: " + userDTO.getId(), exception.getMessage());
        verify(userRepository, times(1)).findById(userDTO.getId());
        verify(taskRepository, never()).countByUserId(any());
        verify(taskRepository, never()).countByUserIdAndStatus(any(), any());
    }

    @Test
    void shouldFindUserByEmailSuccessfully() {
        //given
        when(userRepository.findByEmail(userDTO.getEmail()))
                .thenReturn(Optional.of(userEntity));
        when(userMapper.toDTO(userEntity))
                .thenReturn(userDTO);
        //when
        UserDTO result = userService.findByEmail(userDTO.getEmail());
        //then
        assertNotNull(result);
        assertEquals(userDTO.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findByEmail(userDTO.getEmail());
        verify(userMapper, times(1)).toDTO(userEntity);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundByEmail() {
        //given
        String email = "naoexiste@gmail.com";
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());
        //when e then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.findByEmail(email)
        );
        assertEquals("User not found with email: " + email, exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
        verify(userMapper, never()).toDTO(any());
    }

    @Test
    void shouldFindAllUsersSuccessfully() {
        // given
        Pageable customPageable = PageRequest.of(2, 5);
        List<User> users = Arrays.asList(userEntity, userEntity);
        Page<User> userPage = new PageImpl<>(users);

        when(userRepository.findAll(customPageable)).thenReturn(userPage);
        when(userMapper.toDTO(userEntity)).thenReturn(userDTO);

        // when
        Page<UserDTO> result = userService.findAll(customPageable);

        // then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());

        verify(userRepository, times(1)).findAll(customPageable);
    }
}
