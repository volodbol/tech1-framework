package io.tech1.framework.utilities.geo.functions.mindmax.impl;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import io.tech1.framework.domain.geo.GeoLocation;
import io.tech1.framework.domain.http.requests.IPAddress;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import io.tech1.framework.utilities.geo.facades.GeoCountryFlagUtility;
import io.tech1.framework.utilities.geo.functions.mindmax.MindMaxGeoLocationUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.FRAMEWORK_UTILITIES_PREFIX;
import static io.tech1.framework.domain.constants.FrameworkLogsConstants.LINE_SEPARATOR_INTERPUNCT;
import static io.tech1.framework.domain.enums.Status.FAILURE;
import static io.tech1.framework.domain.enums.Status.SUCCESS;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.contactDevelopmentTeam;

@Slf4j
@Component
public class MindMaxGeoLocationUtilityImpl implements MindMaxGeoLocationUtility {
    private static final String GEO_DATABASE_NAME = "GeoLite2-City.mmdb";

    // Database
    private final DatabaseReader databaseReader;
    // Utilities
    private final GeoCountryFlagUtility geoCountryFlagUtility;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Autowired
    public MindMaxGeoLocationUtilityImpl(
            ResourceLoader resourceLoader,
            GeoCountryFlagUtility geoCountryFlagUtility,
            ApplicationFrameworkProperties applicationFrameworkProperties
    ) {
        this.geoCountryFlagUtility = geoCountryFlagUtility;
        this.applicationFrameworkProperties = applicationFrameworkProperties;
        LOGGER.info(LINE_SEPARATOR_INTERPUNCT);
        if (applicationFrameworkProperties.getUtilitiesConfigs().getGeoLocationsConfigs().isGeoLiteCityDatabaseEnabled()) {
            try {
                LOGGER.info("{} {} database is enabled", FRAMEWORK_UTILITIES_PREFIX, GEO_DATABASE_NAME);
                var resource = resourceLoader.getResource("classpath:" + GEO_DATABASE_NAME);
                var inputStream = resource.getInputStream();
                this.databaseReader = new DatabaseReader.Builder(inputStream).build();
                LOGGER.info("{} {} database loading status: {}", FRAMEWORK_UTILITIES_PREFIX, GEO_DATABASE_NAME, SUCCESS);
            } catch (IOException | RuntimeException ex) {
                var message = String.format("%s %s database loading status: %s", FRAMEWORK_UTILITIES_PREFIX, GEO_DATABASE_NAME, FAILURE);
                LOGGER.error(message);
                LOGGER.error("Please visit https://dev.maxmind.com/ and download `GeoLite2-City.mmdb` database");
                LOGGER.error("Please add `GeoLite2-City.mmdb` database to classpath");
                throw new IllegalArgumentException(message + ". " + ex.getMessage());
            }
        } else {
            LOGGER.warn("{} Geo location {} database is disabled", FRAMEWORK_UTILITIES_PREFIX, GEO_DATABASE_NAME);
            this.databaseReader = null;
        }
        LOGGER.info(LINE_SEPARATOR_INTERPUNCT);
    }

    @Override
    public GeoLocation getGeoLocation(IPAddress ipAddress) {
        if (!this.applicationFrameworkProperties.getUtilitiesConfigs().getGeoLocationsConfigs().isGeoLiteCityDatabaseEnabled()) {
            return GeoLocation.unknown(ipAddress, contactDevelopmentTeam("Geo configurations failure"));
        }
        try {
            var inetAddress = InetAddress.getByName(ipAddress.value());
            var response = this.databaseReader.city(inetAddress);
            var countryCode = response.getCountry().getIsoCode();
            var countryFlag = this.geoCountryFlagUtility.getFlagEmojiByCountryCode(countryCode);
            return GeoLocation.processed(
                    ipAddress,
                    response.getCountry().getName(),
                    countryCode,
                    countryFlag,
                    response.getCity().getName()
            );
        } catch (IOException | GeoIp2Exception ex) {
            return GeoLocation.unknown(ipAddress, ex.getMessage());
        }
    }
}
