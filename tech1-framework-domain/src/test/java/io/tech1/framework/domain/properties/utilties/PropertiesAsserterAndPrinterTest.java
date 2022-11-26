package io.tech1.framework.domain.properties.utilties;

import io.tech1.framework.domain.hardware.monitoring.HardwareName;
import io.tech1.framework.domain.properties.base.*;
import io.tech1.framework.domain.properties.configs.EmailConfigs;
import io.tech1.framework.domain.properties.configs.HardwareMonitoringConfigs;
import io.tech1.framework.domain.properties.configs.IncidentConfigs;
import io.tech1.framework.domain.properties.configs.MvcConfigs;
import io.tech1.framework.domain.properties.configs.incidents.IncidentFeaturesConfigs;
import io.tech1.framework.domain.tests.classes.NotUsedPropertiesConfigs;
import io.tech1.framework.domain.tests.constants.TestsConstants;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.tech1.framework.domain.properties.configs.incidents.IncidentFeatureConfigs.disabledIncidentFeatureConfigs;
import static io.tech1.framework.domain.properties.configs.incidents.IncidentFeatureConfigs.enabledIncidentFeatureConfigs;
import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertProperties;
import static io.tech1.framework.domain.properties.utilities.PropertiesPrinter.printProperties;
import static io.tech1.framework.domain.tests.constants.TestsPropertiesConstants.*;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomBoolean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

public class PropertiesAsserterAndPrinterTest {

    @Test
    public void notUsedPropertiesConfigsTest() {
        // Arrange
        var notUsedPropertiesConfigs = NotUsedPropertiesConfigs.of(
                ScheduledJob.of(true, SchedulerConfiguration.of(10, 10, TimeUnit.SECONDS)),
                SpringServer.of(8080),
                SpringLogging.of("logback-test.xml")
        );

        // Act
        assertProperties(notUsedPropertiesConfigs, "notUsedPropertiesConfigs");
        printProperties(notUsedPropertiesConfigs);

        // Assert
        // no asserts
    }

    @Test
    public void asyncConfigsTest() {
        // Act
        assertProperties(ASYNC_CONFIGS, "asyncConfigs");
        printProperties(ASYNC_CONFIGS);

        // Assert
        // no asserts
    }

    @Test
    public void eventsConfigsTest() {
        // Act
        assertProperties(EVENTS_CONFIGS, "eventsConfigs");
        printProperties(EVENTS_CONFIGS);

        // Assert
        // no asserts
    }

    @Test
    public void mvcConfigsDisabledTest() {
        // Arrange
        var mvcConfigs = MvcConfigs.of(
                false,
                null,
                null
        );

        // Act
        assertProperties(mvcConfigs, "mvcConfigs");
        printProperties(mvcConfigs);

        // Assert
        // no asserts
    }

    @Test
    public void mvcConfigsTest() {
        // Act
        assertProperties(MVC_CONFIGS, "mvcConfigs");
        printProperties(MVC_CONFIGS);

        // Assert
        // no asserts
    }

    @Test
    public void emailConfigsDisabledTest() {
        // Arrange
        var emailConfigs = EmailConfigs.of(
                false,
                null,
                null,
                null,
                null,
                null,
                null
        );

        // Act
        assertProperties(emailConfigs, "emailConfigs");
        printProperties(emailConfigs);

        // Assert
        // no asserts
    }

    @Test
    public void emailConfigsTest() {
        // Act
        assertProperties(EMAIL_CONFIGS, "emailConfigs");
        printProperties(EMAIL_CONFIGS);

        // Assert
        // no asserts
    }

