package com.shinhan_hackathon.the_family_guardian.global.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
abstract class TransactionEvent extends ApplicationEvent {

    private final String eventId;
    private final Long transactionId;

    public TransactionEvent(Object source, String eventId, Long transactionId) {
        super(source);
        this.eventId = eventId;
        this.transactionId = transactionId;
    }
}
