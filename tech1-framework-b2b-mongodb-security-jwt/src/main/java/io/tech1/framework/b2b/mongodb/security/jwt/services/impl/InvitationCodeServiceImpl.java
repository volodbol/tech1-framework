package io.tech1.framework.b2b.mongodb.security.jwt.services.impl;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCodes;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.InvitationCodeRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.services.InvitationCodeService;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.Objects.isNull;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvitationCodeServiceImpl implements InvitationCodeService {

    // Repositories
    private final InvitationCodeRepository invitationCodeRepository;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    public ResponseInvitationCodes findByOwner(Username owner) {
        var invitationCodes = this.invitationCodeRepository.findByOwner(owner).stream()
                .map(DbInvitationCode::getResponseInvitationCode)
                .sorted((o1, o2) -> {
                    if (isNull(o1.invited()) && isNull(o2.invited())) {
                        return 0;
                    } else if (isNull(o1.invited())) {
                        return -1;
                    } else if (isNull(o2.invited())) {
                        return 1;
                    } else {
                        return comparing((ResponseInvitationCode code) -> code.invited().identifier()).compare(o1, o2);
                    }
                }).collect(Collectors.toList());
        return new ResponseInvitationCodes(
                this.applicationFrameworkProperties.getSecurityJwtConfigs().getAuthoritiesConfigs().getAvailableAuthorities(),
                invitationCodes
        );
    }

    @Override
    public void save(RequestNewInvitationCodeParams requestNewInvitationCodeParams, Username owner) {
        var invitationCode = new DbInvitationCode(
                owner,
                requestNewInvitationCodeParams.authorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        );
        this.invitationCodeRepository.save(invitationCode);
    }

    @Override
    public void deleteById(String invitationCodeId) {
        this.invitationCodeRepository.deleteById(invitationCodeId);
    }
}
