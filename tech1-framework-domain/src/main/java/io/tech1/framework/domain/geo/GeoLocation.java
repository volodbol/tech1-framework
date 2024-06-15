package io.tech1.framework.domain.geo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.tech1.framework.domain.constants.StringConstants;
import io.tech1.framework.domain.http.requests.IPAddress;
import io.tech1.framework.domain.tests.constants.TestsFlagsConstants;
import io.tech1.framework.domain.tuples.Tuple5;
import io.tech1.framework.domain.utilities.random.RandomUtility;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

import static io.tech1.framework.domain.http.requests.IPAddress.localhost;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomBoolean;
import static io.tech1.framework.domain.utilities.strings.StringUtility.hasLength;
import static java.util.Objects.nonNull;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class GeoLocation {
    private static final List<Tuple5<String, String, String, String, String>> TEST_DATA = List.of(
            new Tuple5<>("103.194.114.1", "Ukraine", "UA", TestsFlagsConstants.UKRAINE, "Lviv"),
            new Tuple5<>("103.194.114.2", "Ukraine", "UA", TestsFlagsConstants.UKRAINE, "Kyiv"),
            new Tuple5<>("1.186.0.1", "United Kingdom", "UK", TestsFlagsConstants.UK, "London"),
            new Tuple5<>("1.186.0.2", "United Kingdom", "UK", TestsFlagsConstants.UK, "Manchester"),
            new Tuple5<>("55.255.1.1", "USA", "US", TestsFlagsConstants.USA, "New York"),
            new Tuple5<>("55.255.1.2", "USA", "US", TestsFlagsConstants.USA, "Washington"),
            new Tuple5<>("55.255.1.3", "USA", "US", TestsFlagsConstants.USA, "Las Vegas"),
            new Tuple5<>("55.255.1.4", "USA", "US", TestsFlagsConstants.USA, "Los Angeles"),
            new Tuple5<>("149.90.0.1", "Portugal", "PT", TestsFlagsConstants.PORTUGAL, "Porto"),
            new Tuple5<>("149.90.0.2", "Portugal", "PT", TestsFlagsConstants.PORTUGAL, "Lisbon")
    );

    private final String ipAddr;
    private final String country;
    private final String countryCode;
    private final String countryFlag;
    @JsonIgnore
    private final String city;
    @JsonIgnore
    private final String exceptionDetails;

    public GeoLocation(
            String ipAddr,
            String country,
            String countryCode,
            String countryFlag,
            String city,
            String exceptionDetails
    ) {
        this.ipAddr = ipAddr;
        if (nonNull(countryCode)) {
            this.countryCode = countryCode;
        } else {
            this.countryCode = StringConstants.UNKNOWN;
        }
        if (nonNull(countryFlag)) {
            this.countryFlag = countryFlag;
        } else {
            this.countryFlag = TestsFlagsConstants.UNKNOWN;
        }
        if (nonNull(country)) {
            this.country = country.trim();
            this.city = nonNull(city) ? city.trim() : null;
        } else {
            this.country = StringConstants.UNKNOWN;
            this.city = null;
        }
        this.exceptionDetails = exceptionDetails;
    }

    public static GeoLocation unknown(
            IPAddress ipAddress,
            String exceptionDetails
    ) {
        return new GeoLocation(
                getIpAddrOrUnknown(ipAddress),
                StringConstants.UNKNOWN,
                StringConstants.UNKNOWN,
                TestsFlagsConstants.UNKNOWN,
                StringConstants.UNKNOWN,
                exceptionDetails
        );
    }

    public static GeoLocation processing(
            IPAddress ipAddress
    ) {
        return new GeoLocation(
                getIpAddrOrUnknown(ipAddress),
                StringConstants.UNDEFINED,
                StringConstants.UNDEFINED,
                TestsFlagsConstants.UNKNOWN,
                StringConstants.UNDEFINED,
                StringConstants.EMPTY
        );
    }

    public static GeoLocation processed(
            IPAddress ipAddress,
            String country,
            String countryCode,
            String countryFlag,
            String city
    ) {
        return new GeoLocation(
                getIpAddrOrUnknown(ipAddress),
                country,
                countryCode,
                countryFlag,
                city,
                StringConstants.EMPTY
        );
    }

    public static GeoLocation valid() {
        return GeoLocation.processed(
                localhost(),
                "Ukraine",
                "UA",
                "🇺🇦",
                "Lviv"
        );
    }

    public static GeoLocation invalid() {
        return GeoLocation.unknown(
                localhost(),
                "Location is unknown"
        );
    }

    public static GeoLocation random() {
        return randomBoolean() ? valid() : invalid();
    }

    public static GeoLocation testData() {
        var tuple5 = RandomUtility.randomElement(TEST_DATA);
        return GeoLocation.processed(
                new IPAddress(tuple5.a()),
                tuple5.b(),
                tuple5.c(),
                tuple5.d(),
                tuple5.e()
        );
    }

    public String getWhere() {
        var countryPresent = hasLength(this.country);
        var cityPresent = hasLength(this.city);
        var countryFlagPrefix = hasLength(this.countryFlag) ? this.countryFlag + " " : StringConstants.EMPTY;
        if (countryPresent && !cityPresent) {
            return countryFlagPrefix + this.country;
        }
        if (countryPresent) {
            return countryFlagPrefix + this.country + ", " + this.city;
        }
        return TestsFlagsConstants.UNKNOWN + " " + StringConstants.UNKNOWN;
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    public static String getIpAddrOrUnknown(IPAddress ipAddress) {
        return nonNull(ipAddress) ? ipAddress.value() : StringConstants.UNKNOWN;
    }
}
