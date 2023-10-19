package com.site.aviatrails.repository;

import com.site.aviatrails.domain.UserInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    static UserInfo userInfo;

    @BeforeAll
    static void beforeAll() {
        userInfo = new UserInfo();
        userInfo.setFirstName("TestFirstName");
        userInfo.setLastName("TestLastName");
        userInfo.setEmail("test@test.com");
        userInfo.setPhoneNumber("4789632589647");
    }

    @Test
    void findAllTest() {
        userRepository.save(userInfo);
        List<UserInfo> newList = userRepository.findAll();
        Assertions.assertNotNull(newList);
    }

    @Test
    void findByIdTest() {
        UserInfo saved = userRepository.save(userInfo);
        Optional<UserInfo> newUser = userRepository.findById(saved.getId());
        Assertions.assertTrue(newUser.isPresent());
    }

    @Test
    void saveTest() {
        List<UserInfo> oldList = userRepository.findAll();
        userRepository.save(userInfo);
        List<UserInfo> newList = userRepository.findAll();

        Assertions.assertNotEquals(oldList.size(), newList.size());
    }

    @Test
    void updateTest() {
        UserInfo user = userRepository.save(userInfo);
        user.setFirstName("UPDATED");
        UserInfo updatedUser = userRepository.saveAndFlush(user);
        Assertions.assertEquals(updatedUser.getFirstName(), "UPDATED");
    }

    @Test
    void deleteTest() {
        UserInfo userSaved = userRepository.save(userInfo);
        userRepository.delete(userSaved);
        Optional<UserInfo> user = userRepository.findById(userSaved.getId());
        Assertions.assertFalse(user.isPresent());
    }

    @Test
    void findIdByFirstNameAndLastNameTest() {
        UserInfo saved = userRepository.save(userInfo);
        Long newUserId = userRepository.findIdByFirstNameAndLastName(saved.getFirstName(), saved.getLastName());
        Assertions.assertEquals(saved.getId(), newUserId);
    }
}
