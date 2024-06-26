package io.tech1.framework.b2b.base.security.jwt.events.subscribers;

import io.tech1.framework.incidents.domain.authetication.*;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1Failure;
import io.tech1.framework.incidents.domain.session.IncidentSessionExpired;
import io.tech1.framework.incidents.domain.session.IncidentSessionRefreshed;
import org.springframework.context.event.EventListener;

public interface SecurityJwtIncidentSubscriber {
    @EventListener
    void onEvent(IncidentAuthenticationLogin incident);
    @EventListener
    void onEvent(IncidentAuthenticationLoginFailureUsernamePassword incident);
    @EventListener
    void onEvent(IncidentAuthenticationLoginFailureUsernameMaskedPassword incident);
    @EventListener
    void onEvent(IncidentAuthenticationLogoutMin incident);
    @EventListener
    void onEvent(IncidentAuthenticationLogoutFull incident);
    @EventListener
    void onEvent(IncidentRegistration1 incident);
    @EventListener
    void onEvent(IncidentRegistration1Failure incident);
    @EventListener
    void onEvent(IncidentSessionRefreshed incident);
    @EventListener
    void onEvent(IncidentSessionExpired incident);
}
