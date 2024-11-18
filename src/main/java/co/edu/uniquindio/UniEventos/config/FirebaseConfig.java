package co.edu.uniquindio.UniEventos.config;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

@Configuration
public class FirebaseConfig {
    @Bean
    public FirebaseApp intializeFirebase() throws IOException {
        // Obtener la URL del archivo JSON desde la variable de entorno
        String credentialsUrl = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        if (credentialsUrl == null) {
            throw new IllegalStateException("FIREBASE_CREDENTIALS_URL is not set");
        }

        // Descargar el archivo JSON desde la URL
        URL url = new URL(credentialsUrl);
        ReadableByteChannel byteChannel = Channels.newChannel(url.openStream());

        // Convertir el byteChannel a InputStream
        InputStream serviceAccount = Channels.newInputStream(byteChannel);

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("unieventos-54199.appspot.com")
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(options);
        }
        return null;
    }
}