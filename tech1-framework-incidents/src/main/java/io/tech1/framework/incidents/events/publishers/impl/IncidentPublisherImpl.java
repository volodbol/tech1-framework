package io.tech1.framework.incidents.events.publishers.impl;

import io.tech1.framework.domain.pubsub.AbstractEventPublisher;
import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.domain.authetication.*;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1Failure;
import io.tech1.framework.incidents.domain.session.IncidentSessionExpired;
import io.tech1.framework.incidents.domain.session.IncidentSessionRefreshed;
import io.tech1.framework.incidents.domain.throwable.IncidentThrowable;
import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.*;
import static io.tech1.framework.incidents.domain.IncidentAttributes.IncidentsTypes.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncidentPublisherImpl extends AbstractEventPublisher implements IncidentPublisher {

    // Spring Publisher
    private final ApplicationEventPublisher applicationEventPublisher;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    public void publishAuthenticationLogin(IncidentAuthenticationLogin incident) {
        if (this.applicationFrameworkProperties.getIncidentConfigs().getFeatures().getLogin().isEnabled()) {
            LOGGER.debug(INCIDENT_AUTHENTICATION_LOGIN, this.getType(), incident.getUsername());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, AUTHENTICATION_LOGIN);
        }
    }

    @Override
    public void publishAuthenticationLoginFailureUsernamePassword(IncidentAuthenticationLoginFailureUsernamePassword incident) {
        if (this.applicationFrameworkProperties.getIncidentConfigs().getFeatures().getLoginFailureUsernamePassword().isEnabled()) {
            LOGGER.debug(INCIDENT_AUTHENTICATION_LOGIN_FAILURE, this.getType(), incident.getUsername());
            this.applicationEventPublisher.publishEvent(incident);
        }
    }

    @Override
    public void publishAuthenticationLoginFailureUsernameMaskedPassword(IncidentAuthenticationLoginFailureUsernameMaskedPassword incident) {
        if (this.applicationFrameworkProperties.getIncidentConfigs().getFeatures().getLoginFailureUsernameMaskedPassword().isEnabled()) {
            LOGGER.debug(INCIDENT_AUTHENTICATION_LOGIN_FAILURE, this.getType(), incident.getUsername());
            this.applicationEventPublisher.publishEvent(incident);
        }
    }

    @Override
    public void publishAuthenticationLogoutMin(IncidentAuthenticationLogoutMin incident) {
        if (this.applicationFrameworkProperties.getIncidentConfigs().getFeatures().getLogoutMin().isEnabled()) {
            LOGGER.debug(INCIDENT_AUTHENTICATION_LOGOUT, this.getType(), incident.getUsername());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, AUTHENTICATION_LOGOUT_MIN);
        }
    }

    @Override
    public void publishAuthenticationLogoutFull(IncidentAuthenticationLogoutFull incident) {
        if (this.applicationFrameworkProperties.getIncidentConfigs().getFeatures().getLogout().isEnabled()) {
            LOGGER.debug(INCIDENT_AUTHENTICATION_LOGOUT, this.getType(), incident.getUsername());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, AUTHENTICATION_LOGOUT);
        }
    }

    @Override
    public void publishRegistration1(IncidentRegistration1 incident) {
        if (this.applicationFrameworkProperties.getIncidentConfigs().getFeatures().getRegister1().isEnabled()) {
            LOGGER.debug(INCIDENT_REGISTER1, this.getType(), incident.getUsername());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, REGISTER1);
        }
    }

    @Override
    public void publishRegistration1Failure(IncidentRegistration1Failure incident) {
        if (this.applicationFrameworkProperties.getIncidentConfigs().getFeatures().getRegister1Failure().isEnabled()) {
            LOGGER.debug(INCIDENT_REGISTER1_FAILURE, this.getType(), incident.getUsername());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, REGISTER1_FAILURE);
        }
    }

    @Override
    public void publishSessionRefreshed(IncidentSessionRefreshed incident) {
        if (this.applicationFrameworkProperties.getIncidentConfigs().getFeatures().getSessionRefreshed().isEnabled()) {
            LOGGER.debug(INCIDENT_SESSION_REFRESHED, this.getType(), incident.getUsername());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, SESSION_REFRESHED);
        }
    }

    @Override
    public void publishSessionExpired(IncidentSessionExpired incident) {
        if (this.applicationFrameworkProperties.getIncidentConfigs().getFeatures().getSessionExpired().isEnabled()) {
            LOGGER.debug(INCIDENT_SESSION_EXPIRED, this.getType(), incident.getUsername());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, SESSION_EXPIRED);
        }
    }

    @Override
    public void publishIncident(Incident incident) {
        LOGGER.debug(INCIDENT, this.getType(), incident.getType());
        this.applicationEventPublisher.publishEvent(incident);
    }

    @Override
    public void publishThrowable(IncidentThrowable incident) {
        LOGGER.debug(INCIDENT_THROWABLE, this.getType(), incident.getThrowable().getMessage());
        this.applicationEventPublisher.publishEvent(incident);
    }

    @Override
    public void publishThrowable(Throwable throwable) {
        this.publishThrowable(IncidentThrowable.of(throwable));
    }
}
