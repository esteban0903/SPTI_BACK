package co.edu.eci.blueprints.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;

/**
 * DataSource configuration that prefers an externally configured JDBC URL but
 * falls back to an in-memory H2 database if the external connection cannot be established.
 */
@Configuration
public class DataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

    @Bean
    @Primary
    public DataSource dataSource(Environment env) {
        String jdbcUrl = env.getProperty("SPRING_DATASOURCE_URL");
        String username = env.getProperty("SPRING_DATASOURCE_USERNAME");
        String password = env.getProperty("SPRING_DATASOURCE_PASSWORD");

        if (jdbcUrl != null && !jdbcUrl.isBlank()) {
            try {
                log.info("Attempting to use external datasource: {}", jdbcUrl);
                HikariConfig cfg = new HikariConfig();
                cfg.setJdbcUrl(jdbcUrl);
                if (username != null) cfg.setUsername(username);
                if (password != null) cfg.setPassword(password);
                // reasonable defaults
                cfg.setMaximumPoolSize(10);
                HikariDataSource ds = new HikariDataSource(cfg);

                // quick connectivity check
                try (Connection c = ds.getConnection()) {
                    log.info("Successfully connected to external datasource");
                    return ds;
                }
            } catch (Exception e) {
                log.warn("External datasource unavailable or failed to initialize, falling back to H2 in-memory. Cause: {}", e.toString());
                // attempt to close if partially initialized
            }
        } else {
            log.info("No external SPRING_DATASOURCE_URL configured, using H2 in-memory.");
        }

        // Fallback: H2 in-memory datasource
        DriverManagerDataSource h2 = new DriverManagerDataSource();
        h2.setDriverClassName("org.h2.Driver");
        h2.setUrl("jdbc:h2:mem:blueprints;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        h2.setUsername("sa");
        h2.setPassword("");
        log.info("Using fallback H2 in-memory datasource (jdbc:h2:mem:blueprints)");
        return h2;
    }
}
