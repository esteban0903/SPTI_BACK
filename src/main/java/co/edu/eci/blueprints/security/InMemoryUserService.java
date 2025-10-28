package co.edu.eci.blueprints.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class InMemoryUserService {
    private final Map<String, String> users; // username -> hash
    private final PasswordEncoder encoder;

    /**
     * Constructor. Reads user passwords from environment variables to avoid
     * keeping credentials in source control. Environment variables supported:
     * - STUDENT_PASSWORD
     * - ASSISTANT_PASSWORD
     * If none are provided, the internal map will be empty and authentication
     * must be provided by other means in production.
     */
    public InMemoryUserService(PasswordEncoder encoder, Environment env) {
        this.encoder = encoder;

        // Prefer values from Spring Environment (includes env vars and properties).
        String studentPwd = env.getProperty("STUDENT_PASSWORD");
        String assistantPwd = env.getProperty("ASSISTANT_PASSWORD");

        if (studentPwd == null && assistantPwd == null) {
            // No credentials provided; keep empty map to avoid hard-coded secrets.
            // For tests/local dev, provide these values via a profile-specific
            // properties file (e.g. src/test/resources/application-identity.properties)
            // or via environment variables. Do NOT hard-code passwords here.
            this.users = Collections.emptyMap();
        } else {
            Map<String, String> m = new HashMap<>();
            if (studentPwd != null) m.put("student", encoder.encode(studentPwd));
            if (assistantPwd != null) m.put("assistant", encoder.encode(assistantPwd));
            this.users = Collections.unmodifiableMap(m);
        }
    }

    public boolean isValid(String username, String rawPassword) {
        String hash = users.get(username);
        return hash != null && encoder.matches(rawPassword, hash);
    }
}
