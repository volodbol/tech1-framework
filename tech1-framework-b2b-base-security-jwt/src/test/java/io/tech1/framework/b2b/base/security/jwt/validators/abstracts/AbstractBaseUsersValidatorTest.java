package io.tech1.framework.b2b.base.security.jwt.validators.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserChangePasswordBasic;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate2;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.repositories.UsersRepository;
import io.tech1.framework.b2b.base.security.jwt.tests.contexts.TestsApplicationValidatorsContext;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseUsersValidator;
import io.tech1.framework.b2b.base.security.jwt.validators.abtracts.AbstractBaseUsersValidator;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.stream.Stream;

import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomZoneId;
import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AbstractBaseUsersValidatorTest {

    private static Stream<Arguments> validateUserChangePasswordRequestBasicArgs() {
        return Stream.of(
                Arguments.of(new RequestUserChangePasswordBasic(null, null), invalidAttribute("newPassword")),
                Arguments.of(new RequestUserChangePasswordBasic(Password.of("simple"), null), invalidAttribute("confirmPassword")),
                Arguments.of(new RequestUserChangePasswordBasic(Password.of("simple"), Password.of(randomString())), "New password should contain an uppercase latin letter, a lowercase latin letter, a number and be at least 8 characters long"),
                Arguments.of(new RequestUserChangePasswordBasic(Password.of("Simple"), Password.of(randomString())), "New password should contain an uppercase latin letter, a lowercase latin letter, a number and be at least 8 characters long"),
                Arguments.of(new RequestUserChangePasswordBasic(Password.of("Simple1"), Password.of(randomString())), "New password should contain an uppercase latin letter, a lowercase latin letter, a number and be at least 8 characters long"),
                Arguments.of(new RequestUserChangePasswordBasic(Password.of("ComPLEx12"), Password.of("NoMatch")), "Provided new password and confirm password are not the same"),
                Arguments.of(new RequestUserChangePasswordBasic(Password.of("ComPLEx12"), Password.of("ComPLEx12")), null)
        );
    }

    @Configuration
    @Import({
            TestsApplicationValidatorsContext.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final UsersRepository usersRepository;

        @Bean
        BaseUsersValidator baseUsersValidator() {
            return new AbstractBaseUsersValidator(
                    this.usersRepository
            ) {};
        }
    }

    private final UsersRepository usersRepository;

    private final BaseUsersValidator componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.usersRepository
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.usersRepository
        );
    }

    @Test
    void validateUserUpdateRequest1InvalidZoneIdTest() {
        // Arrange
        var username = entity(Username.class);
        var requestUserUpdate1 = new RequestUserUpdate1("invalidZoneId", Email.random(), randomString());

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateUserUpdateRequest1(username, requestUserUpdate1));

        // Assert
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Attribute `zoneId` is invalid");
    }

    @Test
    void validateUserUpdateRequest1InvalidEmailTest() {
        // Arrange
        var username = entity(Username.class);
        var requestUserUpdate1 = new RequestUserUpdate1(randomZoneId().getId(), Email.of(randomString()), randomString());

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateUserUpdateRequest1(username, requestUserUpdate1));

        // Assert
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Attribute `email` is invalid");
    }

    @Test
    void validateUserUpdateRequest1EmailValidNoUserTest() {
        // Arrange
        var username = entity(Username.class);
        var email = Email.random();
        when(this.usersRepository.findByEmailAsJwtUserOrNull(email)).thenReturn(null);
        var requestUserUpdate1 = new RequestUserUpdate1(randomZoneId().getId(), email, randomString());

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateUserUpdateRequest1(username, requestUserUpdate1));

        // Assert
        assertThat(throwable).isNull();
        verify(this.usersRepository).findByEmailAsJwtUserOrNull(email);
    }

    @Test
    void validateUserUpdateRequest1EmailValidUserFoundTest() {
        // Arrange
        var user= entity(JwtUser.class);
        var email = Email.random();
        when(this.usersRepository.findByEmailAsJwtUserOrNull(email)).thenReturn(user);
        var requestUserUpdate1 = new RequestUserUpdate1(randomZoneId().getId(), email, randomString());

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateUserUpdateRequest1(user.username(), requestUserUpdate1));

        // Assert
        assertThat(throwable).isNull();
        verify(this.usersRepository).findByEmailAsJwtUserOrNull(email);
    }

    @Test
    void validateUserUpdateRequest1EmailValidTwoUsersTest() {
        // Arrange
        var username = entity(Username.class);
        var email = Email.random();
        var user = entity(JwtUser.class);
        when(this.usersRepository.findByEmailAsJwtUserOrNull(email)).thenReturn(user);
        var requestUserUpdate1 = new RequestUserUpdate1(randomZoneId().getId(), email, randomString());

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateUserUpdateRequest1(username, requestUserUpdate1));

        // Assert
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Attribute `email` is invalid");
        verify(this.usersRepository).findByEmailAsJwtUserOrNull(email);
    }

    @Test
    void validateUserUpdateRequest2InvalidZoneIdTest() {
        // Arrange
        var requestUserUpdate2 = new RequestUserUpdate2("invalidZoneId", randomString());

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateUserUpdateRequest2(requestUserUpdate2));

        // Assert
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Attribute `zoneId` is invalid");
    }

    @Test
    void validateUserUpdateRequest2Test() {
        // Arrange
        var requestUserUpdate2 = new RequestUserUpdate2(randomZoneId().getId(), randomString());

        // Act
        this.componentUnderTest.validateUserUpdateRequest2(requestUserUpdate2);

        // Assert
        // no asserts
    }

    @ParameterizedTest
    @MethodSource("validateUserChangePasswordRequestBasicArgs")
    void validateUserChangePasswordRequestBasic(RequestUserChangePasswordBasic request, String exceptionMessage) {
        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateUserChangePasswordRequestBasic(request));

        // Assert
        if (nonNull(exceptionMessage)) {
            assertThat(throwable).isNotNull();
            assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
            assertThat(throwable.getMessage()).isEqualTo(exceptionMessage);
        } else {
            assertThat(throwable).isNull();
        }
    }
}
