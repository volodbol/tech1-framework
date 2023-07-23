package io.tech1.framework.domain.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.tech1.framework.domain.utilities.time.TimestampUtility.getCurrentTimestamp;
import static java.time.temporal.ChronoUnit.*;
import static org.assertj.core.api.Assertions.assertThat;

class TimeAgoTest {

    private static Stream<Arguments> constructorTest() {
        return Stream.of(
                Arguments.of(getCurrentTimestamp(), "just now"),
                Arguments.of(getCurrentTimestamp() - TimeAmount.of(10L, SECONDS).toMillis(), "10 seconds ago"),
                Arguments.of(getCurrentTimestamp() - TimeAmount.of(1L, MINUTES).toMillis(), "1 minute ago"),
                Arguments.of(getCurrentTimestamp() - TimeAmount.of(10L, MINUTES).toMillis(), "10 minutes ago"),
                Arguments.of(getCurrentTimestamp() - TimeAmount.of(2L, HOURS).toMillis(), "2 hours ago"),
                Arguments.of(getCurrentTimestamp() - TimeAmount.of(25L, DAYS).toMillis(), "25 days ago"),
                Arguments.of(getCurrentTimestamp() - TimeAmount.of(25L, MONTHS).toMillis(), "2 years ago"),
                Arguments.of(getCurrentTimestamp() - TimeAmount.of(2L, YEARS).toMillis(), "2 years ago")
        );
    }

    @ParameterizedTest
    @MethodSource("constructorTest")
    void constructorTest(long timestamp, String expected) {
        // Act
        var actual = TimeAgo.of(timestamp);

        // Assert
        assertThat(actual.getValue()).isEqualTo(expected);
        assertThat(actual.toString()).isEqualTo(expected);
    }
}
