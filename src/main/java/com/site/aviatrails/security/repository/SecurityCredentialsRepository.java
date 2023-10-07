package com.site.aviatrails.security.repository;

import com.site.aviatrails.security.domain.SecurityCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecurityCredentialsRepository extends JpaRepository<SecurityCredentials, Long> {

    Optional<SecurityCredentials> findByUserLogin(String login);
}
