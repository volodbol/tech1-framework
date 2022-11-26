package io.tech1.framework.incidents.domain.throwable;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class ThrowableIncidentTest {

    @Test
    public void of1Test() {
        // Arrange
        var throwable = mock(Throwable.class);

        // Act
        var actual = ThrowableIncident.of(throwable);

        // Assert
        assertThat(actual.getThrowable()).isEqualTo(throwable);
        assertThat(actual.getMethod()).isNull();
        assertThat(actual.getParams()).isNull();
        assertThat(actual.getAttributes()).isNull();
    }

    @Test
    public void of2Test() {
        // Arrange
        var throwable = mock(Throwable.class);
        var method = randomMethod();
        var params = List.of(new Object(), randomString(), randomLong());

        // Act
        var actual = ThrowableIncident.of(throwable, method, params);

        // Assert
        assertThat(actual.getThrowable()).isEqualTo(throwable);
        assertThat(actual.getMethod()).isEqualTo(method);
        assertThat(actual.getParams()).isEqualTo(params);
        assertThat(actual.getAttributes()).isNull();
    }

    @Test
    public void of3Test() {
        // Arrange
        var throwable = mock(Throwable.class);
        var attributes = Map.of(randomString(), new Object());

        // Act
        var actual = ThrowableIncident.of(throwable, attributes);

        // Assert
        assertThat(actual.getThrowable()).isEqualTo(throwable);
        assertThat(actual.getMethod()).isNull();
        assertThat(actual.getParams()).isNull();
        assertThat(actual.getAttributes()).isEqualTo(attributes);
    }
}
