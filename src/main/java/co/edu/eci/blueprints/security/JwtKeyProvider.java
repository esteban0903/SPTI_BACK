package co.edu.eci.blueprints.security;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.security.*;

@Component
public class JwtKeyProvider {
    private KeyPair keyPair;

    @PostConstruct
    void init() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            this.keyPair = kpg.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo generar llave RSA", e);
        }
    }

    public PrivateKey privateKey() { return keyPair.getPrivate(); }
    public PublicKey publicKey() { return keyPair.getPublic(); }
}
