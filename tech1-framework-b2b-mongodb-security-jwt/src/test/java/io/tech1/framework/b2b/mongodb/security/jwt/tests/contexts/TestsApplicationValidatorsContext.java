package io.tech1.framework.b2b.mongodb.security.jwt.tests.contexts;

import io.tech1.framework.b2b.mongodb.security.jwt.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.b2b.mongodb.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoInvitationCodesRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUserRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUserSessionRepository;
import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
import io.tech1.framework.properties.tests.contexts.ApplicationFrameworkPropertiesContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan({
        "io.tech1.framework.b2b.mongodb.security.jwt.validators"
})
@Import({
        ApplicationFrameworkPropertiesContext.class
})
public class TestsApplicationValidatorsContext {

    // =================================================================================================================
    // Publishers
    // =================================================================================================================
    @Bean
    SecurityJwtPublisher securityJwtPublisher() {
        return mock(SecurityJwtPublisher.class);
    }

    @Bean
    SecurityJwtIncidentPublisher securityJwtIncidentPublisher() {
        return mock(SecurityJwtIncidentPublisher.class);
    }

    @Bean
    IncidentPublisher incidentPublisher() {
        return mock(IncidentPublisher.class);
    }

    // =================================================================================================================
    // Repositories
    // =================================================================================================================
    @Bean
    MongoInvitationCodesRepository invitationCodeRepository() {
        return mock(MongoInvitationCodesRepository.class);
    }

    @Bean
    MongoUserSessionRepository userSessionRepository() {
        return mock(MongoUserSessionRepository.class);
    }

    @Bean
    MongoUserRepository userRepository() {
        return mock(MongoUserRepository.class);
    }
}
