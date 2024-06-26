package io.tech1.framework.utilities.geo.functions.ipapi.utility.impl;

import io.tech1.framework.domain.exceptions.geo.GeoLocationNotFoundException;
import io.tech1.framework.domain.http.requests.IPAddress;
import io.tech1.framework.domain.properties.ApplicationFrameworkProperties;
import io.tech1.framework.domain.properties.ApplicationFrameworkPropertiesTestsHardcodedContext;
import io.tech1.framework.utilities.geo.facades.GeoCountryFlagUtility;
import io.tech1.framework.utilities.geo.facades.impl.GeoCountryFlagUtilityImpl;
import io.tech1.framework.utilities.geo.functions.ipapi.configurations.IPAPIConfiguration;
import io.tech1.framework.utilities.geo.functions.ipapi.feign.IPAPIFeign;
import io.tech1.framework.utilities.geo.functions.ipapi.utility.IPAPIGeoLocationUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@Slf4j
@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class IPAPIGeoLocationUtilityImplConsoleTest {

    @Configuration
    @Import({
            IPAPIConfiguration.class,
            ApplicationFrameworkPropertiesTestsHardcodedContext.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final IPAPIFeign ipapiFeign;
        private final ResourceLoader resourceLoader;
        private final ApplicationFrameworkProperties applicationFrameworkProperties;

        @Bean
        GeoCountryFlagUtility geoCountryFlagUtility() {
            return new GeoCountryFlagUtilityImpl(
                    this.resourceLoader,
                    this.applicationFrameworkProperties
            );
        }

        @Bean
        IPAPIGeoLocationUtility ipapiGeoLocationUtility() {
            return new IPAPIGeoLocationUtilityImpl(
                    this.ipapiFeign,
                    this.geoCountryFlagUtility()
            );
        }
    }

    private final IPAPIGeoLocationUtility componentUnderTest;

    @Test
    @Disabled
    void australiaTest() throws GeoLocationNotFoundException {
        // Act
        var geoLocation = this.componentUnderTest.getGeoLocation(new IPAddress("1.1.1.1"));

        // Assert
        LOGGER.debug("geoLocation: " + geoLocation);
    }

    @Test
    @Disabled
    void localhostTest() throws GeoLocationNotFoundException {
        // Act
        var geoLocation = this.componentUnderTest.getGeoLocation(new IPAddress("127.0.0.1"));

        // Assert
        LOGGER.debug("geoLocation: " + geoLocation);
    }
}
