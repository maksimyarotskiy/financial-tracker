package com.maksimyarotskiy.financial_tracker.controller;

import com.maksimyarotskiy.financial_tracker.dto.UserRequest;
import com.maksimyarotskiy.financial_tracker.dto.UserResponse;
import com.maksimyarotskiy.financial_tracker.model.Transaction;
import com.maksimyarotskiy.financial_tracker.model.User;
import com.maksimyarotskiy.financial_tracker.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRequest userRequest) {
        try {
            userService.registerUser(userRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("User successfully created");
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PostMapping("/{userId}/transactions")
    public void addTransaction(@PathVariable Long userId, @RequestBody Transaction transaction) {
        userService.addTransactionToUser(userId, transaction);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers()
                .stream()
                .map(user -> {
                    UserResponse response = new UserResponse();
                    response.setId(user.getId());
                    response.setName(user.getName());
                    response.setEmail(user.getEmail());
                    return response;
                }).collect(Collectors.toList());

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}/transactions")
    public ResponseEntity<List<Transaction>> getUserTransactions(@PathVariable Long userId) {
            List<Transaction> userTransactions = userService.getUserTransactions(userId);
            return ResponseEntity.ok(userTransactions);
    }
}
