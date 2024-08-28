package com.shinhan_hackathon.the_family_guardian.global.event;

public class PaymentApproveEvent extends TransactionApproveEvent {
    public PaymentApproveEvent(Object source, String eventId, Long transactionId) {
        super(source, eventId, transactionId);
    }
}
