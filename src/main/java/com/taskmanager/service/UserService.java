package com.taskmanager.service;

import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.mapper.UserMapper;
import com.taskmanager.model.dto.UserDTO;
import com.taskmanager.model.dto.UserStatsDTO;
import com.taskmanager.model.entity.User;
import com.taskmanager.model.enums.TaskStatus;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TaskRepository taskRepository;

    public UserDTO createUser(UserDTO dto) {
        var emailExists = userRepository.existsByEmail(dto.getEmail());
        if (emailExists) {
            throw new IllegalArgumentException("Email already exists");
        }
        var userEntity = userMapper.toEntity(dto);
        var savedEntity = userRepository.save(userEntity);
        return userMapper.toDTO(savedEntity);
    }

    public UserDTO findById(Long id) {
        User user = findUserEntityById(id);
        return userMapper.toDTO(user);
    }

    public Page<UserDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toDTO);
    }

    private User findUserEntityById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public UserDTO updateUser(Long id, UserDTO dto) {
        User user = findUserEntityById(id);

        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new IllegalArgumentException("Email already exists: " + dto.getEmail());
            }
        }
        userMapper.updateEntity(user, dto);

        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    public void deleteUser(Long id) {
        User user = findUserEntityById(id);
        userRepository.delete(user);
    }

    public UserStatsDTO getUserStats(Long id) {
        User user = findUserEntityById(id);
        var totalTasks = taskRepository.countByUserId(id);
        var todoTasks = taskRepository.countByUserIdAndStatus(id, TaskStatus.TODO);
        var inProgressTasks = taskRepository.countByUserIdAndStatus(id, TaskStatus.IN_PROGRESS);
        var doneTasks = taskRepository.countByUserIdAndStatus(id, TaskStatus.DONE);

        return UserStatsDTO.builder()
                .userId(user.getId())
                .userName(user.getName())
                .totalTasks(totalTasks)
                .todoTasks(todoTasks)
                .inProgressTasks(inProgressTasks)
                .doneTasks(doneTasks)
                .completionRate(totalTasks > 0 ? (doneTasks * 100.0) / totalTasks : 0.0)
                .build();
    }
}
