package com.shinhan_hackathon.the_family_guardian.infra.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@Component
public class FirebaseInitializer {

    private final String firebaseAdminJsonPath, firebaseAppName;

    public FirebaseInitializer(
            @Value("${firebase.admin-key-path}") String firebaseAdminJsonPath,
            @Value("${firebase.app-name}") String firebaseAppName
    ) {
        this.firebaseAdminJsonPath = firebaseAdminJsonPath;
        this.firebaseAppName = firebaseAppName;
    }

    @PostConstruct
    private void initialize() {
        try {
            FileInputStream fileInputStream = new FileInputStream(firebaseAdminJsonPath);
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(fileInputStream);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                log.info("Initializing FirebaseApp with name: {}", firebaseAppName);
                FirebaseApp.initializeApp(options, firebaseAppName);
            }
            log.info("FirebaseApp '{}' successfully initialized.", FirebaseApp.getInstance(firebaseAppName).getName());
        } catch (IOException e) {
            log.warn("Failed to initialize FirebaseApp: {}", e.getMessage(), e);
        } catch (IllegalStateException e) {
            log.warn("FirebaseApp with name '{}' does not exist: {}", firebaseAppName, e.getMessage(), e);
        }
    }
}