    @RepeatedTest(TestsConstants.SMALL_ITERATIONS_COUNT)
    public void incidentConfigsCorrectFeaturesTest() {
        var flag = randomBoolean();
        var loginFailureUsernamePassword = flag ? enabledIncidentFeatureConfigs() : disabledIncidentFeatureConfigs();
        var loginFailureUsernameMaskedPassword = !flag ? enabledIncidentFeatureConfigs() : disabledIncidentFeatureConfigs();
        var incidentConfigs = IncidentConfigs.of(
                true,
                RemoteServer.of(
                        "http://localhost:8973",
                        "incident-user",
                        "incident-password"
                ),
                IncidentFeaturesConfigs.of(
                        randomBoolean() ? enabledIncidentFeatureConfigs() : disabledIncidentFeatureConfigs(),
                        loginFailureUsernamePassword,
                        loginFailureUsernameMaskedPassword,
                        randomBoolean() ? enabledIncidentFeatureConfigs() : disabledIncidentFeatureConfigs(),
                        randomBoolean() ? enabledIncidentFeatureConfigs() : disabledIncidentFeatureConfigs(),
                        randomBoolean() ? enabledIncidentFeatureConfigs() : disabledIncidentFeatureConfigs(),
                        randomBoolean() ? enabledIncidentFeatureConfigs() : disabledIncidentFeatureConfigs(),
                        randomBoolean() ? enabledIncidentFeatureConfigs() : disabledIncidentFeatureConfigs()
                )
        );

        // Act
        assertProperties(incidentConfigs, "incidentConfigs");
        printProperties(incidentConfigs);

        // Assert
        // no asserts
    }

    @Test
    public void incidentConfigsExceptionFeaturesTest() {
        var incidentConfigs = IncidentConfigs.of(
                true,
                RemoteServer.of(
                        "http://localhost:8973",
                        "incident-user",
                        "incident-password"
                ),
                IncidentFeaturesConfigs.of(
                        randomBoolean() ? enabledIncidentFeatureConfigs() : disabledIncidentFeatureConfigs(),
                        enabledIncidentFeatureConfigs(),
                        enabledIncidentFeatureConfigs(),
                        randomBoolean() ? enabledIncidentFeatureConfigs() : disabledIncidentFeatureConfigs(),
                        randomBoolean() ? enabledIncidentFeatureConfigs() : disabledIncidentFeatureConfigs(),
                        randomBoolean() ? enabledIncidentFeatureConfigs() : disabledIncidentFeatureConfigs(),
                        randomBoolean() ? enabledIncidentFeatureConfigs() : disabledIncidentFeatureConfigs(),
                        randomBoolean() ? enabledIncidentFeatureConfigs() : disabledIncidentFeatureConfigs()
                )
        );

        // Act
        var throwable = catchThrowable(() -> assertProperties(incidentConfigs, "incidentConfigs"));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable.getClass()).isEqualTo(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).isEqualTo("Please configure login failure incident feature. Only one feature type could be enabled");
    }

    @Test
    public void incidentConfigsTest() {
        // Act
        assertProperties(INCIDENT_CONFIGS, "incidentConfigs");
        printProperties(INCIDENT_CONFIGS);

        // Assert
        // no asserts
    }

    @Test
    public void hardwareMonitoringConfigsExceptionTest() {
        // Arrange
        var hardwareMonitoringConfigs = HardwareMonitoringConfigs.of(
                Map.of(
                        HardwareName.CPU, new BigDecimal("80"),
                        HardwareName.HEAP, new BigDecimal("85")
                )
        );

        // Act
        var throwable = catchThrowable(() -> assertProperties(hardwareMonitoringConfigs, "hardwareMonitoringConfigs"));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable.getClass()).isEqualTo(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).isEqualTo("Attribute `hardwareMonitoringConfigs.thresholdsConfigs` must contains 5 HardwareName elements");
    }

    @Test
    public void hardwareMonitoringConfigsTest() {
        // Act
        assertProperties(HARDWARE_MONITORING_CONFIGS, "hardwareMonitoringConfigs");
        printProperties(HARDWARE_MONITORING_CONFIGS);

        // Assert
        // no asserts
    }

    @Test
    public void hardwareServerConfigsTest() {
        // Act
        assertProperties(HARDWARE_SERVER_CONFIGS, "hardwareServerConfigs");
        printProperties(HARDWARE_SERVER_CONFIGS);

        // Assert
        // no asserts
    }

    @Test
    public void securityJwtConfigsTest() {
        // Act
        assertProperties(SECURITY_JWT_CONFIGS, "securityJwtConfigs");
        printProperties(SECURITY_JWT_CONFIGS);

        // Assert
        // no asserts
    }

    @Test
    public void securityJwtWebsocketsConfigsTest() {
        // Act
        assertProperties(SECURITY_JWT_WEBSOCKETS_CONFIGS, "securityJwtWebsocketsConfigs");
        printProperties(SECURITY_JWT_WEBSOCKETS_CONFIGS);

        // Assert
        // no asserts
    }
}
