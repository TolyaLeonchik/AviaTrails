package com.site.aviatrails.service;

import com.site.aviatrails.domain.UserInfo;
import com.site.aviatrails.repository.UserRepository;
import com.site.aviatrails.validator.PhoneNumberValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<UserInfo> getUsers(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.ASC, "id");
        return userRepository.findAll(pageable);
    }

    public Optional<UserInfo> getUser(Long id) {
        return userRepository.findById(id);
    }

    public void createUser(UserInfo userInfo) {
        PhoneNumberValidator.validate(userInfo.getPhoneNumber());
        userRepository.save(userInfo);
    }

    public void updateUser(UserInfo userInfo) {
        userRepository.saveAndFlush(userInfo);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
