package fox.mc.backfill.mediaconvert.config;

import fox.mc2.commons.Configs;
import fox.mc2.vidispine.service.VidispineClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "vidispine")
public class VidispineConfiguration {
    private String env;
    private String user;

    @Autowired
    private ConfigProperties configProperties;

    @Bean
    public VidispineClient vidispineClient() {
        return VidispineClient.builder().environment(Configs.ENV.valueOf(env))
                .user(user)
                .password(configProperties.getAdminPassword())
                .build();
    }
}
