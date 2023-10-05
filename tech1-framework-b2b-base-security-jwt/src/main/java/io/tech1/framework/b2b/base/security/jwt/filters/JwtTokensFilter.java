package io.tech1.framework.b2b.base.security.jwt.filters;

import io.tech1.framework.b2b.base.security.jwt.cookies.CookieProvider;
import io.tech1.framework.b2b.base.security.jwt.domain.sessions.Session;
import io.tech1.framework.b2b.base.security.jwt.services.TokensService;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.domain.exceptions.cookie.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtTokensFilter extends OncePerRequestFilter {

    // Session
    private final SessionRegistry sessionRegistry;
    // Services
    private final TokensService tokensService;
    // Cookies
    private final CookieProvider cookieProvider;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            var cookieAccessToken = this.cookieProvider.readJwtAccessToken(request);
            var cookieRefreshToken = this.cookieProvider.readJwtRefreshToken(request);
            var user = this.tokensService.getJwtUserByAccessTokenOrThrow(cookieAccessToken, cookieRefreshToken);

            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            this.sessionRegistry.register(new Session(user.username(), cookieAccessToken.getJwtAccessToken(), cookieRefreshToken.getJwtRefreshToken()));

            filterChain.doFilter(request, response);
        } catch (CookieAccessTokenNotFoundException | CookieAccessTokenExpiredException ex) {
            LOGGER.warn("JWT tokens filter, access token is required. Message: {}", ex.getMessage());
            // NOTE: place to refresh token. problem how to distinguish authenticated vs. anonymous/permitAll endpoints
            filterChain.doFilter(request, response);
        } catch (CookieRefreshTokenNotFoundException | CookieAccessTokenInvalidException | CookieRefreshTokenInvalidException | CookieAccessTokenDbNotFoundException ex) {
            LOGGER.warn("JWT tokens filter, clear cookies. Message: {}", ex.getMessage());
            this.cookieProvider.clearCookies(response);
            response.sendError(HttpStatus.UNAUTHORIZED.value());
        }
    }
}