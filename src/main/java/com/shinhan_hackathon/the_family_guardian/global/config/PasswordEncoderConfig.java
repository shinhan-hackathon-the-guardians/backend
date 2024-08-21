package com.shinhan_hackathon.the_family_guardian.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
        return new DevelopPasswordEncoder();
    }

    static class DevelopPasswordEncoder implements PasswordEncoder {

        private static final String DEV_STR = "개발 전용 PasswordEncoder를 사용했습니다.";

        DevelopPasswordEncoder() {
            log.warn(DEV_STR);
        }

        @Override
        public String encode(CharSequence rawPassword) {
            log.warn(DEV_STR);
            return rawPassword.toString();
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            log.warn(DEV_STR);
            return encodedPassword.equals(rawPassword.toString());
        }
    }
}
