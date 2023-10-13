package com.site.aviatrails.security.service;

import com.site.aviatrails.domain.Role;
import com.site.aviatrails.domain.UserInfo;
import com.site.aviatrails.repository.UserRepository;
import com.site.aviatrails.security.JwtUtils;
import com.site.aviatrails.security.domain.AuthRequest;
import com.site.aviatrails.security.domain.RegistrationDTO;
import com.site.aviatrails.security.domain.SecurityCredentials;
import com.site.aviatrails.security.repository.SecurityCredentialsRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SecurityService {

    private final SecurityCredentials securityCredentials;
    private final SecurityCredentialsRepository securityCredentialsRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserInfo userInfo;
    private final UserRepository userRepository;

    public SecurityService(SecurityCredentials securityCredentials, SecurityCredentialsRepository securityCredentialsRepository,
                           JwtUtils jwtUtils, PasswordEncoder passwordEncoder, UserInfo userInfo, UserRepository userRepository) {
        this.securityCredentials = securityCredentials;
        this.securityCredentialsRepository = securityCredentialsRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.userInfo = userInfo;
        this.userRepository = userRepository;
    }

    public String generateToken(AuthRequest authRequest) {
        Optional<SecurityCredentials> credentials = securityCredentialsRepository.findByUserLogin(authRequest.getLogin());

        if (credentials.isPresent() && passwordEncoder.matches(authRequest.getPassword(), credentials.get().getUserPassword())) {
            return jwtUtils.generatedJwtToken(authRequest.getLogin());
        }
        return "";
    }

    @Transactional(rollbackFor = Exception.class)
    public void registration(RegistrationDTO registrationDTO) {
        userInfo.setFirstName(registrationDTO.getFirstName());
        userInfo.setLastName(registrationDTO.getLastName());
        userInfo.setEmail(registrationDTO.getEmail());
        userInfo.setPhoneNumber(registrationDTO.getPhoneNumber());
        UserInfo userInfoResult = userRepository.save(userInfo);

        securityCredentials.setUserLogin(registrationDTO.getUserLogin());
        securityCredentials.setUserPassword(passwordEncoder.encode(registrationDTO.getUserPassword()));
        securityCredentials.setUserRole(Role.USER);
        securityCredentials.setUserId(userInfoResult.getId());

        securityCredentialsRepository.save(securityCredentials);
    }
}
