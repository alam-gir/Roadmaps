package com.roadmaps.Roadmaps.modules.user.service.impl;

import com.roadmaps.Roadmaps.common.exceptions.ApiException;
import com.roadmaps.Roadmaps.common.exceptions.DuplicateEmailException;
import com.roadmaps.Roadmaps.common.exceptions.NotFoundException;
import com.roadmaps.Roadmaps.modules.user.dtos.UserRequestDto;
import com.roadmaps.Roadmaps.modules.user.enities.User;
import com.roadmaps.Roadmaps.modules.user.mapper.UserMapper;
import com.roadmaps.Roadmaps.modules.user.repository.UserRepository;
import com.roadmaps.Roadmaps.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new NotFoundException("User not found by : " + email));
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
}
