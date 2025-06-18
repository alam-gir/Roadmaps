package com.roadmaps.Roadmaps.security;

import com.roadmaps.Roadmaps.cache.UserCacheService;
import com.roadmaps.Roadmaps.common.exceptions.ApiException;
import com.roadmaps.Roadmaps.common.exceptions.NotFoundException;
import com.roadmaps.Roadmaps.modules.user.enities.User;
import com.roadmaps.Roadmaps.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.roadmaps.Roadmaps.security.UserPrinciple.createUserPrinciple;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {
    private final UserCacheService userCacheService;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try{
            User user = userCacheService.getUserByEmail(email);
            if(user == null){
                user = userRepository.findByEmailIgnoreCase(email)
                    .orElseThrow(() -> new NotFoundException("User not found"));
            }

            userCacheService.setUserByEmail(email, user);
            return createUserPrinciple(user);
        } catch (DataIntegrityViolationException e) {
            log.warn("User not found with email {}", email);
            throw new NotFoundException("User not found");
        } catch (Exception e){
            log.warn("Error while getting user by email {}", email);
            throw new ApiException();
        }
    }
}
