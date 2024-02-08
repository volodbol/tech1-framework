package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.configs.utilities.GeoLocationsConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class UtilitiesConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private final GeoLocationsConfigs geoLocationsConfigs;

    public static UtilitiesConfigs testsHardcoded() {
        return new UtilitiesConfigs(
                GeoLocationsConfigs.disabled()
        );
    }

    public static UtilitiesConfigs random() {
        return new UtilitiesConfigs(
                GeoLocationsConfigs.random()
        );
    }
}
