package com.shinhan_hackathon.the_family_guardian.global.event;

public class TransactionApproveEvent extends TransactionEvent {
    public TransactionApproveEvent(Object source, String eventId, Long transactionId) {
        super(source, eventId, transactionId);
    }
}
