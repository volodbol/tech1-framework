package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomBoolean;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class InvitationCodes extends AbstractTogglePropertyConfigs {
    @MandatoryProperty
    private final boolean enabled;

    public static InvitationCodes testsHardcoded() {
        return new InvitationCodes(true);
    }

    public static InvitationCodes random() {
        return randomBoolean() ? enabled() : disabled();
    }

    public static InvitationCodes enabled() {
        return testsHardcoded();
    }

    public static InvitationCodes disabled() {
        return new InvitationCodes(false);
    }
}
