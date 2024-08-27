package com.shinhan_hackathon.the_family_guardian.global.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public void publishTransactionApproveEvent(String eventId, Long transactionId) {
        log.info("[Publish Approve Event] Event-ID: {}", eventId);
        TransactionApproveEvent transactionApproveEvent = new TransactionApproveEvent(this, eventId, transactionId);
        eventPublisher.publishEvent(transactionApproveEvent);
    }

    public void publishTransactionRejectEvent(String eventId, Long transactionId) {
        log.info("[Publish Reject Event] Event-ID: {}", eventId);
        TransactionRejectEvent transactionRejectEvent = new TransactionRejectEvent(this, eventId, transactionId);
        eventPublisher.publishEvent(transactionRejectEvent);
    }
}
