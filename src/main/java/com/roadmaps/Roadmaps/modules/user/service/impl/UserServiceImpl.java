package com.roadmaps.Roadmaps.modules.user.service.impl;

import com.roadmaps.Roadmaps.cache.UserCacheService;
import com.roadmaps.Roadmaps.common.exceptions.ApiException;
import com.roadmaps.Roadmaps.common.exceptions.DuplicateEmailException;
import com.roadmaps.Roadmaps.common.exceptions.NotFoundException;
import com.roadmaps.Roadmaps.modules.user.dtos.UserRequestDto;
import com.roadmaps.Roadmaps.modules.user.enities.EmailVerificationToken;
import com.roadmaps.Roadmaps.modules.user.enities.User;
import com.roadmaps.Roadmaps.modules.user.mapper.UserMapper;
import com.roadmaps.Roadmaps.modules.user.repository.UserRepository;
import com.roadmaps.Roadmaps.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserCacheService userCacheService;
    private final UserMapper userMapper;

    @Override
    public User getUserByEmail(String email) {
        try{
            User cachedUser = userCacheService.getUserByEmail(email);
            if (cachedUser != null) {
                return cachedUser;
            }

            User user = userRepository.findByEmailIgnoreCase(email)
                    .orElseThrow(() -> new NotFoundException("User not found"));

            userCacheService.setUserByEmail(email, user);
            return user;
        } catch (NotFoundException e) {
            log.warn("User not found with email {}", email);
            throw e;
        } catch (Exception e){
            log.warn("Error while getting user by email {}", email);
            throw new ApiException();
        }
    }

    @Override
    public User addUser(UserRequestDto userDto) {
        try{
            User user = userMapper.toEntityWithEncryptedPassword(userDto);
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            log.error("Error while saving user", ex);
            throw new DuplicateEmailException(userDto.getEmail());
        } catch (Exception ex) {
            log.error("Error while saving user", ex);
            throw new ApiException("Failed to create account!");
        }
    }

    @Override
    public String generateEmailVerificationToken(User user) {
        try{
            EmailVerificationToken token = new EmailVerificationToken();
            token.setToken(UUID.randomUUID().toString());
            token.setExpiredAt(LocalDateTime.now().plusMinutes(5));

            user.setVerificationToken(token);
            userRepository.save(user);

            return token.getToken();
        } catch (Exception ex) {
            log.error("Error while generating emailVerificationToken", ex);
        }
        return null;
    }

    @Override
    public User update(User user) {
        try{
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            log.error("Error while updating user", ex);
            throw new DuplicateEmailException(user.getEmail());
        } catch (Exception ex) {
            log.error("Error while updating user", ex);
            throw new ApiException("Failed to update user data!");
        }
    }
}
