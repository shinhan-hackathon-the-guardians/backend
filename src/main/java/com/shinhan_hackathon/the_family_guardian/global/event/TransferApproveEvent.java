package com.shinhan_hackathon.the_family_guardian.global.event;

public class TransferApproveEvent extends TransactionApproveEvent {
    public TransferApproveEvent(Object source, String eventId, Long transactionId) {
        super(source, eventId, transactionId);
    }
}
