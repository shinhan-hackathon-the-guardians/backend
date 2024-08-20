package com.shinhan_hackathon.the_family_guardian.bank.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Header {
    private String apiName; // API 이름
    private String transmissionDate; // API 요청 일자(YYYYMMDD), 현재 날짜
    private String transmissionTime; // API 전송 시각(HHMMSS), 현재 시간 +-5
    private String institutionCode = "00100"; // 기관 코드
    private String fintechAppNo = "001"; // 핀테크 앱 일련번호
    private String apiServiceCode; // API 서비스 코드, apiName 필드와 동일값
    private String institutionTransactionUniqueNo; // 기관 거래 고유번호
    private String apiKey; // 발급받은 API Key
    private String userKey; // 회원가입할 때 받은 User Key
}
