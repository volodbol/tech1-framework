package io.tech1.framework.domain.http.requests;

import io.tech1.framework.domain.constants.StringConstants;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

import static io.tech1.framework.domain.constants.StringConstants.UNDEFINED;
import static io.tech1.framework.domain.constants.StringConstants.UNKNOWN;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomBoolean;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomElement;
import static java.util.Objects.nonNull;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class UserAgentDetails {
    private static final List<String> TEST_DATA_BROWSERS = List.of("Chrome", "Mozilla", "Opera", "Edge");
    private static final List<String> TEST_DATA_PLATFORMS = List.of("macOS", "linux", "windows");

    private final String browser;
    private final String platform;
    private final String deviceType;
    private final String exceptionDetails;

    public UserAgentDetails(
            String browser,
            String platform,
            String deviceType,
            String exceptionDetails
    ) {
        this.browser = nonNull(browser) ? browser: UNKNOWN;
        this.platform = nonNull(platform) ? platform: UNKNOWN;
        this.deviceType = nonNull(deviceType) ? deviceType: UNKNOWN;
        this.exceptionDetails = exceptionDetails;
    }

    public static UserAgentDetails unknown(
            String exceptionDetails
    ) {
        return new UserAgentDetails(
                UNKNOWN,
                UNKNOWN,
                UNKNOWN,
                exceptionDetails
        );
    }

    public static UserAgentDetails processing() {
        return new UserAgentDetails(
                UNDEFINED,
                UNDEFINED,
                UNDEFINED,
                StringConstants.EMPTY
        );
    }

    public static UserAgentDetails processed(
            String browser,
            String platform,
            String deviceType
    ) {
        return new UserAgentDetails(
                browser,
                platform,
                deviceType,
                StringConstants.EMPTY
        );
    }

    public static UserAgentDetails valid() {
        return UserAgentDetails.processed(
                "Chrome",
                "macOS",
                "Desktop"
        );
    }

    public static UserAgentDetails invalid() {
        return UserAgentDetails.unknown(
                "User agent details are unknown"
        );
    }

    public static UserAgentDetails random() {
        return randomBoolean() ? valid() : invalid();
    }

    public static UserAgentDetails testData() {
        return UserAgentDetails.processed(
                randomElement(TEST_DATA_BROWSERS),
                randomElement(TEST_DATA_PLATFORMS),
                "Desktop"
        );
    }

    public String getWhat() {
        return this.browser + ", " + this.platform + " on " + this.deviceType;
    }
}
