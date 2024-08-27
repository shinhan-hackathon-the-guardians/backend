package com.shinhan_hackathon.the_family_guardian.global.fcm;

public interface MessageSender {
    void sendMessage(String receiverAddress, String title, String body);
}
