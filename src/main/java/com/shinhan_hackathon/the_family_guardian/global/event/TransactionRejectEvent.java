package com.shinhan_hackathon.the_family_guardian.global.event;

public class TransactionRejectEvent extends TransactionEvent {
    public TransactionRejectEvent(Object source, String eventId, Long transactionId) {
        super(source, eventId, transactionId);
    }
}
