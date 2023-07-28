package io.tech1.framework.b2b.base.security.jwt.domain.dto.requests;

import com.fasterxml.jackson.core.type.TypeReference;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.tests.runners.AbstractSerializationDeserializationRunner;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static io.tech1.framework.domain.tests.constants.TestsConstants.TECH1;
import static org.assertj.core.api.Assertions.assertThat;

class RequestUserLoginTest extends AbstractSerializationDeserializationRunner {
    private static final RequestUserLogin REQUEST = new RequestUserLogin(
            TECH1,
            Password.of("password123")
    );

    @Override
    protected String getFolder() {
        return "dto/requests";
    }

    @Override
    protected String getFileName() {
        return "user-login-invitation-code-id-1.json";
    }

    // serialization is not required for request-based dtos

    @SneakyThrows
    @Test
    void deserializeTest() {
        // Arrange
        var json = this.readFile();
        var typeReference = new TypeReference<RequestUserLogin>() {};

        // Act
        var actual = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(actual).isEqualTo(REQUEST);
    }
}
