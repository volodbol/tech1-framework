package io.tech1.framework.incidents.events.publishers.impl;

import io.tech1.framework.incidents.domain.throwable.IncidentThrowable;
import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static io.tech1.framework.incidents.tests.random.IncidentsRandomUtility.*;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class IncidentPublisherImplTest {

    @Configuration
    static class ContextConfiguration {
        @Primary
        @Bean
        ApplicationEventPublisher applicationEventPublisher() {
            return mock(ApplicationEventPublisher.class);
        }

        @Bean
        ApplicationFrameworkProperties applicationFrameworkProperties() {
            return mock(ApplicationFrameworkProperties.class);
        }

        @Bean
        IncidentPublisher incidentPublisher() {
            return new IncidentPublisherImpl(
                    this.applicationEventPublisher()
            );
        }
    }

    // Spring Publisher
    private final ApplicationEventPublisher applicationEventPublisher;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final IncidentPublisher componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.applicationEventPublisher,
                this.applicationFrameworkProperties
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.applicationEventPublisher,
                this.applicationFrameworkProperties
        );
    }

    @Test
    void publishResetServerStartedTest() {
        // Arrange
        var incident = randomIncidentSystemResetServerStarted();

        // Act
        this.componentUnderTest.publishResetServerStarted(incident);

        // Assert
        verify(this.applicationEventPublisher).publishEvent(eq(incident));
    }

    @Test
    void publishResetServerCompletedTest() {
        // Arrange
        var incident = randomIncidentSystemResetServerCompleted();

        // Act
        this.componentUnderTest.publishResetServerCompleted(incident);

        // Assert
        verify(this.applicationEventPublisher).publishEvent(eq(incident));
    }

    @Test
    void publishIncidentTest() {
        // Arrange
        var incident = randomIncident();

        // Act
        this.componentUnderTest.publishIncident(incident);

        // Assert
        verify(this.applicationEventPublisher).publishEvent(eq(incident));
    }

    @Test
    void publishThrowableIncidentTest() {
        // Arrange
        var incident = randomThrowableIncident();

        // Act
        this.componentUnderTest.publishThrowable(incident);

        // Assert
        verify(this.applicationEventPublisher).publishEvent(eq(incident));
    }

    @Test
    void publishThrowableTest() {
        // Arrange
        var throwable = randomThrowableIncident().getThrowable();

        // Act
        this.componentUnderTest.publishThrowable(throwable);

        // Assert
        verify(this.applicationEventPublisher).publishEvent(eq(IncidentThrowable.of(throwable)));
    }
}
