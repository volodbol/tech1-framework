package io.tech1.framework.b2b.base.security.jwt.resources;

import io.tech1.framework.b2b.base.security.jwt.annotations.AbstractFrameworkBaseSecurityResource;
import io.tech1.framework.b2b.base.security.jwt.assistants.current.CurrentSessionAssistant;
import io.tech1.framework.b2b.base.security.jwt.tokens.TokensProvider;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSessionsTable;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.security.CurrentClientUser;
import io.tech1.framework.b2b.base.security.jwt.services.BaseUsersSessionsService;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseUsersSessionsRequestsValidator;
import io.tech1.framework.domain.exceptions.tokens.AccessTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@AbstractFrameworkBaseSecurityResource
@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseSecurityUsersSessionsResource {

    // Assistants
    private final CurrentSessionAssistant currentSessionAssistant;
    // Services
    private final BaseUsersSessionsService baseUsersSessionsService;
    // Cookie
    private final TokensProvider tokensProvider;
    // Validators
    private final BaseUsersSessionsRequestsValidator baseUsersSessionsRequestsValidator;

    @GetMapping
    public ResponseUserSessionsTable getSessionsTable(HttpServletRequest httpServletRequest) throws AccessTokenNotFoundException {
        var cookie = this.tokensProvider.readJwtAccessToken(httpServletRequest);
        return this.currentSessionAssistant.getCurrentUserDbSessionsTable(cookie);
    }

    @GetMapping("/current")
    public CurrentClientUser getCurrentClientUser(HttpServletRequest httpServletRequest) throws AccessTokenNotFoundException {
        var user = this.currentSessionAssistant.getCurrentClientUser();
        var session = this.currentSessionAssistant.getCurrentUserSession(httpServletRequest);
        this.baseUsersSessionsService.renewUserRequestMetadata(session, httpServletRequest);
        return user;
    }

    @PostMapping("/{sessionId}/renew/manually")
    public void renewManually(@PathVariable UserSessionId sessionId) {
        var user = this.currentSessionAssistant.getCurrentJwtUser();
        this.baseUsersSessionsRequestsValidator.validateAccess(user.username(), sessionId);
        this.baseUsersSessionsService.enableUserRequestMetadataRenewManually(sessionId);
    }

    @DeleteMapping("/{sessionId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable UserSessionId sessionId) {
        var username = this.currentSessionAssistant.getCurrentUsername();
        this.baseUsersSessionsRequestsValidator.validateAccess(username, sessionId);
        this.baseUsersSessionsService.deleteById(sessionId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllExceptCurrent(HttpServletRequest httpServletRequest) throws AccessTokenNotFoundException {
        var username = this.currentSessionAssistant.getCurrentUsername();
        var cookie = this.tokensProvider.readJwtAccessToken(httpServletRequest);
        this.baseUsersSessionsService.deleteAllExceptCurrent(username, cookie);
    }
}

