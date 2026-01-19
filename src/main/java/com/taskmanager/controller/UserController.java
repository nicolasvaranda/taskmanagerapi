package com.taskmanager.controller;

import com.taskmanager.model.dto.TaskDTO;
import com.taskmanager.model.dto.UserDTO;
import com.taskmanager.model.dto.UserStatsDTO;
import com.taskmanager.model.enums.TaskStatus;
import com.taskmanager.service.TaskService;
import com.taskmanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO dto) {
        UserDTO response = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        UserDTO response = userService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email) {
        UserDTO response = userService.findByEmail(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAllUsers(Pageable pageable) {
        Page<UserDTO> response = userService.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody @Valid UserDTO dto) {
        UserDTO response = userService.updateUser(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<Page<TaskDTO>> getUserTasks(@PathVariable Long id, @RequestParam(required = false) TaskStatus status, Pageable pageable) {
        Page<TaskDTO> response;

        if (status != null) {
            response = taskService.findByUserIdAndStatus(id, status, pageable);
        } else {
            response = taskService.findByUserId(pageable, id);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<UserStatsDTO> getUserStats(@PathVariable Long id) {
        UserStatsDTO response = userService.getUserStats(id);
        return ResponseEntity.ok(response);
    }
}
