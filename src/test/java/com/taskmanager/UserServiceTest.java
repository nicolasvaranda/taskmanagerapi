package com.taskmanager;

import com.taskmanager.exception.DuplicateResourceException;
import com.taskmanager.mapper.UserMapper;
import com.taskmanager.model.dto.UserDTO;
import com.taskmanager.model.entity.User;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.postgresql.hostchooser.HostRequirement.any;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

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
    void shouldThrowExceptionWhenEmailAlreadyExists() {
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
}
