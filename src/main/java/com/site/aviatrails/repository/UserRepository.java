package com.site.aviatrails.repository;

import com.site.aviatrails.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long> {

    @Query("select a.id from user_info a where a.firstName=:firstName AND a.lastName=:lastName")
    Long findIdByFirstNameAndLastName(String firstName, String lastName);
}
