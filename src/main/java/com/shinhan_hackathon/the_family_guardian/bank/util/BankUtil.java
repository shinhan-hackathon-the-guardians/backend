package com.shinhan_hackathon.the_family_guardian.bank.util;

import com.shinhan_hackathon.the_family_guardian.bank.dto.Header;
import com.shinhan_hackathon.the_family_guardian.bank.dto.HeaderResponse;
import io.github.cdimascio.dotenv.Dotenv;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class BankUtil {

    private static final String apiKey;
    private static final String userKey;

    private static final String SUCCESS_CODE = "H0000";

    static {
        Dotenv dotenv = Dotenv.load();
        apiKey = dotenv.get("API_KEY");
        userKey = dotenv.get("USER_KEY");
    }

    public static Header createHeader(String apiName) {
        String apiServiceCode = apiName;

        return Header.builder()
                .apiName(apiName)
                .apiServiceCode(apiServiceCode)
                .apiKey(apiKey)
                .userKey(userKey)
                .build();
    }

    // 현재 날짜를 YYYYMMDD 형식으로 반환하는 메소드
    public static String generateTransmissionDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date now = new Date();
        return dateFormat.format(now);
    }

    // 현재 시간에서 ±5분 범위의 시간을 HHMMSS 형식으로 반환하는 메소드
    public static String generateTransmissionTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
        Calendar calendar = Calendar.getInstance();

        return timeFormat.format(calendar.getTime());
    }

    public static String generateInstitutionTransactionUniqueCodeNo() {
        // 현재 날짜와 시간을 YYYYMMDD + HHMMSS 형식으로 생성
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateTime = dateFormat.format(new Date());

        // 6자리의 일련번호 생성 (000001 ~ 999999)
        Random random = new Random();
        int sequence = random.nextInt(999999) + 1;
        String sequenceStr = String.format("%06d", sequence);

        // 최종 코드 생성
        return dateTime + sequenceStr;
    }

    public static void validateBankApiResponse(HeaderResponse headerResponse) {
        String responseCode = headerResponse.getResponseCode();

        if (!SUCCESS_CODE.equals(responseCode)) {
            throw new RuntimeException("은행 API 요청 실패. 올바르지 않은 응답.");
        }
    }
}
