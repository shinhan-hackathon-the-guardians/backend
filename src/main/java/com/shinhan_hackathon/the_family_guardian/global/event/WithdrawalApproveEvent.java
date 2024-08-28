package com.shinhan_hackathon.the_family_guardian.global.event;

public class WithdrawalApproveEvent extends TransactionApproveEvent {
    public WithdrawalApproveEvent(Object source, String eventId, Long transactionId) {
        super(source, eventId, transactionId);
    }
}
