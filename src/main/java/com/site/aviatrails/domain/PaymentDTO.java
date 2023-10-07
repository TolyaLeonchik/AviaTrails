package com.site.aviatrails.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@Entity(name = "payment_history")
public class PaymentDTO {
    @Id
    @SequenceGenerator(name = "SeqGenPayment", sequenceName = "payment_history_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "SeqGenPayment")
    private Long id;

    @Column(name = "ticket_id")
    private Long ticketId;

    @Column(name = "passenger_id")
    private Long passengerId;

    @Column(name = "card_number")
    private String cardNumber;
}
