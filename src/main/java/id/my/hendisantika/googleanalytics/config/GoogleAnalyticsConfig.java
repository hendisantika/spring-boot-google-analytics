package id.my.hendisantika.googleanalytics.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "google.analytics")
@Data
public class GoogleAnalyticsConfig {

    private String propertyId;
    private String credentialsPath;
    private boolean enabled = true;

    public boolean isConfigured() {
        return enabled && propertyId != null && !propertyId.isEmpty();
    }
}