package io.tech1.framework.b2b.base.security.jwt.events.subscribers;

import io.tech1.framework.b2b.base.security.jwt.domain.events.*;
import org.springframework.context.event.EventListener;

public interface SecurityJwtSubscriber {
    @EventListener
    void onAuthenticationLogin(EventAuthenticationLogin event);
    @EventListener
    void onAuthenticationLoginFailure(EventAuthenticationLoginFailure event);
    @EventListener
    void onAuthenticationLogout(EventAuthenticationLogout event);
    @EventListener
    void onRegistration1(EventRegistration1 event);
    @EventListener
    void onRegistration1Failure(EventRegistration1Failure event);
    @EventListener
    void onSessionRefreshed(EventSessionRefreshed event);
    @EventListener
    void onSessionExpired(EventSessionExpired event);
    @EventListener
    void onSessionUserRequestMetadataAdd(EventSessionUserRequestMetadataAdd event);
    @EventListener
    void onSessionUserRequestMetadataRenew(EventSessionUserRequestMetadataRenew event);
}
