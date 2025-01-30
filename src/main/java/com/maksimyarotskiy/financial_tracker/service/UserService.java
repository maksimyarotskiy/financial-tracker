package com.maksimyarotskiy.financial_tracker.service;

import com.maksimyarotskiy.financial_tracker.dto.UserRequest;
import com.maksimyarotskiy.financial_tracker.model.User;
import com.maksimyarotskiy.financial_tracker.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.maksimyarotskiy.financial_tracker.model.Transaction;
import com.maksimyarotskiy.financial_tracker.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TransactionRepository transactionRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.transactionRepository = transactionRepository;
    }

    public void registerUser(UserRequest userRequest) {
        if (userRepository.findByName(userRequest.getName()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());

        userRepository.save(user);
    }

    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void addTransactionToUser(Long userId, Transaction transaction) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Устанавливаем связь между транзакцией и пользователем
        transaction.setUser(user);

        transactionRepository.save(transaction);
    }

    public List<Transaction> getUserTransactions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getTransactions();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
