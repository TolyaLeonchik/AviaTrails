package com.site.aviatrails.repository;

import com.site.aviatrails.domain.PaymentDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentDTO, Long> {

    Optional<PaymentDTO> findByTicketId(Long id);

    boolean existsByTicketId(Long id);
}
