package io.tech1.framework.b2b.base.security.jwt.domain.jwt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;

public record JwtAccessToken(@NotNull String value) {

    @JsonCreator
    public static JwtAccessToken of(String value) {
        return new JwtAccessToken(value);
    }

    public static JwtAccessToken random() {
        return new JwtAccessToken(randomString());
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}