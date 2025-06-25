package com.roadmaps.Roadmaps.modules.user.service.impl;

import com.roadmaps.Roadmaps.common.exceptions.ApiException;
import com.roadmaps.Roadmaps.common.exceptions.DuplicateEmailException;
import com.roadmaps.Roadmaps.common.exceptions.NotFoundException;
import com.roadmaps.Roadmaps.modules.user.enities.User;
import com.roadmaps.Roadmaps.modules.user.repository.UserRepository;
import com.roadmaps.Roadmaps.modules.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User getUserByEmail(String email) {
        try {

            return userRepository.findByEmailIgnoreCase(email)
                    .orElseThrow(() -> new NotFoundException("User not found"));

        } catch (NotFoundException e) {
            log.warn("User not found with email {}", email);
            throw e;
        } catch (Exception e) {
            log.warn("Error while getting user by email {}", email);
            throw new ApiException();
        }
    }

    @Override
    @Transactional
    public User save(User user) {
        try {
            userRepository.findByEmailIgnoreCase(user.getEmail())
                    .ifPresent(u -> {
                        throw new DuplicateEmailException(user.getEmail());
                    });
            return userRepository.save(user);
        } catch (DuplicateEmailException ex) {
            log.warn("User with email {} already exists", ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error when saving user", ex);
            throw new ApiException("Failed to create account!");
        }
    }
}
