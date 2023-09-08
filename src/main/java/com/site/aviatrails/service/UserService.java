package com.site.aviatrails.service;

import com.site.aviatrails.domain.UserInfo;
import com.site.aviatrails.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserInfo> getUsers() {
        return userRepository.findAll();
    }

    public Optional<UserInfo> getUser(Long id) {
        return userRepository.findById(id);
    }

    public void createUser(UserInfo userInfo) {
        userRepository.save(userInfo);
    }

    public void updateUser(UserInfo userInfo) {
        userRepository.saveAndFlush(userInfo);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
