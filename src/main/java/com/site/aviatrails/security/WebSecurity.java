package com.site.aviatrails.security;

import com.site.aviatrails.domain.Role;
import com.site.aviatrails.domain.tickets.Ticket;
import com.site.aviatrails.repository.TicketRepository;
import com.site.aviatrails.security.domain.SecurityCredentials;
import com.site.aviatrails.security.repository.SecurityCredentialsRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class WebSecurity {

    private final SecurityCredentialsRepository securityCredentialsRepository;
    private final TicketRepository ticketRepository;

    public WebSecurity(SecurityCredentialsRepository securityCredentialsRepository, TicketRepository ticketRepository) {
        this.securityCredentialsRepository = securityCredentialsRepository;
        this.ticketRepository = ticketRepository;
    }

    public boolean canAccessUser(Authentication authentication, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String pattern = "/user/";
        int startIndex = requestURI.indexOf(pattern) + pattern.length();
        String userId = requestURI.substring(startIndex);
        long id = Long.parseLong(userId);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<SecurityCredentials> securityCredentials = securityCredentialsRepository.findByUserLogin(userDetails.getUsername());
        if (securityCredentials.get().getUserRole().equals(Role.ADMIN)) {
            return true;
        } else if (securityCredentials.get().getUserId().equals(id)) {
            return true;
        }
        return false;
    }

    public boolean canAccessToDelete(Authentication authentication, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String pattern = "/booking/refund/";
        int startIndex = requestURI.indexOf(pattern) + pattern.length();
        String tickedId = requestURI.substring(startIndex);
        long id = Long.parseLong(tickedId);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<SecurityCredentials> securityCredentials = securityCredentialsRepository.findByUserLogin(userDetails.getUsername());
        Optional<Ticket> ticket = ticketRepository.findById(id);

        if (securityCredentials.get().getUserRole().equals(Role.ADMIN)) {
            return true;
        } else if (securityCredentials.get().getUserId().equals(ticket.get().getPassengerId())) {
            return true;
        }
        return false;
    }
}
